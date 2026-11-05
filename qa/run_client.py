#!/usr/bin/env python3
"""Launch a prepared NeoForge 26.1.2/26.1.2.122 client in its own QA directory.

Usage: DISPLAY_NUM=131 python3 qa/run_client.py <client_dir> <multiplayer|-> <label> [seconds]
The multiplayer target is the isolated loopback server at 127.0.0.1:25672.
Requires a prepared assets/libraries/natives/versions cache and explicit mods in client_dir/mods.
Snapshots the dedicated display every 20 seconds, then stops only its own JVM and Xvfb.
No launcher installation, mod copying or other worktree mutation is performed.
"""
import json
import os
import pathlib
import shutil
import signal
import subprocess
import sys
import time

CLIENT = pathlib.Path(sys.argv[1]).resolve()
WORLD = sys.argv[2]
LABEL = sys.argv[3]
SECONDS = int(sys.argv[4]) if len(sys.argv) > 4 else 150

MC = "26.1.2"
NF = "neoforge-26.1.2.122"
# dedicated display: other agents on this host use :99/:101/:103
DISPLAY = ":" + os.environ.get("DISPLAY_NUM", "97")


def load(path):
    return json.load(open(path))


def lib_path(lib, libraries_dir):
    art = lib.get("downloads", {}).get("artifact")
    if art:
        return libraries_dir / art["path"]
    # fall back to maven coordinates
    name = lib["name"].split(":")
    return libraries_dir / name[0].replace(".", "/") / name[1] / name[2] / \
        f"{name[1]}-{name[2]}{'-' + name[3] if len(name) > 3 else ''}.jar"


def allowed(entry, os_name="linux", arch="x86_64"):
    rules = entry.get("rules")
    if not rules:
        return True
    result = False
    for rule in rules:
        r = rule
        ok = True
        osr = r.get("os")
        if osr:
            if "name" in osr and osr["name"] != os_name:
                ok = False
            if "arch" in osr and osr["arch"] not in (arch, "x86"):
                ok = False
        feat = r.get("features")
        if feat:
            ok = False  # demo / custom resolution / quickplay features: handled manually
        if ok:
            result = r["action"] == "allow"
    return result


def build_command():
    child = load(CLIENT / "versions" / NF / f"{NF}.json")
    parent = load(CLIENT / "versions" / MC / f"{MC}.json")
    libraries_dir = CLIENT / "libraries"

    jars = []
    seen = set()
    for lib in child["libraries"] + parent["libraries"]:
        if not allowed(lib):
            continue
        p = lib_path(lib, libraries_dir)
        if p.exists() and p not in seen:
            seen.add(p)
            jars.append(str(p))
    jars.append(str(CLIENT / "versions" / MC / f"{MC}.jar"))

    natives = CLIENT / "natives"
    natives.mkdir(exist_ok=True)

    subst = {
        "auth_player_name": "QA_Player",
        "version_name": NF,
        "game_directory": str(CLIENT),
        "assets_root": str(CLIENT / "assets"),
        "assets_index_name": parent["assetIndex"]["id"],
        "auth_uuid": "0000000000004d00b1a5e0f0a1b2c3d4",
        "auth_access_token": "0",
        "clientid": "0",
        "auth_xuid": "0",
        "user_type": "msa",
        "user_properties": "{}",
        "version_type": parent.get("type", "release"),
        "natives_directory": str(natives),
        "launcher_name": "fleet-qa",
        "launcher_version": "1.0.0",
        "library_directory": str(libraries_dir),
        "classpath": ":".join(jars),
        "classpath_separator": ":",
        "quickPlayPath": str(CLIENT / "quickPlay"),
        "quickPlaySingleplayer": WORLD,
        "resolution_width": "1280",
        "resolution_height": "720",
    }

    def expand(value):
        if isinstance(value, list):
            return [expand(v) for v in value]
        for k, v in subst.items():
            value = value.replace("${" + k + "}", v)
        return value

    args = []
    for entry in parent["arguments"]["jvm"] + child.get("arguments", {}).get("jvm", []):
        if isinstance(entry, dict) and not allowed(entry, arch="x86"):
            continue
        val = entry if isinstance(entry, str) else entry.get("value")
        args += [expand(val)] if isinstance(val, str) else expand(val)

    game = []
    for entry in parent["arguments"]["game"]:
        if isinstance(entry, str):
            game.append(expand(entry))
        elif allowed(entry):
            val = entry.get("value")
            game += [expand(val)] if isinstance(val, str) else expand(val)
    game += [expand(v) for v in child.get("arguments", {}).get("game", [])]
    if WORLD != "-":
        game += ["--quickPlayMultiplayer", "127.0.0.1:25672"]

    heap = os.environ.get("CLIENT_HEAP", "1500M")
    cmd = ["java", "-Dio.netty.transport.noNative=true", "-Xms" + ("512M" if heap.endswith("M") and int(heap[:-1]) < 1024 else "768M"),
           "-Xmx" + heap, *args, child["mainClass"], *game]
    return cmd


def main():
    shots = CLIENT / f"shots-{LABEL}"
    if shots.exists():
        shutil.rmtree(shots)
    shots.mkdir()

    # fresh Xvfb
    pass  # This runner never kills an existing display.
    time.sleep(1)
    xvfb = subprocess.Popen(
        ["Xvfb", DISPLAY, "-screen", "0", "1280x720x24", "-ac"],
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    time.sleep(2)

    env = dict(os.environ, DISPLAY=DISPLAY)
    log_path = CLIENT / f"run-{LABEL}.log"
    cmd = build_command()
    (CLIENT / f"cmd-{LABEL}.txt").write_text(" ".join(cmd) + "\n")
    with log_path.open("w") as log:
        proc = subprocess.Popen(cmd, cwd=CLIENT, env=env, stdout=log,
                                stderr=subprocess.STDOUT, start_new_session=True)

        n = 0
        deadline = time.monotonic() + SECONDS
        while time.monotonic() < deadline:
            time.sleep(20)
            n += 1
            if n == 1:
                # no window manager on Xvfb -> grant input focus explicitly, otherwise the
                # game auto-pauses on the first tick and ignores clicks
                r = subprocess.run(["xdotool", "search", "--name", "Minecraft"],
                                   env=env, capture_output=True, text=True)
                for wid in r.stdout.split():
                    subprocess.run(["xdotool", "windowfocus", "--sync", wid],
                                   env=env, capture_output=True)
            if n in (2, 3) and os.environ.get("NO_AUTO_CLICK") != "1":
                # first-launch narrator/accessibility dialog blocks quickPlay. No window
                # manager on Xvfb -> no input focus, so click "Continue" by coordinates
                # (window geometry is stable: dialog button at 639,567 on the 1280x720 root).
                subprocess.run(["xdotool", "mousemove", "639", "567", "click", "1"],
                               env=env, capture_output=True)
                time.sleep(1.5)
            subprocess.run(
                ["import", "-window", "root", str(shots / f"shot{n}.png")],
                env=env, capture_output=True)
            if proc.poll() is not None:
                break
        if proc.poll() is None:
            os.killpg(os.getpgid(proc.pid), signal.SIGTERM)
            try:
                proc.wait(timeout=20)
            except subprocess.TimeoutExpired:
                os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
        log.write(f"\nRUN-EXIT={proc.returncode}\n")

    pass  # This runner never kills an existing display.
    xvfb.terminate()
    print(f"{LABEL}: log={log_path} exit={proc.returncode} shots={len(list(shots.glob('*.png')))}")


if __name__ == "__main__":
    sys.exit(main())
