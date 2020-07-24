package com.progwml6.ironchest.common.block.tileentity;

import com.google.common.collect.Sets;
import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IronChestsTileEntityTypes {

  public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, IronChests.MODID);

  public static final RegistryObject<TileEntityType<IronChestTileEntity>> IRON_CHEST = TILE_ENTITIES.register(
          "iron_chest", () -> new TileEntityType<>(IronChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.IRON_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<GoldChestTileEntity>> GOLD_CHEST = TILE_ENTITIES.register(
          "gold_chest", () -> new TileEntityType<>(GoldChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.GOLD_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<DiamondChestTileEntity>> DIAMOND_CHEST = TILE_ENTITIES.register(
          "diamond_chest", () -> new TileEntityType<>(DiamondChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.DIAMOND_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<CopperChestTileEntity>> COPPER_CHEST = TILE_ENTITIES.register(
          "copper_chest", () -> new TileEntityType<>(CopperChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.COPPER_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<SilverChestTileEntity>> SILVER_CHEST = TILE_ENTITIES.register(
          "silver_chest", () -> new TileEntityType<>(SilverChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.SILVER_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<CrystalChestTileEntity>> CRYSTAL_CHEST = TILE_ENTITIES.register(
          "crystal_chest", () -> new TileEntityType<>(CrystalChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.CRYSTAL_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<ObsidianChestTileEntity>> OBSIDIAN_CHEST = TILE_ENTITIES.register(
          "obsidian_chest", () -> new TileEntityType<>(ObsidianChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.OBSIDIAN_CHEST.get()), null));

  public static final RegistryObject<TileEntityType<DirtChestTileEntity>> DIRT_CHEST = TILE_ENTITIES.register(
          "dirt_chest", () -> new TileEntityType<>(DirtChestTileEntity::new, Sets.newHashSet(IronChestsBlocks.DIRT_CHEST.get()), null));
}
