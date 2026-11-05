package cn.foggyhillside.ends_delight;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.sounds.SoundEvents;
import vectorwing.farmersdelight.common.registry.ModEffects;

/** Consumption timing, animation and effects split from FoodProperties in modern Minecraft. */
public final class ConsumableList {
    public static final Consumable CHORUS_FRUIT_GRAIN = Consumable.builder().consumeSeconds(0.8F).build();
    public static final Consumable CHORUS_FRUIT_MILK_TEA = Consumable.builder().animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).build();
    public static final Consumable BUBBLE_TEA = Consumable.builder().animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).build();
    public static final Consumable DRAGON_BREATH_SODA = Consumable.builder().animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,60*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE,60*20,0), 1)).build();
    public static final Consumable SHULKER_MEAT = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,4*20,1), 1)).build();
    public static final Consumable SHULKER_MEAT_SLICE = Consumable.builder().consumeSeconds(0.8F).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,2*20,1), 1)).build();
    public static final Consumable STIR_FRIED_SHULKER_MEAT = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,5*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,180*20,0), 1)).build();
    public static final Consumable FRIED_DRAGON_EGG = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,240*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,240*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE,240*20,2), 1)).build();
    public static final Consumable DRAGON_LEG = Consumable.builder().build();
    public static final Consumable SMOKED_DRAGON_LEG = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,15*20,1), 1)).build();
    public static final Consumable CHORUS_FRUIT_POPSICLE = Consumable.builder().build();
    public static final Consumable CHORUS_FRUIT_WINE = Consumable.builder().animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).build();
    public static final Consumable DRAGON_BREATH_AND_CHORUS_SOUP = Consumable.builder().animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,20*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,60*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE,60*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.COMFORT, 180*20, 0), 1.0F)).build();
    public static final Consumable LIQUID_DRAGON_EGG = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,60*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,60*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE,60*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.BLINDNESS,20*20,0), 1)).build();
    public static final Consumable STUFFED_RICE_CAKE = Consumable.builder().build();
    public static final Consumable RAW_ENDERMITE_MEAT = Consumable.builder().build();
    public static final Consumable DRIED_ENDERMITE_MEAT = Consumable.builder().build();
    public static final Consumable RAW_DRAGON_MEAT = Consumable.builder().build();
    public static final Consumable END_BARBECUE_STICK = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,5*20,1), 1)).build();
    public static final Consumable ROASTED_DRAGON_MEAT = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,15*20,1), 1)).build();
    public static final Consumable CHORUS_FLOWER_TEA = Consumable.builder().animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 5*20, 1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20*20, 0), 1)).build();
    public static final Consumable ROASTED_SHULKER_MEAT = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,4*20,1), 1)).build();
    public static final Consumable ROASTED_SHULKER_MEAT_SLICE = Consumable.builder().consumeSeconds(0.8F).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,2*20,1), 1)).build();
    public static final Consumable RAW_DRAGON_MEAT_CUTS = Consumable.builder().consumeSeconds(0.8F).build();
    public static final Consumable ROASTED_DRAGON_MEAT_CUTS = Consumable.builder().consumeSeconds(0.8F).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,5*20,1), 1)).build();
    public static final Consumable DRAGON_MEAT_STEW = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION,75*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,15*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,30*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,300*20,0), 1)).build();
    public static final Consumable CHORUS_SUCCULENT = Consumable.builder().consumeSeconds(0.8F).build();
    public static final Consumable END_MIXED_SALAD = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,6*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SPEED,10*20,0), 1)).build();
    public static final Consumable ASSORTED_SALAD = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,10*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SPEED,25*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,25*20,0), 1)).build();
    public static final Consumable CHORUS_FLOWER_PIE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,5*20,0), 1)).build();
    public static final Consumable STEAMED_DRAGON_EGG = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION,60*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,300*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,60*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH,60*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE,60*20,2), 1)).build();
    public static final Consumable GRILLED_SHULKER = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,5*20,0), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,2*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,300*20,0), 1)).build();
    public static final Consumable ROASTED_DRAGON_STEAK = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,25*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,300*20,0), 1)).build();
    public static final Consumable CHORUS_SAUCE = Consumable.builder().build();
    public static final Consumable CHORUS_COOKIE = Consumable.builder().build();
    public static final Consumable ENDER_CONGEE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,15*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE,45*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST,45*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.COMFORT,180*20,0), 1)).build();
    public static final Consumable DRAGON_LEG_WITH_SAUCE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION,75*20,2), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,15*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,300*20,0), 1)).build();
    public static final Consumable ENDERMAN_GRISTLE = Consumable.builder().consumeSeconds(0.8F).build();
    public static final Consumable RAW_ENDER_SAUSAGE = Consumable.builder().build();
    public static final Consumable ENDER_SAUSAGE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,8*20,1), 1)).build();
    public static final Consumable ENDERMAN_GRISTLE_STEW = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,180*20,0), 1)).build();
    public static final Consumable SHULKER_SOUP = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,5*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.COMFORT,180*20,0), 1)).build();
    public static final Consumable ENDER_NOODLE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,50,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.COMFORT,180*20,0), 1)).build();
    public static final Consumable SHULKER_OMELETTE_MIXTURE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,5*20,1), 1)).build();
    public static final Consumable SHULKER_OMELETTE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LEVITATION,5*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,5*20,0), 1)).build();
    public static final Consumable ENDER_BAMBOO_RICE = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION,15*20,1), 1)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.NOURISHMENT,180*20,0), 1)).build();
}
