package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.client.model.inventory.IronChestItemStackRenderer;
import com.progwml6.ironchest.common.block.entity.CopperChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.CrystalChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.DiamondChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.DirtChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.GoldChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.ObsidianChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.SilverChestBlockEntity;
import com.progwml6.ironchest.common.item.IronChestBlockItem;
import com.progwml6.ironchest.common.item.IronChestsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class IronChestsBlocks {

  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IronChests.MOD_ID);
  public static final DeferredRegister<Item> ITEMS = IronChestsItems.ITEMS;

  public static final RegistryObject<IronChestBlock> IRON_CHEST = register(
    "iron_chest", () -> new IronChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getIronChestItemStackRenderer());

  public static final RegistryObject<GoldChestBlock> GOLD_CHEST = register(
    "gold_chest", () -> new GoldChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getGoldChestItemStackRenderer());

  public static final RegistryObject<DiamondChestBlock> DIAMOND_CHEST = register(
    "diamond_chest", () -> new DiamondChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getDiamondChestItemStackRenderer());

  public static final RegistryObject<CopperChestBlock> COPPER_CHEST = register(
    "copper_chest", () -> new CopperChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getCopperChestItemStackRenderer());

  public static final RegistryObject<SilverChestBlock> SILVER_CHEST = register(
    "silver_chest", () -> new SilverChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getSilverChestItemStackRenderer());

  public static final RegistryObject<CrystalChestBlock> CRYSTAL_CHEST = register(
    "crystal_chest", () -> new CrystalChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getCrystalChestItemStackRenderer());

  public static final RegistryObject<ObsidianChestBlock> OBSIDIAN_CHEST = register(
    "obsidian_chest", () -> new ObsidianChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getObsidianChestItemStackRenderer());

  public static final RegistryObject<DirtChestBlock> DIRT_CHEST = register(
    "dirt_chest", () -> new DirtChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    () -> getDirtChestItemStackRenderer());

  //HELPERS

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<Callable<BlockEntityWithoutLevelRenderer>> renderMethod) {
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

  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<BlockEntityWithoutLevelRenderer>> renderMethod) {
    return () -> new IronChestBlockItem(block.get(), new Item.Properties().tab(IronChests.IRONCHESTS_ITEM_GROUP), renderMethod);
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getIronChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new IronChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.IRON_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getGoldChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new GoldChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.GOLD_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getDiamondChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new DiamondChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.DIAMOND_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getCopperChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new CopperChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.COPPER_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getSilverChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new SilverChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.SILVER_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getCrystalChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new CrystalChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.CRYSTAL_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getObsidianChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new ObsidianChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.OBSIDIAN_CHEST.get().defaultBlockState()));
  }

  @OnlyIn(Dist.CLIENT)
  public static Callable<BlockEntityWithoutLevelRenderer> getDirtChestItemStackRenderer() {
    return () -> new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new DirtChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.DIRT_CHEST.get().defaultBlockState()));
  }
}
