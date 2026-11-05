package cn.foggyhillside.ends_delight.registry;

import cn.foggyhillside.ends_delight.EndsDelight;
import cn.foggyhillside.ends_delight.FoodList;
import cn.foggyhillside.ends_delight.ConsumableList;
import cn.foggyhillside.ends_delight.item.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EndsDelight.MODID);

    private static Item.Properties properties(net.minecraft.resources.Identifier id) {
        return new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, id));
    }

    //BlockItem
    public static final DeferredItem<BlockItem> CHORUS_FRUIT_CRATE = ITEMS.register("chorus_fruit_crate",
            id -> new BlockItem(ModBlocks.CHORUS_FRUIT_CRATE.get(), properties(id).useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> END_STOVE = ITEMS.register("end_stove",
            id -> new BlockItem(ModBlocks.END_STOVE.get(), properties(id).useBlockDescriptionPrefix()));
    //knife
    public static final DeferredItem<Item> DRAGON_EGG_SHELL_KNIFE = ITEMS.register("dragon_egg_shell_knife",
            id -> new KnifeItem(ModMaterials.DRAGON_EGG_SHELL, properties(id).tool(ModMaterials.DRAGON_EGG_SHELL, vectorwing.farmersdelight.common.tag.ModTags.Blocks.MINEABLE_WITH_KNIFE, 0.5F, -2.0F, 0.0F)));
    public static final DeferredItem<Item> PURPUR_KNIFE = ITEMS.register("purpur_knife",
            id -> new KnifeItem(ModMaterials.PURPUR, properties(id).tool(ModMaterials.PURPUR, vectorwing.farmersdelight.common.tag.ModTags.Blocks.MINEABLE_WITH_KNIFE, 0.5F, -2.0F, 0.0F)));
    public static final DeferredItem<Item> END_STONE_KNIFE = ITEMS.register("end_stone_knife",
            id -> new KnifeItem(ModMaterials.END_STONE, properties(id).tool(ModMaterials.END_STONE, vectorwing.farmersdelight.common.tag.ModTags.Blocks.MINEABLE_WITH_KNIFE, 0.5F, -2.0F, 0.0F)));
    public static final DeferredItem<Item> DRAGON_TOOTH_KNIFE = ITEMS.register("dragon_tooth_knife",
            id -> new KnifeItem(ModMaterials.DRAGON_TOOTH, properties(id).tool(ModMaterials.DRAGON_TOOTH, vectorwing.farmersdelight.common.tag.ModTags.Blocks.MINEABLE_WITH_KNIFE, 0.5F, -2.0F, 0.0F)));
    //Ingredients And Other Foods
    public static final DeferredItem<Item> ENDER_PEARL_GRAIN = ITEMS.register("ender_pearl_grain",
            id -> new Item(properties(id)));
    public static final DeferredItem<Item> CHORUS_FRUIT_GRAIN = ITEMS.register("chorus_fruit_grain",
            id -> new Item(properties(id).food(FoodList.CHORUS_FRUIT_GRAIN, ConsumableList.CHORUS_FRUIT_GRAIN)));
    public static final DeferredItem<BlockItem> CHORUS_SUCCULENT = ITEMS.register("chorus_succulent",
            id -> new BlockItem(ModBlocks.CHORUS_SUCCULENT.get(), properties(id).useBlockDescriptionPrefix().food(FoodList.CHORUS_SUCCULENT, ConsumableList.CHORUS_SUCCULENT)));
    public static final DeferredItem<Item> DRIED_CHORUS_FLOWER = ITEMS.register("dried_chorus_flower",
            id -> new Item(properties(id)));
    public static final DeferredItem<Item> DRAGON_TOOTH = ITEMS.register("dragon_tooth",
            id -> new Item(properties(id)));
    public static final DeferredItem<Item> NON_HATCHABLE_DRAGON_EGG = ITEMS.register("non_hatchable_dragon_egg",
            id -> new Item(properties(id).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> HALF_DRAGON_EGG_SHELL = ITEMS.register("half_dragon_egg_shell",
            id -> new Item(properties(id).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LIQUID_DRAGON_EGG = ITEMS.register("liquid_dragon_egg",
            id -> new ConsumableItem(properties(id).food(FoodList.LIQUID_DRAGON_EGG, ConsumableList.LIQUID_DRAGON_EGG), true));
    public static final DeferredItem<Item> FRIED_DRAGON_EGG = ITEMS.register("fried_dragon_egg",
            id -> new ConsumableItem(properties(id).food(FoodList.FRIED_DRAGON_EGG, ConsumableList.FRIED_DRAGON_EGG), true));
    public static final DeferredItem<Item> SHULKER_MEAT = ITEMS.register("shulker_meat",
            id -> new ConsumableItem(properties(id).food(FoodList.SHULKER_MEAT, ConsumableList.SHULKER_MEAT), true));
    public static final DeferredItem<Item> SHULKER_MEAT_SLICE = ITEMS.register("shulker_meat_slice",
            id -> new ConsumableItem(properties(id).food(FoodList.SHULKER_MEAT_SLICE, ConsumableList.SHULKER_MEAT_SLICE), true));
    public static final DeferredItem<Item> ROASTED_SHULKER_MEAT = ITEMS.register("roasted_shulker_meat",
            id -> new ConsumableItem(properties(id).food(FoodList.ROASTED_SHULKER_MEAT, ConsumableList.ROASTED_SHULKER_MEAT), true));
    public static final DeferredItem<Item> ROASTED_SHULKER_MEAT_SLICE = ITEMS.register("roasted_shulker_meat_slice",
            id -> new ConsumableItem(properties(id).food(FoodList.ROASTED_SHULKER_MEAT_SLICE, ConsumableList.ROASTED_SHULKER_MEAT_SLICE), true));
    public static final DeferredItem<Item> DRAGON_LEG = ITEMS.register("dragon_leg",
            id -> new Item(properties(id).food(FoodList.DRAGON_LEG, ConsumableList.DRAGON_LEG)));
    public static final DeferredItem<Item> SMOKED_DRAGON_LEG = ITEMS.register("smoked_dragon_leg",
            id -> new ConsumableItem(properties(id).food(FoodList.SMOKED_DRAGON_LEG, ConsumableList.SMOKED_DRAGON_LEG), true));
    public static final DeferredItem<Item> RAW_DRAGON_MEAT = ITEMS.register("raw_dragon_meat",
            id -> new Item(properties(id).food(FoodList.RAW_DRAGON_MEAT, ConsumableList.RAW_DRAGON_MEAT)));
    public static final DeferredItem<Item> ROASTED_DRAGON_MEAT = ITEMS.register("roasted_dragon_meat",
            id -> new ConsumableItem(properties(id).food(FoodList.ROASTED_DRAGON_MEAT, ConsumableList.ROASTED_DRAGON_MEAT), true));
    public static final DeferredItem<Item> RAW_DRAGON_MEAT_CUTS = ITEMS.register("raw_dragon_meat_cuts",
            id -> new Item(properties(id).food(FoodList.RAW_DRAGON_MEAT_CUTS, ConsumableList.RAW_DRAGON_MEAT_CUTS)));
    public static final DeferredItem<Item> ROASTED_DRAGON_MEAT_CUTS = ITEMS.register("roasted_dragon_meat_cuts",
            id -> new ConsumableItem(properties(id).food(FoodList.ROASTED_DRAGON_MEAT_CUTS, ConsumableList.ROASTED_DRAGON_MEAT_CUTS), true));
    public static final DeferredItem<Item> RAW_ENDERMITE_MEAT = ITEMS.register("raw_ender_mite_meat",
            id -> new Item(properties(id).food(FoodList.RAW_ENDERMITE_MEAT, ConsumableList.RAW_ENDERMITE_MEAT)));
    public static final DeferredItem<Item> DRIED_ENDERMITE_MEAT = ITEMS.register("dried_endermite_meat",
            id -> new Item(properties(id).food(FoodList.DRIED_ENDERMITE_MEAT, ConsumableList.DRIED_ENDERMITE_MEAT)));
    public static final DeferredItem<Item> ENDERMAN_GRISTLE = ITEMS.register("enderman_gristle",
            id -> new EndermanGristleItem(properties(id).food(FoodList.ENDERMAN_GRISTLE, ConsumableList.ENDERMAN_GRISTLE), 0.3F, false));
    public static final DeferredItem<Item> CHORUS_SAUCE = ITEMS.register("chorus_sauce",
            id -> new ConsumableItem(properties(id).food(FoodList.CHORUS_SAUCE, ConsumableList.CHORUS_SAUCE).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(64)));
    public static final DeferredItem<Item> SHULKER_OMELETTE_MIXTURE = ITEMS.register("shulker_omelette_mixture",
            id -> new ConsumableItem(properties(id).food(FoodList.SHULKER_OMELETTE_MIXTURE, ConsumableList.SHULKER_OMELETTE_MIXTURE),true));
    public static final DeferredItem<Item> SHULKER_OMELETTE = ITEMS.register("shulker_omelette",
            id -> new ConsumableItem(properties(id).food(FoodList.SHULKER_OMELETTE, ConsumableList.SHULKER_OMELETTE),true));
    public static final DeferredItem<Item> RAW_ENDER_SAUSAGE = ITEMS.register("raw_ender_sausage",
            id -> new EndermanGristleItem(properties(id).food(FoodList.RAW_ENDER_SAUSAGE, ConsumableList.RAW_ENDER_SAUSAGE), 0.3F, false));
    public static final DeferredItem<Item> ENDER_SAUSAGE = ITEMS.register("ender_sausage",
            id -> new EndermanGristleItem(properties(id).food(FoodList.ENDER_SAUSAGE, ConsumableList.ENDER_SAUSAGE), 0.2F, true, true));
    public static final DeferredItem<Item> ENDER_BAMBOO_RICE = ITEMS.register("ender_bamboo_rice",
            id -> new ConsumableItem(properties(id).food(FoodList.ENDER_BAMBOO_RICE, ConsumableList.ENDER_BAMBOO_RICE), true));
    public static final DeferredItem<Item> STUFFED_RICE_CAKE = ITEMS.register("stuffed_rice_cake",
            id -> new ConsumableChorusItem(properties(id).food(FoodList.STUFFED_RICE_CAKE, ConsumableList.STUFFED_RICE_CAKE), true));
    public static final DeferredItem<Item> CHORUS_FLOWER_PIE = ITEMS.register("chorus_flower_pie",
            id -> new ConsumableChorusItem(properties(id).food(FoodList.CHORUS_FLOWER_PIE, ConsumableList.CHORUS_FLOWER_PIE), true, true));
    public static final DeferredItem<Item> CHORUS_COOKIE = ITEMS.register("chorus_cookie",
            id -> new ConsumableChorusItem(properties(id).food(FoodList.CHORUS_COOKIE, ConsumableList.CHORUS_COOKIE), true));
    public static final DeferredItem<Item> CHORUS_FRUIT_POPSICLE = ITEMS.register("chorus_fruit_popsicle",
            id -> new ChorusFruitPopsicleItem(properties(id).food(FoodList.CHORUS_FRUIT_POPSICLE, ConsumableList.CHORUS_FRUIT_POPSICLE).stacksTo(16)));
    //Drink
    public static final DeferredItem<Item> CHORUS_FRUIT_WINE = ITEMS.register("chorus_fruit_wine",
            id -> new ChorusFruitWineItem(properties(id).food(FoodList.CHORUS_FRUIT_WINE, ConsumableList.CHORUS_FRUIT_WINE).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final DeferredItem<Item> CHORUS_FRUIT_MILK_TEA = ITEMS.register("chorus_fruit_milk_tea",
            id -> new BubbleTeaItem(properties(id).food(FoodList.CHORUS_FRUIT_MILK_TEA, ConsumableList.CHORUS_FRUIT_MILK_TEA).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE)));
    public static final DeferredItem<Item> BUBBLE_TEA = ITEMS.register("bubble_tea",
            id -> new BubbleTeaItem(properties(id).food(FoodList.BUBBLE_TEA, ConsumableList.BUBBLE_TEA).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE)));
    public static final DeferredItem<Item> DRAGON_BREATH_SODA = ITEMS.register("dragon_breath_soda",
            id -> new DrinkableItem(properties(id).food(FoodList.DRAGON_BREATH_SODA, ConsumableList.DRAGON_BREATH_SODA).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE), true));
    public static final DeferredItem<Item> CHORUS_FLOWER_TEA = ITEMS.register("chorus_flower_tea",
            id -> new ChorusFlowerTeaItem(properties(id).food(FoodList.CHORUS_FLOWER_TEA, ConsumableList.CHORUS_FLOWER_TEA).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE), true, true));
    //Pie
    public static final DeferredItem<BlockItem> CHORUS_FRUIT_PIE = ITEMS.register("chorus_fruit_pie",
            id -> new BlockItem(ModBlocks.CHORUS_FRUIT_PIE.get(), properties(id).useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> CHORUS_FRUIT_PIE_SLICE = ITEMS.register("chorus_fruit_pie_slice",
            id -> new ConsumableChorusItem(properties(id).food(FoodValues.PIE_SLICE, FoodValues.consumableFor(FoodValues.PIE_SLICE)), true, true));
    //Congee
    public static final DeferredItem<Item> ENDER_CONGEE = ITEMS.register("ender_congee",
            id -> new ConsumableItem(properties(id).food(FoodList.ENDER_CONGEE, ConsumableList.ENDER_CONGEE).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
    //Soup
    public static final DeferredItem<Item> DRAGON_BREATH_AND_CHORUS_SOUP = ITEMS.register("dragon_breath_and_chorus_soup",
            id -> new DrinkableChorusItem(properties(id).food(FoodList.DRAGON_BREATH_AND_CHORUS_SOUP, ConsumableList.DRAGON_BREATH_AND_CHORUS_SOUP).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true, true));
    public static final DeferredItem<Item> SHULKER_SOUP = ITEMS.register("shulker_soup",
            id -> new ConsumableItem(properties(id).food(FoodList.SHULKER_SOUP, ConsumableList.SHULKER_SOUP).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
    //ConsumableItem
    public static final DeferredItem<Item> ENDER_NOODLE = ITEMS.register("ender_noodle",
            id -> new ConsumableItem(properties(id).food(FoodList.ENDER_NOODLE, ConsumableList.ENDER_NOODLE).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
    public static final DeferredItem<Item> ENDERMAN_GRISTLE_STEW = ITEMS.register("enderman_gristle_stew",
            id -> new EndermanGristleItem(properties(id).food(FoodList.ENDERMAN_GRISTLE_STEW, ConsumableList.ENDERMAN_GRISTLE_STEW).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), 0.2F, true, true));
    public static final DeferredItem<Item> STIR_FRIED_SHULKER_MEAT = ITEMS.register("stir_fried_shulker_meat",
            id -> new ConsumableItem(properties(id).food(FoodList.STIR_FRIED_SHULKER_MEAT, ConsumableList.STIR_FRIED_SHULKER_MEAT).stacksTo(16).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL), true));
    public static final DeferredItem<Item> ROASTED_DRAGON_STEAK = ITEMS.register("roasted_dragon_steak",
            id -> new ConsumableItem(properties(id).food(FoodList.ROASTED_DRAGON_STEAK, ConsumableList.ROASTED_DRAGON_STEAK).stacksTo(16).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL), true));
    public static final DeferredItem<Item> END_MIXED_SALAD = ITEMS.register("end_mixed_salad",
            id -> new ConsumableItem(properties(id).food(FoodList.END_MIXED_SALAD, ConsumableList.END_MIXED_SALAD).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
    public static final DeferredItem<Item> ASSORTED_SALAD = ITEMS.register("assorted_salad",
            id -> new ConsumableItem(properties(id).food(FoodList.ASSORTED_SALAD, ConsumableList.ASSORTED_SALAD).craftRemainder(Items.SHULKER_SHELL).usingConvertsTo(Items.SHULKER_SHELL).stacksTo(16), true));
    //BarbecueStick
    public static final DeferredItem<Item> END_BARBECUE_STICK = ITEMS.register("end_barbecue_stick",
            id -> new ConsumableItem(properties(id).food(FoodList.END_BARBECUE_STICK, ConsumableList.END_BARBECUE_STICK), true));
    //Feast
    public static final DeferredItem<BlockItem> DRAGON_LEG_BLOCK = ITEMS.register("dragon_leg_with_sauce_block",
            id -> new BlockItem(ModBlocks.DRAGON_LEG_BLOCK.get(), properties(id).useBlockDescriptionPrefix().stacksTo(1)));
    public static final DeferredItem<Item> DRAGON_LEG_WITH_SAUCE = ITEMS.register("dragon_leg_with_sauce",
            id -> new ConsumableItem(properties(id).food(FoodList.DRAGON_LEG_WITH_SAUCE, ConsumableList.DRAGON_LEG_WITH_SAUCE).stacksTo(16).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL), true));
    public static final DeferredItem<BlockItem> STEAMED_DRAGON_EGG_BLOCK = ITEMS.register("steamed_dragon_egg_block",
            id -> new BlockItem(ModBlocks.STEAMED_DRAGON_EGG_BLOCK.get(), properties(id).useBlockDescriptionPrefix().stacksTo(1)));
    public static final DeferredItem<Item> STEAMED_DRAGON_EGG = ITEMS.register("steamed_dragon_egg",
            id -> new ConsumableItem(properties(id).food(FoodList.STEAMED_DRAGON_EGG, ConsumableList.STEAMED_DRAGON_EGG).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
    public static final DeferredItem<BlockItem> DRAGON_MEAT_STEW_BLOCK = ITEMS.register("dragon_meat_stew_block",
            id -> new BlockItem(ModBlocks.DRAGON_MEAT_STEW_BLOCK.get(), properties(id).useBlockDescriptionPrefix().stacksTo(1)));
    public static final DeferredItem<Item> DRAGON_MEAT_STEW = ITEMS.register("dragon_meat_stew",
            id -> new ConsumableItem(properties(id).food(FoodList.DRAGON_MEAT_STEW, ConsumableList.DRAGON_MEAT_STEW).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
    public static final DeferredItem<BlockItem> GRILLED_SHULKER_BLOCK = ITEMS.register("grilled_shulker_block",
            id -> new BlockItem(ModBlocks.GRILLED_SHULKER_BLOCK.get(), properties(id).useBlockDescriptionPrefix().stacksTo(1)));
    public static final DeferredItem<Item> GRILLED_SHULKER = ITEMS.register("grilled_shulker",
            id -> new ConsumableItem(properties(id).food(FoodList.GRILLED_SHULKER, ConsumableList.GRILLED_SHULKER).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL).stacksTo(16), true));
}
