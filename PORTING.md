# End's Delight for Minecraft 26.1.2

This is an independent NeoForge port, based on FoggyHillside/End-s-Delight branch
`1.21.1`, commit `5778ed081dfb18656f04cb1ba91220bf87ea2bba` (MIT).
The upstream commit already fixes the Farmer's Delight pie model parent.
The upstream refs, open PRs and Modrinth versions were checked on 2026-11-05;
no upstream 26.x branch or published 26.x release was found. PR #67 updates Russian translations.

Target: Minecraft **26.1.2**, NeoForge **26.1.2.122**, **Java 25**.
Required runtime dependency: Farmer's Delight **1.3.3-fix7** for 26.1.2.
The standalone jar does not embed Farmer's Delight or the QA mod.

## Build

```sh
./gradlew clean build
```

The default local dependency is
`../../pack-libs/fd/farmersdelight-1.3.3-fix7-26.1.2.jar` relative to this worktree.
On another host, supply its path explicitly:

```sh
./gradlew build -PfarmersDelightJar=/path/to/farmersdelight-1.3.3-fix7-26.1.2.jar
```

Gradle 9.2.1 and NeoGradle 7.1.38 are pinned. The normal build resolves the exact
NeoForge target; it does not borrow another project's compile classpath.
Output: `build/libs/ends_delight-2.6.1+neoforge.26.1.2-port.1.jar`.

## Port behavior

- All 63 item IDs, 8 block IDs and 75 recipe IDs retain the `ends_delight` namespace.
- Nutrition and saturation remain in `FoodList`; all consumption effects, durations,
  probabilities and drink animations are in `ConsumableList`.
- Consumption remainders use `usingConvertsTo` as well as the original crafting remainder.
  FD's custom consumption hooks still handle teleportation, cleansing and extinguishing.
- Item/block registration supplies resource keys. Block items retain block translation keys.
- Knife tiers become `ToolMaterial` with unchanged stats, FD knife mining rules,
  original repair ingredients expressed as tags, and FD knife abilities.
- Teleports use NeoForge `ItemConsumption` events, stack cooldowns and server damage.
  A copy of the consumed gristle stack is kept for the event after consumption.
- End stove keeps its six slots, campfire recipes, particles and heat-source integration.
  Rendering delegates to FD's extracted render-state renderer, which uses the same
  height, orientation, scale and per-slot offsets as the original renderer.
- Recipes use modern ingredient strings; the dough/pasta union uses the new custom
  ingredient discriminator. Loot modifiers load directly from data resources, with
  priorities preserving the old list order. The obsolete global list is removed.
- All original PNGs, translations, block loot tables and worldgen files are preserved.
  Item model definitions are added; succulent models explicitly use cutout rendering.

## Isolated QA

```sh
./gradlew qaJar
python3 qa/audit_content.py
```

`qaJar` produces a separate `-qa.jar`. Install it only in a disposable test instance,
with the standalone jar and the exact FD jar. It adds explicit console commands:

- `edqa run`: registry/recipe inventory, nutrition and effects, consumption remainders,
  crafting/heating matches and assembly, real cutting-board processing, cooking-pot
  ticks, six-slot stove processing, feast servings, entity loot pipelines, knife stats,
  knife damage multiplier, plant bonemeal and harvest.
- `edqa worldgen`: search naturally generated End chunks for succulent in end highlands.
  Test seed: `5778081`. The test does not place the feature to satisfy the check.
- `edqa player`: special consumption using the connected real player. Wait until the
  dimension has fully loaded; use Normal difficulty for the expected gristle damage.
  FakePlayer deliberately has a no-op teleport connection and cannot validate this.
- `edqa showcase`: place a display of the eight blocks, three plant stages and six
  stove items in the isolated End area around x22..37, y100, z0..3.

The client QA waits for normal gameplay before resolving all 63 item models and 144
block states, checks their baked sprites, then opens an actual rendered item catalogue.
Escape closes the catalogue. This QA screen is absent from the standalone jar.

Runtime evidence lives separately under
`/root/home/forks/port-reports/ends-delight-26.1.2-20261105/`.
The disposable server/client directories are `qa/runtime/server` and `qa/runtime/client`;
world name `ends-delight-isolated`, loopback game/RCON ports 25672/25673. QA runtime
files are ignored by Git. Never run the QA commands in a shared or production world.
