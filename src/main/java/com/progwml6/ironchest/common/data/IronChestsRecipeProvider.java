package com.progwml6.ironchest.common.data;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.item.IronChestsItems;
import com.progwml6.ironchest.common.item.IronChestsUpgradeType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import java.util.Objects;
import java.util.function.Consumer;

public class IronChestsRecipeProvider extends RecipeProvider implements IConditionBuilder {

  public static final Tags.IOptionalNamedTag<Item> INGOTS_COPPER = tag("ingots/copper");
  public static final Tags.IOptionalNamedTag<Item> INGOTS_SILVER = tag("ingots/silver");

  public IronChestsRecipeProvider(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
    this.addChestsRecipes(consumer);
    this.addUpgradesRecipes(consumer);
  }

  private void addChestsRecipes(Consumer<FinishedRecipe> consumer) {
    String folder = "chests/";

    ShapedRecipeBuilder.shaped(IronChestsBlocks.IRON_CHEST.get())
      .define('M', Tags.Items.INGOTS_IRON)
      .define('S', Tags.Items.CHESTS_WOODEN)
      .pattern("MMM")
      .pattern("MSM")
      .pattern("MMM")
      .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
      .save(consumer, location(folder + "vanilla_iron_chest"));

    ShapedRecipeBuilder.shaped(IronChestsBlocks.GOLD_CHEST.get())
      .define('M', Tags.Items.INGOTS_GOLD)
      .define('S', IronChestsBlocks.IRON_CHEST.get())
      .pattern("MMM")
      .pattern("MSM")
      .pattern("MMM")
      .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
      .save(consumer, location(folder + "iron_gold_chest"));

    ShapedRecipeBuilder.shaped(IronChestsBlocks.DIAMOND_CHEST.get())
      .define('M', Tags.Items.GEMS_DIAMOND)
      .define('S', IronChestsBlocks.GOLD_CHEST.get())
      .define('G', Tags.Items.GLASS)
      .pattern("GGG")
      .pattern("MSM")
      .pattern("GGG")
      .unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND))
      .save(consumer, location(folder + "gold_diamond_chest"));

    ShapedRecipeBuilder.shaped(IronChestsBlocks.OBSIDIAN_CHEST.get())
      .define('M', Blocks.OBSIDIAN)
      .define('S', IronChestsBlocks.DIAMOND_CHEST.get())
      .pattern("MMM")
      .pattern("MSM")
      .pattern("MMM")
      .unlockedBy("has_obsidian", has(Blocks.OBSIDIAN))
      .save(consumer, location(folder + "diamond_obsidian_chest"));

    ShapedRecipeBuilder.shaped(IronChestsBlocks.CRYSTAL_CHEST.get())
      .define('G', Tags.Items.GLASS)
      .define('S', IronChestsBlocks.DIAMOND_CHEST.get())
      .pattern("GGG")
      .pattern("GSG")
      .pattern("GGG")
      .unlockedBy("has_glass", has(Tags.Items.GLASS))
      .save(consumer, location(folder + "diamond_crystal_chest"));

    ShapedRecipeBuilder.shaped(IronChestsBlocks.DIRT_CHEST.get())
      .define('M', Ingredient.of(Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL))
      .define('S', Tags.Items.CHESTS_WOODEN)
      .pattern("MMM")
      .pattern("MSM")
      .pattern("MMM")
      .unlockedBy("has_iron_ingot", has(Blocks.DIRT))
      .save(consumer, location(folder + "vanilla_dirt_chest"));

    ResourceLocation copperToIronChest = location(folder + "copper_iron_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsBlocks.IRON_CHEST.get())
        .define('M', Tags.Items.INGOTS_IRON)
        .define('S', IronChestsBlocks.COPPER_CHEST.get())
        .define('G', Tags.Items.GLASS)
        .pattern("MGM")
        .pattern("GSG")
        .pattern("MGM")
        .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))::save)
      .setAdvancement(location("recipes/ironchest/chests/copper_iron_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(copperToIronChest))
          .addCriterion("has_item", has(Tags.Items.INGOTS_IRON))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(copperToIronChest))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, copperToIronChest);

    ResourceLocation copperToSilverChest = location(folder + "copper_silver_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsBlocks.SILVER_CHEST.get())
        .define('M', INGOTS_SILVER)
        .define('S', IronChestsBlocks.COPPER_CHEST.get())
        .pattern("MMM")
        .pattern("MSM")
        .pattern("MMM")
        .unlockedBy("has_item", has(INGOTS_SILVER))::save)
      .setAdvancement(location("recipes/ironchest/chests/copper_silver_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(copperToSilverChest))
          .addCriterion("has_item", has(INGOTS_SILVER))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(copperToSilverChest))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, copperToSilverChest);

    ResourceLocation ironToSilverChest = location(folder + "iron_silver_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsBlocks.SILVER_CHEST.get())
        .define('M', INGOTS_SILVER)
        .define('S', IronChestsBlocks.IRON_CHEST.get())
        .define('G', Tags.Items.GLASS)
        .pattern("MGM")
        .pattern("MSM")
        .pattern("MGM")
        .unlockedBy("has_item", has(INGOTS_SILVER))::save)
      .setAdvancement(location("recipes/ironchest/chests/iron_silver_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(ironToSilverChest))
          .addCriterion("has_item", has(INGOTS_SILVER))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(ironToSilverChest))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, ironToSilverChest);

    ResourceLocation vanillaToCopperChest = location(folder + "vanilla_copper_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsBlocks.COPPER_CHEST.get())
        .define('M', INGOTS_COPPER)
        .define('S', Tags.Items.CHESTS_WOODEN)
        .pattern("MMM")
        .pattern("MSM")
        .pattern("MMM")
        .unlockedBy("has_item", has(INGOTS_COPPER))::save)
      .setAdvancement(location("recipes/ironchest/chests/vanilla_copper_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(vanillaToCopperChest))
          .addCriterion("has_item", has(INGOTS_COPPER))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(vanillaToCopperChest))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, vanillaToCopperChest);

    ResourceLocation silverToGoldChest = location(folder + "silver_gold_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsBlocks.GOLD_CHEST.get())
        .define('M', Tags.Items.INGOTS_GOLD)
        .define('S', IronChestsBlocks.SILVER_CHEST.get())
        .define('G', Tags.Items.GLASS)
        .pattern("MGM")
        .pattern("GSG")
        .pattern("MGM")
        .unlockedBy("has_item", has(INGOTS_SILVER))::save)
      .setAdvancement(location("recipes/ironchest/chests/silver_gold_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(silverToGoldChest))
          .addCriterion("has_item", has(INGOTS_SILVER))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(silverToGoldChest))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, silverToGoldChest);

    ResourceLocation silverToDiamondChest = location(folder + "silver_diamond_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsBlocks.DIAMOND_CHEST.get())
        .define('M', Tags.Items.GEMS_DIAMOND)
        .define('S', IronChestsBlocks.SILVER_CHEST.get())
        .define('G', Tags.Items.GLASS)
        .pattern("GGG")
        .pattern("GSG")
        .pattern("MMM")
        .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))::save)
      .setAdvancement(location("recipes/ironchest/chests/silver_diamond_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(silverToDiamondChest))
          .addCriterion("has_item", has(Tags.Items.GEMS_DIAMOND))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(silverToDiamondChest))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, silverToDiamondChest);
  }

  private void addUpgradesRecipes(Consumer<FinishedRecipe> consumer) {
    String folder = "upgrades/";

    ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_IRON).get())
      .define('M', Tags.Items.INGOTS_IRON)
      .define('P', ItemTags.PLANKS)
      .pattern("MMM")
      .pattern("MPM")
      .pattern("MMM")
      .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
      .save(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_IRON).get(), folder));

    ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.IRON_TO_GOLD).get())
      .define('I', Tags.Items.INGOTS_IRON)
      .define('G', Tags.Items.INGOTS_GOLD)
      .pattern("GGG")
      .pattern("GIG")
      .pattern("GGG")
      .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
      .save(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.IRON_TO_GOLD).get(), folder));

    ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.GOLD_TO_DIAMOND).get())
      .define('M', Tags.Items.GEMS_DIAMOND)
      .define('S', Tags.Items.INGOTS_GOLD)
      .define('G', Tags.Items.GLASS)
      .pattern("GGG")
      .pattern("MSM")
      .pattern("GGG")
      .unlockedBy("has_glass", has(Tags.Items.GLASS))
      .save(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.GOLD_TO_DIAMOND).get(), folder));

    ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_OBSIDIAN).get())
      .define('S', Blocks.OBSIDIAN)
      .define('G', Tags.Items.GLASS)
      .pattern("GGG")
      .pattern("GSG")
      .pattern("GGG")
      .unlockedBy("has_glass", has(Tags.Items.GLASS))
      .save(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_OBSIDIAN).get(), folder));

    ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_CRYSTAL).get())
      .define('M', Blocks.OBSIDIAN)
      .define('G', Tags.Items.GLASS)
      .pattern("MMM")
      .pattern("MGM")
      .pattern("MMM")
      .unlockedBy("has_glass", has(Tags.Items.GLASS))
      .save(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_CRYSTAL).get(), folder));

    ResourceLocation woodToCopperChestUpgradeId = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_COPPER).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_COPPER).get())
        .define('M', INGOTS_COPPER)
        .define('S', ItemTags.PLANKS)
        .pattern("MMM")
        .pattern("MSM")
        .pattern("MMM")
        .unlockedBy("has_item", has(ItemTags.PLANKS))::save)
      .setAdvancement(location("recipes/ironchest/upgrades/wood_to_copper_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(woodToCopperChestUpgradeId))
          .addCriterion("has_item", has(ItemTags.PLANKS))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(woodToCopperChestUpgradeId))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, woodToCopperChestUpgradeId);

    ResourceLocation copperToIronChestUpgrade = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_IRON).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_IRON).get())
        .define('M', Tags.Items.INGOTS_IRON)
        .define('S', INGOTS_COPPER)
        .define('G', Tags.Items.GLASS)
        .pattern("MGM")
        .pattern("GSG")
        .pattern("MGM")
        .unlockedBy("has_item", has(ItemTags.PLANKS))::save)
      .setAdvancement(location("recipes/ironchest/upgrades/copper_to_iron_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(copperToIronChestUpgrade))
          .addCriterion("has_item", has(Tags.Items.GLASS))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(copperToIronChestUpgrade))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, copperToIronChestUpgrade);

    ResourceLocation copperToSilverChestUpgrade = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_SILVER).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(and(not(new TagEmptyCondition("forge:ingots/copper")), not(new TagEmptyCondition("forge:ingots/silver"))))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_SILVER).get())
        .define('M', INGOTS_SILVER)
        .define('S', INGOTS_COPPER)
        .pattern("MMM")
        .pattern("MSM")
        .pattern("MMM")
        .unlockedBy("has_item", has(INGOTS_COPPER))::save)
      .setAdvancement(location("recipes/ironchest/upgrades/copper_to_silver_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(and(not(new TagEmptyCondition("forge:ingots/copper")), not(new TagEmptyCondition("forge:ingots/silver"))))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(copperToSilverChestUpgrade))
          .addCriterion("has_item", has(INGOTS_COPPER))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(copperToSilverChestUpgrade))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, copperToSilverChestUpgrade);

    ResourceLocation silverToGoldChestUpgrade = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.SILVER_TO_GOLD).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
      .addRecipe(ShapedRecipeBuilder.shaped(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.SILVER_TO_GOLD).get())
        .define('M', Tags.Items.INGOTS_GOLD)
        .define('S', INGOTS_SILVER)
        .define('G', Tags.Items.GLASS)
        .pattern("MGM")
        .pattern("GSG")
        .pattern("MGM")
        .unlockedBy("has_item", has(Tags.Items.GLASS))::save)
      .setAdvancement(location("recipes/ironchest/upgrades/silver_to_gold_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.advancement()
          .parent(new ResourceLocation("recipes/root"))
          .rewards(AdvancementRewards.Builder.recipe(silverToGoldChestUpgrade))
          .addCriterion("has_item", has(Tags.Items.GLASS))
          .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(silverToGoldChestUpgrade))
          .requirements(RequirementsStrategy.OR))
      ).build(consumer, silverToGoldChestUpgrade);
  }

  protected static ResourceLocation prefix(ItemLike item, String prefix) {
    ResourceLocation loc = Objects.requireNonNull(item.asItem().getRegistryName());
    return location(prefix + loc.getPath());
  }

  private static ResourceLocation location(String id) {
    return new ResourceLocation(IronChests.MODID, id);
  }

  private static Tags.IOptionalNamedTag<Item> tag(String name) {
    return ItemTags.createOptional(new ResourceLocation("forge", name));
  }
}
