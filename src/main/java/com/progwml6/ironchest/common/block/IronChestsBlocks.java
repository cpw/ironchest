package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.client.tileentity.IronChestItemStackRenderer;
import com.progwml6.ironchest.common.block.tileentity.CopperChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.DiamondChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.DirtChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.GoldChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.ObsidianChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.SilverChestTileEntity;
import com.progwml6.ironchest.common.item.IronChestsItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class IronChestsBlocks {

  public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, IronChests.MODID);
  public static final DeferredRegister<Item> ITEMS = IronChestsItems.ITEMS;

  public static final RegistryObject<IronChestBlock> IRON_CHEST = register(
          "iron_chest", () -> new IronChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> ironChestRenderer());

  public static final RegistryObject<GoldChestBlock> GOLD_CHEST = register(
          "gold_chest", () -> new GoldChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> goldChestRenderer());

  public static final RegistryObject<DiamondChestBlock> DIAMOND_CHEST = register(
          "diamond_chest", () -> new DiamondChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> diamondChestRenderer());

  public static final RegistryObject<CopperChestBlock> COPPER_CHEST = register(
          "copper_chest", () -> new CopperChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> copperChestRenderer());

  public static final RegistryObject<SilverChestBlock> SILVER_CHEST = register(
          "silver_chest", () -> new SilverChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> silverChestRenderer());

  public static final RegistryObject<CrystalChestBlock> CRYSTAL_CHEST = register(
          "crystal_chest", () -> new CrystalChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> crystalChestRenderer());

  public static final RegistryObject<ObsidianChestBlock> OBSIDIAN_CHEST = register(
          "obsidian_chest", () -> new ObsidianChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 10000.0F)),
          () -> obsidianChestRenderer());

  public static final RegistryObject<DirtChestBlock> DIRT_CHEST = register(
          "dirt_chest", () -> new DirtChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)),
          () -> dirtChestRenderer());

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
    return register(name, sup, block -> item(block, renderMethod));
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
    RegistryObject<T> ret = registerNoItem(name, sup);
    ITEMS.register(name, itemCreator.apply(ret));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
    return BLOCKS.register(name, sup);
  }

  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
    return () -> new BlockItem(block.get(), new Item.Properties().group(IronChests.IRONCHESTS_ITEM_GROUP).setISTER(renderMethod));
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> ironChestRenderer() {
    return () -> new IronChestItemStackRenderer(IronChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> goldChestRenderer() {
    return () -> new IronChestItemStackRenderer(GoldChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> diamondChestRenderer() {
    return () -> new IronChestItemStackRenderer(DiamondChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> copperChestRenderer() {
    return () -> new IronChestItemStackRenderer(CopperChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> silverChestRenderer() {
    return () -> new IronChestItemStackRenderer(SilverChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> crystalChestRenderer() {
    return () -> new IronChestItemStackRenderer(CrystalChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> obsidianChestRenderer() {
    return () -> new IronChestItemStackRenderer(ObsidianChestTileEntity::new);
  }

  @OnlyIn(Dist.CLIENT)
  private static Callable<ItemStackTileEntityRenderer> dirtChestRenderer() {
    return () -> new IronChestItemStackRenderer(DirtChestTileEntity::new);
  }
}
