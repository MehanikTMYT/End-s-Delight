package cn.foggyhillside.ends_delight.qa;

import cn.foggyhillside.ends_delight.registry.ModItems;
import cn.foggyhillside.ends_delight.registry.ModBlocks;
import cn.foggyhillside.ends_delight.block.ChorusSucculentBlock;
import cn.foggyhillside.ends_delight.block.DragonLegBlock;
import cn.foggyhillside.ends_delight.block.entity.EndStoveBlockEntity;
import com.google.gson.*;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.*;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import vectorwing.farmersdelight.common.block.entity.*;
import vectorwing.farmersdelight.common.crafting.*;
import vectorwing.farmersdelight.common.registry.ModEffects;
import java.nio.file.*;
import java.util.*;

/** Explicit commands only, uses a dedicated test world; not shipped in the release jar. */
@Mod("ends_delight_qa")
public class EndsDelightQa {
    private static int passed, failed;
    private static final Path LOG = Path.of("edqa-results.txt");
    public EndsDelightQa() {
        NeoForge.EVENT_BUS.addListener(this::commands);
    }
    private void commands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("edqa")
            .then(Commands.literal("run").executes(c -> { run(c.getSource().getServer()); return failed == 0 ? 1 : 0; }))
            .then(Commands.literal("showcase").executes(c -> { showcase(c.getSource().getServer().getLevel(Level.END)); return 1; }))
            .then(Commands.literal("player").executes(c -> {
                var players = c.getSource().getServer().getPlayerList().getPlayers();
                if (players.isEmpty()) { log("REAL_PLAYER_TESTS NOT_RUN: connect a client first"); return 0; }
                special(c.getSource().getServer().getLevel(Level.END), players.getFirst()); return 1;
            }))
            .then(Commands.literal("worldgen").executes(c -> { worldgen(c.getSource().getServer()); return 1; })));
    }
    static Identifier id(String s) { return Identifier.parse(s.contains(":") ? s : "ends_delight:" + s); }
    static Item item(String s) { return BuiltInRegistries.ITEM.getValue(id(s)); }
    static void log(String s) {
        System.out.println("[EDQA] " + s);
        try { Files.writeString(LOG, s + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
    static void check(String label, boolean ok, Object detail) {
        if (ok) passed++; else failed++;
        log((ok ? "PASS " : "FAIL ") + label + " :: " + detail);
    }
    static void section(String label, Runnable r) {
        try { r.run(); } catch (Throwable t) { check(label, false, t); t.printStackTrace(); }
    }
    static JsonObject expected() {
        try (var r = new java.io.InputStreamReader(EndsDelightQa.class.getResourceAsStream("/expected.json"))) {
            return JsonParser.parseReader(r).getAsJsonObject();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    static FakePlayer player(ServerLevel level) {
        FakePlayer p = FakePlayerFactory.getMinecraft(level);
        p.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
        p.setPos(0.5, 100, 0.5);
        p.setShiftKeyDown(false);
        p.getInventory().clearContent();
        return p;
    }
    static void floor(ServerLevel level) {
        for (int x = -12; x <= 32; x++) for (int z = -12; z <= 16; z++)
            level.setBlockAndUpdate(new BlockPos(x, 99, z), Blocks.END_STONE.defaultBlockState());
    }
    static void run(MinecraftServer server) {
        passed = failed = 0;
        log("BEGIN target=26.1.2/26.1.2.122 Java=" + System.getProperty("java.version"));
        ServerLevel level = server.getLevel(Level.END);
        floor(level);
        FakePlayer p = player(level);
        var exp = expected();
        section("registries-recipes", () -> {
            for (var x : exp.getAsJsonArray("items")) {
                var key = id(x.getAsString());
                check("item/"+key, BuiltInRegistries.ITEM.containsKey(key) && item(key.toString()) != Items.AIR, item(key.toString()));
            }
            for (var x : exp.getAsJsonArray("blocks")) {
                var key = id(x.getAsString());
                var block = BuiltInRegistries.BLOCK.getValue(key);
                BlockPos pos = new BlockPos(2 + passed % 20, 100, 8);
                check("block/"+key, block != Blocks.AIR && (level.setBlockAndUpdate(pos, block.defaultBlockState()) || level.getBlockState(pos).is(block)), block);
            }
            for (var x : exp.getAsJsonArray("recipes")) {
                var key = ResourceKey.create(Registries.RECIPE, id(x.getAsString()));
                check("recipe/"+x.getAsString(), server.getRecipeManager().byKey(key).isPresent(), key);
            }
        });
        section("food", () -> {
            for (var x : exp.getAsJsonArray("foods")) {
                JsonObject f = x.getAsJsonObject();
                String name = f.get("item").getAsString();
                Item it = item(name);
                ItemStack stack = new ItemStack(it);
                var food = stack.get(DataComponents.FOOD);
                var con = stack.get(DataComponents.CONSUMABLE);
                check("food-components/"+name, food != null && con != null && food.nutrition() == f.get("nutrition").getAsInt()
                    && Math.abs(food.saturation() - f.get("saturation").getAsFloat()) < 0.001F
                    && food.canAlwaysEat() == f.get("always").getAsBoolean()
                    && con.consumeTicks() == f.get("ticks").getAsInt(), food + " " + con);
                p.removeAllEffects(); p.setHealth(p.getMaxHealth()); p.getFoodData().setFoodLevel(0); p.setShiftKeyDown(false);
                p.getInventory().clearContent();
                ItemStack result = stack.finishUsingItem(level, p);
                boolean effects = true;
                for (var effect : f.getAsJsonArray("effects")) {
                    var e = effect.getAsJsonObject();
                    var holder = BuiltInRegistries.MOB_EFFECT.get(id(e.get("id").getAsString())).orElseThrow();
                    var actual = p.getEffect(holder);
                    effects &= actual != null && actual.getDuration() == e.get("duration").getAsInt() && actual.getAmplifier() == e.get("amplifier").getAsInt();
                }
                check("consume-effects/"+name, effects && p.getFoodData().getFoodLevel() == food.nutrition(), p.getActiveEffects()+" hunger="+p.getFoodData().getFoodLevel());
                if (f.has("remainder")) {
                    Item remainder = item(f.get("remainder").getAsString());
                    check("remainder/"+name, result.is(remainder) || p.getInventory().contains(new ItemStack(remainder)), result);
                }
            }
        });
        section("crafting-heating", () -> crafting(level));
        log("Special teleport checks require an actual connected player: edqa player (FakePlayer has a no-op teleport connection).");
        section("cutting", () -> cutting(level, p));
        section("cooking-pot", () -> cooking(level));
        section("stove", () -> stove(level, p));
        section("feasts", () -> feasts(level, p));
        section("loot", () -> loot(level, p));
        section("knife-materials", () -> {
            for (var spec : List.of(new Object[]{ModItems.END_STONE_KNIFE.get(), 200, 4.0F}, new Object[]{ModItems.PURPUR_KNIFE.get(), 200, 4.0F}, new Object[]{ModItems.DRAGON_EGG_SHELL_KNIFE.get(), 1250, 6.0F}, new Object[]{ModItems.DRAGON_TOOTH_KNIFE.get(), 1561, 8.0F})) {
                var stack = new ItemStack((Item)spec[0]);
                check("knife-material/"+stack.getItem(), stack.getMaxDamage() == (int)spec[1] && Math.abs(stack.getDestroySpeed(Blocks.PUMPKIN.defaultBlockState())-(float)spec[2]) < 0.01F, "durability="+stack.getMaxDamage()+" pumpkin-speed="+stack.getDestroySpeed(Blocks.PUMPKIN.defaultBlockState()));
            }
        });
        section("knife-damage", () -> {
            p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.DRAGON_TOOTH_KNIFE.get()));
            var target = EntityType.ENDERMITE.create(level, EntitySpawnReason.COMMAND);
            float before = target.getHealth();
            target.hurtServer(level, level.damageSources().playerAttack(p), 1.0F);
            check("knife-end-mob-multiplier", Math.abs(before - target.getHealth() - 3.5F) < 0.001F, before+" -> "+target.getHealth());
        });
        section("plant", () -> {
            BlockPos pos = new BlockPos(12, 100, 4);
            level.setBlockAndUpdate(pos, ModBlocks.CHORUS_SUCCULENT.get().defaultBlockState());
            var plant = (ChorusSucculentBlock) ModBlocks.CHORUS_SUCCULENT.get();
            plant.performBonemeal(level, level.getRandom(), pos, level.getBlockState(pos));
            check("succulent-bonemeal", level.getBlockState(pos).getValue(ChorusSucculentBlock.SUCCULENT) == 3, level.getBlockState(pos));
            var drops = Block.getDrops(level.getBlockState(pos), level, pos, null);
            check("succulent-harvest", drops.stream().mapToInt(ItemStack::getCount).sum() == 3 && drops.stream().allMatch(s -> s.is(ModItems.CHORUS_SUCCULENT.get())), drops);
        });
        log("SUMMARY passed="+passed+" failed="+failed);
    }
    static ItemStack first(Ingredient ingredient) {
        return ingredient.items().findFirst().map(h -> new ItemStack(h.value())).orElseThrow();
    }
    static void crafting(ServerLevel level) {
        for (var holder : level.getServer().getRecipeManager().getRecipes()) {
            if (!holder.id().identifier().getNamespace().equals("ends_delight")) continue;
            if (holder.value() instanceof CraftingRecipe recipe) {
                var info = recipe.placementInfo();
                int width = recipe instanceof ShapedRecipe shaped ? shaped.getWidth() : 3;
                int height = recipe instanceof ShapedRecipe shaped ? shaped.getHeight() : 3;
                var slots = new ArrayList<ItemStack>(Collections.nCopies(width*height, ItemStack.EMPTY));
                for (int i = 0; i < info.slotsToIngredientIndex().size(); i++) {
                    int index = info.slotsToIngredientIndex().getInt(i);
                    if (index >= 0) slots.set(i, first(info.ingredients().get(index)));
                }
                var input = CraftingInput.of(width, height, slots);
                var result = recipe.assemble(input);
                check("craft-match-assemble/"+holder.id().identifier(), recipe.matches(input, level) && !result.isEmpty(), result);
            } else if (holder.value() instanceof AbstractCookingRecipe recipe) {
                var input = new SingleRecipeInput(first(recipe.placementInfo().ingredients().getFirst()));
                var result = recipe.assemble(input);
                check("heating-match-assemble/"+holder.id().identifier(), recipe.matches(input, level) && !result.isEmpty(), result);
            }
        }
    }
    static void special(ServerLevel level, net.minecraft.server.level.ServerPlayer p) {
        if (p.isChangingDimension() || !p.connection.hasClientLoaded()) {
            log("REAL_PLAYER_TESTS NOT_RUN: wait until the client finishes loading its dimension."); return;
        }
        p.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
        p.invulnerableTime = 0;
        p.setAbsorptionAmount(0);
        p.removeAllEffects(); p.setHealth(20); p.setPos(0.5,100,0.5);
        level.setBlockAndUpdate(new BlockPos(-5,104,-5), Blocks.END_STONE.defaultBlockState());
        boolean teleport = cn.foggyhillside.ends_delight.EndermanGristleTransport.randomTeleport(p,-4.5,109,-4.5,true,0.3F);
        check("gristle-teleport-damage", teleport && Math.abs(p.getHealth()-14) < 0.01F, "pos="+p.position()+" health="+p.getHealth());
        p.setPos(0.5,100,0.5); p.setHealth(20); p.setShiftKeyDown(false);
        var before = p.position();
        new ItemStack(ModItems.BUBBLE_TEA.get()).finishUsingItem(level,p);
        check("bubble-tea-no-shift-no-teleport", p.position().equals(before), p.position());
        p.setShiftKeyDown(true); boolean moved = false;
        for(int i=0;i<32 && !moved;i++) { new ItemStack(ModItems.BUBBLE_TEA.get()).finishUsingItem(level,p); moved = !p.position().equals(before); }
        check("bubble-tea-shift-teleport", moved, p.position());
        p.setShiftKeyDown(false);
        p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,200));
        new ItemStack(ModItems.CHORUS_FLOWER_TEA.get()).finishUsingItem(level,p);
        check("flower-tea-clears-levitation", !p.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION), p.getActiveEffects());
        p.setRemainingFireTicks(100); new ItemStack(ModItems.CHORUS_FRUIT_POPSICLE.get()).finishUsingItem(level,p);
        check("popsicle-extinguishes", !p.isOnFire(), p.getRemainingFireTicks());
        p.removeAllEffects(); p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON,200));
        new ItemStack(ModItems.CHORUS_FRUIT_WINE.get()).finishUsingItem(level,p);
        check("wine-removes-harmful-effect", !p.hasEffect(net.minecraft.world.effect.MobEffects.POISON), p.getActiveEffects());
        p.setPos(0.5,100,0.5); p.removeAllEffects();
    }
    static void cutting(ServerLevel level, FakePlayer p) {
        BlockPos pos = new BlockPos(2, 100, 0);
        level.setBlockAndUpdate(pos, vectorwing.farmersdelight.common.registry.ModBlocks.CUTTING_BOARD.get().defaultBlockState());
        var board = (CuttingBoardBlockEntity)level.getBlockEntity(pos);
        for (var holder : level.getServer().getRecipeManager().getRecipes()) {
            if (!holder.id().identifier().getNamespace().equals("ends_delight") || !(holder.value() instanceof CuttingBoardRecipe recipe)) continue;
            ItemStack input = recipe.getIngredients().getFirst().items().findFirst().map(h -> new ItemStack(h.value())).orElseThrow();
            ItemStack tool = recipe.getTool().items().findFirst().map(h -> new ItemStack(h.value())).orElseThrow();
            level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).forEach(Entity::discard);
            board.clearContent(); board.addItem(input); p.setItemInHand(InteractionHand.MAIN_HAND, tool);
            boolean ok = board.processStoredItemUsingTool(tool, p);
            check("cutting-process/"+holder.id().identifier(), ok && board.isEmpty() && level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).stream().filter(e -> e.getItem().is(recipe.getResultItem().getItem())).mapToInt(e -> e.getItem().getCount()).sum() >= recipe.getResultItem().getCount(), "input="+input+" tool="+tool+" expected="+recipe.getResults());
        }
    }
    static void cooking(ServerLevel level) {
        BlockPos pos = new BlockPos(5, 101, 0);
        level.setBlockAndUpdate(pos.below(), ModBlocks.END_STOVE.get().defaultBlockState().setValue(BlockStateProperties.LIT, true));
        level.setBlockAndUpdate(pos, vectorwing.farmersdelight.common.registry.ModBlocks.COOKING_POT.get().defaultBlockState());
        var pot = (CookingPotBlockEntity)level.getBlockEntity(pos);
        for (var holder : level.getServer().getRecipeManager().getRecipes()) {
            if (!holder.id().identifier().getNamespace().equals("ends_delight") || !(holder.value() instanceof CookingPotRecipe recipe)) continue;
            pot.clearContent();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                var ingredient = recipe.getIngredients().get(i);
                var input = ingredient.items().findFirst().map(h -> new ItemStack(h.value())).orElseThrow();
                pot.getInventory().setStackInSlot(i, input);
            }
            pot.getInventory().setStackInSlot(CookingPotBlockEntity.CONTAINER_SLOT, recipe.getOutputContainer().copyWithCount(16));
            for (int tick = 0; tick < recipe.getCookTime() + 30; tick++) CookingPotBlockEntity.cookingTick(level, pos, level.getBlockState(pos), pot);
            var output = pot.getInventory().getStackInSlot(CookingPotBlockEntity.OUTPUT_SLOT);
            check("cooking-process/"+holder.id().identifier(), output.is(recipe.getResultItem().getItem()) || pot.getMeal().is(recipe.getResultItem().getItem()), "heated="+pot.isHeated()+" output="+output+" meal="+pot.getMeal());
        }
    }
    static void stove(ServerLevel level, FakePlayer p) {
        BlockPos pos = new BlockPos(8, 100, 0);
        level.setBlockAndUpdate(pos, ModBlocks.END_STOVE.get().defaultBlockState().setValue(BlockStateProperties.LIT, true));
        var stove = (EndStoveBlockEntity)level.getBlockEntity(pos);
        check("stove-six-slots", stove.getItems().getSlots() == 6, stove.getItems().getSlots());
        for (int i = 0; i < 6; i++) {
            var input = new ItemStack(ModItems.RAW_DRAGON_MEAT.get());
            var recipe = stove.getCookingRecipe(input).orElseThrow();
            check("stove-insert/"+i, stove.placeFood(p, input, recipe), recipe.id());
        }
        for (int t = 0; t < 650; t++) AbstractStoveBlockEntity.serverTick(level, pos, level.getBlockState(pos), stove);
        int cooked = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).stream().filter(e -> e.getItem().is(ModItems.ROASTED_DRAGON_MEAT.get())).mapToInt(e -> e.getItem().getCount()).sum();
        check("stove-cooks-six", stove.isEmpty() && cooked >= 6, "empty="+stove.isEmpty()+" cooked="+cooked);
    }
    static void feasts(ServerLevel level, FakePlayer p) {
        BlockPos pos = new BlockPos(14, 100, 0);
        for (var block : List.of(ModBlocks.STEAMED_DRAGON_EGG_BLOCK.get(), ModBlocks.DRAGON_MEAT_STEW_BLOCK.get(), ModBlocks.GRILLED_SHULKER_BLOCK.get())) {
            level.setBlockAndUpdate(pos, block.defaultBlockState());
            p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOWL, 8));
            int before = level.getBlockState(pos).getValue(vectorwing.farmersdelight.common.block.FeastBlock.SERVINGS);
            level.getBlockState(pos).useItemOn(p.getMainHandItem(), level, p, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
            check("feast-serving/"+block, level.getBlockState(pos).getValue(vectorwing.farmersdelight.common.block.FeastBlock.SERVINGS) == before-1, p.getMainHandItem());
        }
        var state = ModBlocks.DRAGON_LEG_BLOCK.get().defaultBlockState().setValue(DragonLegBlock.FACING, Direction.NORTH);
        level.setBlockAndUpdate(pos, state);
        ModBlocks.DRAGON_LEG_BLOCK.get().setPlacedBy(level, pos, state, p, new ItemStack(ModItems.DRAGON_LEG_BLOCK.get()));
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOWL, 8));
        state.useItemOn(p.getMainHandItem(), level, p, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        check("dragon-leg-pair-serving", level.getBlockState(pos).getValue(DragonLegBlock.SERVINGS) == 5 && level.getBlockState(pos.north()).getValue(DragonLegBlock.SERVINGS) == 5, level.getBlockState(pos)+" / "+level.getBlockState(pos.north()));
        level.setBlockAndUpdate(pos, ModBlocks.CHORUS_FRUIT_PIE.get().defaultBlockState());
        p.getFoodData().setFoodLevel(0); p.setShiftKeyDown(false);
        level.getBlockState(pos).useWithoutItem(level, p, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        check("pie-bite", level.getBlockState(pos).getValue(vectorwing.farmersdelight.common.block.PieBlock.BITES) == 1, level.getBlockState(pos));
    }
    static void loot(ServerLevel level, FakePlayer p) {
        p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.DRAGON_TOOTH_KNIFE.get()));
        for (var type : List.of(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER, EntityType.ENDER_DRAGON)) {
            var mob = type.create(level, EntitySpawnReason.COMMAND);
            mob.setPos(4, 100, 4);
            List<ItemStack> drops = new ArrayList<>();
            mob.dropFromLootTable(level, level.damageSources().playerAttack(p), true, ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("minecraft", "entities/"+BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath())), drops::add);
            if (type == EntityType.ENDERMAN) {
                for (int attempt = 0; attempt < 64; attempt++) mob.dropFromLootTable(level, level.damageSources().playerAttack(p), true, ResourceKey.create(Registries.LOOT_TABLE, Identifier.parse("minecraft:entities/enderman")), drops::add);
            }
            String expected = type == EntityType.ENDERMAN ? "enderman_gristle" : type == EntityType.ENDERMITE ? "raw_ender_mite_meat" : type == EntityType.SHULKER ? "shulker_meat" : "raw_dragon_meat";
            check("entity-loot/"+type, drops.stream().anyMatch(s -> s.is(item(expected))), drops);
            if (type == EntityType.ENDER_DRAGON) {
                check("dragon-leg-loot", drops.stream().anyMatch(s -> s.is(ModItems.DRAGON_LEG.get())), drops);
                check("dragon-tooth-loot", drops.stream().anyMatch(s -> s.is(ModItems.DRAGON_TOOTH.get())), drops);
            }
            if (type == EntityType.ENDERMITE) {
                mob.setRemainingFireTicks(100); drops.clear();
                mob.dropFromLootTable(level, level.damageSources().playerAttack(p), true, ResourceKey.create(Registries.LOOT_TABLE, Identifier.parse("minecraft:entities/endermite")), drops::add);
                check("burning-endermite-loot", drops.stream().anyMatch(s -> s.is(ModItems.DRIED_ENDERMITE_MEAT.get())), drops);
            }
        }
    }
    static void showcase(ServerLevel level) {
        for(int x=20;x<42;x++) for(int z=-5;z<12;z++) level.setBlockAndUpdate(new BlockPos(x,99,z),Blocks.END_STONE.defaultBlockState());
        var blocks = List.of(ModBlocks.END_STOVE.get(),ModBlocks.CHORUS_FRUIT_CRATE.get(),ModBlocks.CHORUS_FRUIT_PIE.get(),ModBlocks.STEAMED_DRAGON_EGG_BLOCK.get(),ModBlocks.DRAGON_MEAT_STEW_BLOCK.get(),ModBlocks.GRILLED_SHULKER_BLOCK.get());
        for(int i=0;i<blocks.size();i++) level.setBlockAndUpdate(new BlockPos(22+i*3,100,0),blocks.get(i).defaultBlockState());
        var stove = (EndStoveBlockEntity)level.getBlockEntity(new BlockPos(22,100,0));
        var foods = List.of(ModItems.RAW_DRAGON_MEAT.get(),ModItems.SHULKER_MEAT.get(),ModItems.RAW_DRAGON_MEAT_CUTS.get(),ModItems.SHULKER_MEAT_SLICE.get(),ModItems.RAW_ENDERMITE_MEAT.get(),ModItems.SHULKER_OMELETTE_MIXTURE.get());
        for(int i=0;i<foods.size();i++) {
            var stack = new ItemStack(foods.get(i));
            stove.placeFood(null,stack,stove.getCookingRecipe(stack).orElseThrow());
        }
        for(int i=1;i<=3;i++) level.setBlockAndUpdate(new BlockPos(24+i*3,100,3),ModBlocks.CHORUS_SUCCULENT.get().defaultBlockState().setValue(ChorusSucculentBlock.SUCCULENT,i));
        var leg = ModBlocks.DRAGON_LEG_BLOCK.get(); var pos = new BlockPos(36,100,3); var state = leg.defaultBlockState();
        level.setBlockAndUpdate(pos,state);leg.setPlacedBy(level,pos,state,null,new ItemStack(ModItems.DRAGON_LEG_BLOCK.get()));
        log("SHOWCASE End blocks x22..37 y100 z0..3; six stove slots; succulent stages 1..3; whole two-block dragon leg.");
    }
    static void worldgen(MinecraftServer server) {
        ServerLevel level = server.getLevel(Level.END);
        int plants = 0, highlands = 0;
        BlockPos first = null;
        for (int cx = 80; cx < 88 && plants == 0; cx++) for (int cz = 80; cz < 88 && plants == 0; cz++) {
            var chunk = level.getChunk(cx, cz);
            for (int x = 0; x < 16; x++) for (int z = 0; z < 16; z++) {
                int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                var pos = new BlockPos(cx*16+x, y, cz*16+z);
                if (level.getBiome(pos).is(Biomes.END_HIGHLANDS)) highlands++;
                for (int dy = -1; dy <= 1; dy++) if (level.getBlockState(pos.above(dy)).is(ModBlocks.CHORUS_SUCCULENT.get())) { plants++; if (first == null) first = pos.above(dy); }
            }
        }
        check("natural-end-generation", plants > 0 && highlands > 0, "plants="+plants+" highlands-columns="+highlands+" first="+first+" seed=5778081");
    }
}
