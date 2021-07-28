package com.progwml6.ironchest.common.block.entity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
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

  public static final RegistryObject<BlockEntityType<SilverChestBlockEntity>> SILVER_CHEST = BLOCK_ENTITIES.register(
    "silver_chest", () -> BlockEntityType.Builder.of(SilverChestBlockEntity::new, IronChestsBlocks.SILVER_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<CrystalChestBlockEntity>> CRYSTAL_CHEST = BLOCK_ENTITIES.register(
    "crystal_chest", () -> BlockEntityType.Builder.of(CrystalChestBlockEntity::new, IronChestsBlocks.CRYSTAL_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<ObsidianChestBlockEntity>> OBSIDIAN_CHEST = BLOCK_ENTITIES.register(
    "obsidian_chest", () -> BlockEntityType.Builder.of(ObsidianChestBlockEntity::new, IronChestsBlocks.OBSIDIAN_CHEST.get()).build(null));

  public static final RegistryObject<BlockEntityType<DirtChestBlockEntity>> DIRT_CHEST = BLOCK_ENTITIES.register(
    "dirt_chest", () -> BlockEntityType.Builder.of(DirtChestBlockEntity::new, IronChestsBlocks.DIRT_CHEST.get()).build(null));
}
