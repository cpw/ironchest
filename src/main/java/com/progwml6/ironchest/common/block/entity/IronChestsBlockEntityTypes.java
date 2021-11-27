package com.progwml6.ironchest.common.block.entity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.regular.entity.CopperChestBlockEntity;
import com.progwml6.ironchest.common.block.regular.entity.CrystalChestBlockEntity;
import com.progwml6.ironchest.common.block.regular.entity.DiamondChestBlockEntity;
import com.progwml6.ironchest.common.block.regular.entity.DirtChestBlockEntity;
import com.progwml6.ironchest.common.block.regular.entity.GoldChestBlockEntity;
import com.progwml6.ironchest.common.block.regular.entity.IronChestBlockEntity;
import com.progwml6.ironchest.common.block.regular.entity.ObsidianChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedCopperChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedCrystalChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedDiamondChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedDirtChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedGoldChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedIronChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedObsidianChestBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IronChestsBlockEntityTypes {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, IronChests.MOD_ID);

  public static final RegistryObject<BlockEntityType<IronChestBlockEntity>> IRON_CHEST = BLOCK_ENTITIES.register(
    "iron_chest", () -> BlockEntityType.Builder.of(IronChestBlockEntity::new, IronChestsBlocks.IRON_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<GoldChestBlockEntity>> GOLD_CHEST = BLOCK_ENTITIES.register(
    "gold_chest", () -> BlockEntityType.Builder.of(GoldChestBlockEntity::new, IronChestsBlocks.GOLD_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<DiamondChestBlockEntity>> DIAMOND_CHEST = BLOCK_ENTITIES.register(
    "diamond_chest", () -> BlockEntityType.Builder.of(DiamondChestBlockEntity::new, IronChestsBlocks.DIAMOND_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<CopperChestBlockEntity>> COPPER_CHEST = BLOCK_ENTITIES.register(
    "copper_chest", () -> BlockEntityType.Builder.of(CopperChestBlockEntity::new, IronChestsBlocks.COPPER_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<CrystalChestBlockEntity>> CRYSTAL_CHEST = BLOCK_ENTITIES.register(
    "crystal_chest", () -> BlockEntityType.Builder.of(CrystalChestBlockEntity::new, IronChestsBlocks.CRYSTAL_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<ObsidianChestBlockEntity>> OBSIDIAN_CHEST = BLOCK_ENTITIES.register(
    "obsidian_chest", () -> BlockEntityType.Builder.of(ObsidianChestBlockEntity::new, IronChestsBlocks.OBSIDIAN_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<DirtChestBlockEntity>> DIRT_CHEST = BLOCK_ENTITIES.register(
    "dirt_chest", () -> BlockEntityType.Builder.of(DirtChestBlockEntity::new, IronChestsBlocks.DIRT_CHEST.get()).build(null));

  // Trapped Chests

  public static final RegistryObject<BlockEntityType<TrappedIronChestBlockEntity>> TRAPPED_IRON_CHEST = BLOCK_ENTITIES.register(
    "trapped_iron_chest", () -> BlockEntityType.Builder.of(TrappedIronChestBlockEntity::new, IronChestsBlocks.TRAPPED_IRON_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<TrappedGoldChestBlockEntity>> TRAPPED_GOLD_CHEST = BLOCK_ENTITIES.register(
    "trapped_gold_chest", () -> BlockEntityType.Builder.of(TrappedGoldChestBlockEntity::new, IronChestsBlocks.TRAPPED_GOLD_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<TrappedDiamondChestBlockEntity>> TRAPPED_DIAMOND_CHEST = BLOCK_ENTITIES.register(
    "trapped_diamond_chest", () -> BlockEntityType.Builder.of(TrappedDiamondChestBlockEntity::new, IronChestsBlocks.TRAPPED_DIAMOND_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<TrappedCopperChestBlockEntity>> TRAPPED_COPPER_CHEST = BLOCK_ENTITIES.register(
    "trapped_copper_chest", () -> BlockEntityType.Builder.of(TrappedCopperChestBlockEntity::new, IronChestsBlocks.TRAPPED_COPPER_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<TrappedCrystalChestBlockEntity>> TRAPPED_CRYSTAL_CHEST = BLOCK_ENTITIES.register(
    "trapped_crystal_chest", () -> BlockEntityType.Builder.of(TrappedCrystalChestBlockEntity::new, IronChestsBlocks.TRAPPED_CRYSTAL_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<TrappedObsidianChestBlockEntity>> TRAPPED_OBSIDIAN_CHEST = BLOCK_ENTITIES.register(
    "trapped_obsidian_chest", () -> BlockEntityType.Builder.of(TrappedObsidianChestBlockEntity::new, IronChestsBlocks.TRAPPED_OBSIDIAN_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<TrappedDirtChestBlockEntity>> TRAPPED_DIRT_CHEST = BLOCK_ENTITIES.register(
    "trapped_dirt_chest", () -> BlockEntityType.Builder.of(TrappedDirtChestBlockEntity::new, IronChestsBlocks.TRAPPED_DIRT_CHEST.get()).build(null));
}
