package com.progwml6.ironchest.common.data;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.item.IronChestsItems;
import com.progwml6.ironchest.common.item.IronChestsUpgradeType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import java.util.Objects;
import java.util.function.Consumer;

public class IronChestsRecipeProvider extends RecipeProvider implements IConditionBuilder {

  public IronChestsRecipeProvider(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    this.addChestsRecipes(consumer);
    this.addUpgradesRecipes(consumer);
  }

  private void addChestsRecipes(Consumer<IFinishedRecipe> consumer) {
    String folder = "chests/";

    ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.IRON_CHEST.get())
      .key('M', Tags.Items.INGOTS_IRON)
      .key('S', Tags.Items.CHESTS_WOODEN)
      .patternLine("MMM")
      .patternLine("MSM")
      .patternLine("MMM")
      .addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON))
      .build(consumer, location(folder + "vanilla_iron_chest"));

    ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.GOLD_CHEST.get())
      .key('M', Tags.Items.INGOTS_GOLD)
      .key('S', IronChestsBlocks.IRON_CHEST.get())
      .patternLine("MMM")
      .patternLine("MSM")
      .patternLine("MMM")
      .addCriterion("has_gold_ingot", hasItem(Tags.Items.INGOTS_GOLD))
      .build(consumer, location(folder + "iron_gold_chest"));

    ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.DIAMOND_CHEST.get())
      .key('M', Tags.Items.GEMS_DIAMOND)
      .key('S', IronChestsBlocks.GOLD_CHEST.get())
      .key('G', Tags.Items.GLASS)
      .patternLine("GGG")
      .patternLine("MSM")
      .patternLine("GGG")
      .addCriterion("has_diamonds", hasItem(Tags.Items.GEMS_DIAMOND))
      .build(consumer, location(folder + "gold_diamond_chest"));

    ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.OBSIDIAN_CHEST.get())
      .key('M', Blocks.OBSIDIAN)
      .key('S', IronChestsBlocks.DIAMOND_CHEST.get())
      .patternLine("MMM")
      .patternLine("MSM")
      .patternLine("MMM")
      .addCriterion("has_obsidian", hasItem(Blocks.OBSIDIAN))
      .build(consumer, location(folder + "diamond_obsidian_chest"));

    ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.CRYSTAL_CHEST.get())
      .key('G', Tags.Items.GLASS)
      .key('S', IronChestsBlocks.DIAMOND_CHEST.get())
      .patternLine("GGG")
      .patternLine("GSG")
      .patternLine("GGG")
      .addCriterion("has_glass", hasItem(Tags.Items.GLASS))
      .build(consumer, location(folder + "diamond_crystal_chest"));

    ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.DIRT_CHEST.get())
      .key('M', Ingredient.fromItems(Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL))
      .key('S', Tags.Items.CHESTS_WOODEN)
      .patternLine("MMM")
      .patternLine("MSM")
      .patternLine("MMM")
      .addCriterion("has_iron_ingot", hasItem(Blocks.DIRT))
      .build(consumer, location(folder + "vanilla_dirt_chest"));

    ResourceLocation copperToIronChest = location(folder + "copper_iron_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.IRON_CHEST.get())
        .key('M', Tags.Items.INGOTS_IRON)
        .key('S', IronChestsBlocks.COPPER_CHEST.get())
        .key('G', Tags.Items.GLASS)
        .patternLine("MGM")
        .patternLine("GSG")
        .patternLine("MGM")
        .addCriterion("has_item", hasItem(Tags.Items.INGOTS_IRON))::build)
      .setAdvancement(location("recipes/ironchest/chests/copper_iron_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(copperToIronChest))
          .withCriterion("has_item", hasItem(Tags.Items.INGOTS_IRON))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(copperToIronChest))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, copperToIronChest);

    ResourceLocation copperToSilverChest = location(folder + "copper_silver_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.SILVER_CHEST.get())
        .key('M', ItemTags.makeWrapperTag("forge:ingots/silver"))
        .key('S', IronChestsBlocks.COPPER_CHEST.get())
        .patternLine("MMM")
        .patternLine("MSM")
        .patternLine("MMM")
        .addCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/silver")))::build)
      .setAdvancement(location("recipes/ironchest/chests/copper_silver_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(copperToSilverChest))
          .withCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/silver")))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(copperToSilverChest))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, copperToSilverChest);

    ResourceLocation ironToSilverChest = location(folder + "iron_silver_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.SILVER_CHEST.get())
        .key('M', ItemTags.makeWrapperTag("forge:ingots/silver"))
        .key('S', IronChestsBlocks.IRON_CHEST.get())
        .key('G', Tags.Items.GLASS)
        .patternLine("GGG")
        .patternLine("MSM")
        .patternLine("GGG")
        .addCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/silver")))::build)
      .setAdvancement(location("recipes/ironchest/chests/iron_silver_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(ironToSilverChest))
          .withCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/silver")))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(ironToSilverChest))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, ironToSilverChest);

    ResourceLocation vanillaToCopperChest = location(folder + "vanilla_copper_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.COPPER_CHEST.get())
        .key('M', ItemTags.makeWrapperTag("forge:ingots/copper"))
        .key('S', Tags.Items.CHESTS_WOODEN)
        .patternLine("MMM")
        .patternLine("MSM")
        .patternLine("MMM")
        .addCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/copper")))::build)
      .setAdvancement(location("recipes/ironchest/chests/vanilla_copper_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(vanillaToCopperChest))
          .withCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/copper")))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(vanillaToCopperChest))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, vanillaToCopperChest);

    ResourceLocation silverToGoldChest = location(folder + "silver_gold_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.GOLD_CHEST.get())
        .key('M', Tags.Items.INGOTS_GOLD)
        .key('S', IronChestsBlocks.SILVER_CHEST.get())
        .key('G', Tags.Items.GLASS)
        .patternLine("MGM")
        .patternLine("GSG")
        .patternLine("MGM")
        .addCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/silver")))::build)
      .setAdvancement(location("recipes/ironchest/chests/silver_gold_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(silverToGoldChest))
          .withCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/silver")))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(silverToGoldChest))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, silverToGoldChest);

    ResourceLocation silverToDiamondChest = location(folder + "silver_diamond_chest");
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsBlocks.DIAMOND_CHEST.get())
        .key('M', Tags.Items.GEMS_DIAMOND)
        .key('S', IronChestsBlocks.SILVER_CHEST.get())
        .key('G', Tags.Items.GLASS)
        .patternLine("GGG")
        .patternLine("GSG")
        .patternLine("MMM")
        .addCriterion("has_item", hasItem(Tags.Items.GEMS_DIAMOND))::build)
      .setAdvancement(location("recipes/ironchest/chests/silver_diamond_chest"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(silverToDiamondChest))
          .withCriterion("has_item", hasItem(Tags.Items.GEMS_DIAMOND))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(silverToDiamondChest))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, silverToDiamondChest);
  }

  private void addUpgradesRecipes(Consumer<IFinishedRecipe> consumer) {
    String folder = "upgrades/";

    ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_IRON).get())
      .key('M', Tags.Items.INGOTS_IRON)
      .key('P', ItemTags.PLANKS)
      .patternLine("MMM")
      .patternLine("MPM")
      .patternLine("MMM")
      .addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON))
      .build(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_IRON).get(), folder));

    ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.IRON_TO_GOLD).get())
      .key('I', Tags.Items.INGOTS_IRON)
      .key('G', Tags.Items.INGOTS_GOLD)
      .patternLine("GGG")
      .patternLine("GIG")
      .patternLine("GGG")
      .addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON))
      .build(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.IRON_TO_GOLD).get(), folder));

    ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.GOLD_TO_DIAMOND).get())
      .key('M', Tags.Items.GEMS_DIAMOND)
      .key('S', Tags.Items.INGOTS_GOLD)
      .key('G', Tags.Items.GLASS)
      .patternLine("GGG")
      .patternLine("MSM")
      .patternLine("GGG")
      .addCriterion("has_glass", hasItem(Tags.Items.GLASS))
      .build(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.GOLD_TO_DIAMOND).get(), folder));

    ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_OBSIDIAN).get())
      .key('S', Blocks.OBSIDIAN)
      .key('G', Tags.Items.GLASS)
      .patternLine("GGG")
      .patternLine("GSG")
      .patternLine("GGG")
      .addCriterion("has_glass", hasItem(Tags.Items.GLASS))
      .build(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_OBSIDIAN).get(), folder));

    ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_CRYSTAL).get())
      .key('M', Blocks.OBSIDIAN)
      .key('G', Tags.Items.GLASS)
      .patternLine("MMM")
      .patternLine("MGM")
      .patternLine("MMM")
      .addCriterion("has_glass", hasItem(Tags.Items.GLASS))
      .build(consumer, prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.DIAMOND_TO_CRYSTAL).get(), folder));

    ResourceLocation woodToCopperChestUpgradeId = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_COPPER).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.WOOD_TO_COPPER).get())
        .key('M', ItemTags.makeWrapperTag("forge:ingots/copper"))
        .key('S', ItemTags.PLANKS)
        .patternLine("MMM")
        .patternLine("MSM")
        .patternLine("MMM")
        .addCriterion("has_item", hasItem(ItemTags.PLANKS))::build)
      .setAdvancement(location("recipes/ironchest/upgrades/wood_to_copper_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(woodToCopperChestUpgradeId))
          .withCriterion("has_item", hasItem(ItemTags.PLANKS))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(woodToCopperChestUpgradeId))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, woodToCopperChestUpgradeId);

    ResourceLocation copperToIronChestUpgrade = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_IRON).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_IRON).get())
        .key('M', Tags.Items.INGOTS_IRON)
        .key('S', ItemTags.makeWrapperTag("forge:ingots/copper"))
        .key('G', Tags.Items.GLASS)
        .patternLine("MGM")
        .patternLine("GSG")
        .patternLine("MGM")
        .addCriterion("has_item", hasItem(ItemTags.PLANKS))::build)
      .setAdvancement(location("recipes/ironchest/upgrades/copper_to_iron_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/copper")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(copperToIronChestUpgrade))
          .withCriterion("has_item", hasItem(Tags.Items.GLASS))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(copperToIronChestUpgrade))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, copperToIronChestUpgrade);

    ResourceLocation copperToSilverChestUpgrade = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_SILVER).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(and(not(new TagEmptyCondition("forge:ingots/copper")), not(new TagEmptyCondition("forge:ingots/silver"))))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.COPPER_TO_SILVER).get())
        .key('M', ItemTags.makeWrapperTag("forge:ingots/silver"))
        .key('S', ItemTags.makeWrapperTag("forge:ingots/copper"))
        .patternLine("MMM")
        .patternLine("MSM")
        .patternLine("MMM")
        .addCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/copper")))::build)
      .setAdvancement(location("recipes/ironchest/upgrades/copper_to_silver_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(and(not(new TagEmptyCondition("forge:ingots/copper")), not(new TagEmptyCondition("forge:ingots/silver"))))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(copperToSilverChestUpgrade))
          .withCriterion("has_item", hasItem(ItemTags.makeWrapperTag("forge:ingots/copper")))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(copperToSilverChestUpgrade))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, copperToSilverChestUpgrade);

    ResourceLocation silverToGoldChestUpgrade = prefix(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.SILVER_TO_GOLD).get(), folder);
    ConditionalRecipe.builder()
      .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
      .addRecipe(ShapedRecipeBuilder.shapedRecipe(IronChestsItems.UPGRADES.get(IronChestsUpgradeType.SILVER_TO_GOLD).get())
        .key('M', Tags.Items.INGOTS_GOLD)
        .key('S', ItemTags.makeWrapperTag("forge:ingots/copper"))
        .key('G', Tags.Items.GLASS)
        .patternLine("MGM")
        .patternLine("GSG")
        .patternLine("MGM")
        .addCriterion("has_item", hasItem(Tags.Items.GLASS))::build)
      .setAdvancement(location("recipes/ironchest/upgrades/silver_to_gold_chest_upgrade"), ConditionalAdvancement.builder()
        .addCondition(not(new TagEmptyCondition("forge:ingots/silver")))
        .addAdvancement(Advancement.Builder.builder()
          .withParentId(new ResourceLocation("recipes/root"))
          .withRewards(AdvancementRewards.Builder.recipe(silverToGoldChestUpgrade))
          .withCriterion("has_item", hasItem(Tags.Items.GLASS))
          .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(silverToGoldChestUpgrade))
          .withRequirementsStrategy(IRequirementsStrategy.OR))
      ).build(consumer, silverToGoldChestUpgrade);
  }

  protected static ResourceLocation prefix(IItemProvider item, String prefix) {
    ResourceLocation loc = Objects.requireNonNull(item.asItem().getRegistryName());
    return location(prefix + loc.getPath());
  }

  private static ResourceLocation location(String id) {
    return new ResourceLocation(IronChests.MODID, id);
  }
}
