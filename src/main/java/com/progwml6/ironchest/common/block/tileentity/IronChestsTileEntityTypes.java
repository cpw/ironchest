package com.progwml6.ironchest.common.block.tileentity;

import com.google.common.collect.Sets;
import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IronChestsTileEntityTypes {

  public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, IronChests.MODID);

  public static final RegistryObject<BlockEntityType<IronChestTileEntity>> IRON_CHEST = TILE_ENTITIES.register(
          "iron_chest", () -> new BlockEntityType<>(IronChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.IRON_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<GoldChestTileEntity>> GOLD_CHEST = TILE_ENTITIES.register(
          "gold_chest", () -> new BlockEntityType<>(GoldChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.GOLD_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<DiamondChestTileEntity>> DIAMOND_CHEST = TILE_ENTITIES.register(
          "diamond_chest", () -> new BlockEntityType<>(DiamondChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.DIAMOND_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<CopperChestTileEntity>> COPPER_CHEST = TILE_ENTITIES.register(
          "copper_chest", () -> new BlockEntityType<>(CopperChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.COPPER_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<SilverChestTileEntity>> SILVER_CHEST = TILE_ENTITIES.register(
          "silver_chest", () -> new BlockEntityType<>(SilverChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.SILVER_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<CrystalChestTileEntity>> CRYSTAL_CHEST = TILE_ENTITIES.register(
          "crystal_chest", () -> new BlockEntityType<>(CrystalChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.CRYSTAL_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<ObsidianChestTileEntity>> OBSIDIAN_CHEST = TILE_ENTITIES.register(
          "obsidian_chest", () -> new BlockEntityType<>(ObsidianChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.OBSIDIAN_CHEST.get()), null));

  public static final RegistryObject<BlockEntityType<DirtChestTileEntity>> DIRT_CHEST = TILE_ENTITIES.register(
          "dirt_chest", () -> new BlockEntityType<>(DirtChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.DIRT_CHEST.get()), null));
}
