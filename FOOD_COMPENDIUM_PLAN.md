# Planned Food Compendium integration — not applied

Read-only inspection on 2026-11-05 found a jar-merge project, rather than a Gradle
multi-project build. `Food-Compendium/build.sh` reads `modules/<version>.list`, calls
`.port-tools/merge-modjars.py`, then adds `NOTICE` and per-mod license files.
`modules/0.1.1.list` includes Farmer's Delight fix7, Fruits Delight port.2 and Ocean's
Delight port.1, retaining the individual mod IDs.

A separate authorized integration task should:

1. Create an isolated Food Compendium checkout/worktree and a new module manifest
   version, leaving existing manifests and release jars intact.
2. Stage the validated standalone End's Delight jar as a pinned module artifact with
   its SHA256 and source commit. Add that path to the new manifest; do not include
   `-qa.jar` or another Farmer's Delight jar.
3. Add `LICENSES/ends_delight.txt` containing the unchanged MIT text and update NOTICE
   with FoggyHillside attribution, upstream HEAD and the port source/commit.
4. Audit merge collisions in tags, item models, loot modifiers and mod metadata.
   Preserve `ends_delight`, `cn.foggyhillside.ends_delight`, all registry IDs, config
   keys and resource paths. Modern loot modifiers must remain separate resources;
   do not reintroduce the removed legacy global loot list.
5. Build a new Compendium jar using its existing merger. Verify exactly one
   `ends_delight` mod entry, one FD fix7 entry and no duplicate classes/resources.
   Do not install the standalone End's Delight jar together with the merged jar.
6. Run this port's QA suite against the merged jar in a separate world and client,
   including cross-module recipe tags, FD pot heat from End stove, entity loot,
   natural End generation and all baked models.
7. Test a copy of the isolated standalone world with the merged jar, verifying that
   registered item/block IDs and saved stove inventories survive the change.
8. Package the new Compendium jar, diff, evidence and checksum for review under that
   task. Do not publish or replace production files without explicit authorization.

No Food Compendium file was changed by this port task.
