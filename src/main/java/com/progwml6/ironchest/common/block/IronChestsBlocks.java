package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.item.IronChestBlockItem;
import com.progwml6.ironchest.common.item.IronChestsItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
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
    IronChestsTypes.IRON);

  public static final RegistryObject<GoldChestBlock> GOLD_CHEST = register(
    "gold_chest", () -> new GoldChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.GOLD);

  public static final RegistryObject<DiamondChestBlock> DIAMOND_CHEST = register(
    "diamond_chest", () -> new DiamondChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.DIAMOND);

  public static final RegistryObject<CopperChestBlock> COPPER_CHEST = register(
    "copper_chest", () -> new CopperChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.COPPER);

  public static final RegistryObject<SilverChestBlock> SILVER_CHEST = register(
    "silver_chest", () -> new SilverChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.SILVER);

  public static final RegistryObject<CrystalChestBlock> CRYSTAL_CHEST = register(
    "crystal_chest", () -> new CrystalChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.CRYSTAL);

  public static final RegistryObject<ObsidianChestBlock> OBSIDIAN_CHEST = register(
    "obsidian_chest", () -> new ObsidianChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.OBSIDIAN);

  public static final RegistryObject<DirtChestBlock> DIRT_CHEST = register(
    "dirt_chest", () -> new DirtChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.DIRT);

  //HELPERS

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, IronChestsTypes chestType) {
    return register(name, sup, block -> item(block, () -> () -> chestType));
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
    RegistryObject<T> ret = registerNoItem(name, sup);
    ITEMS.register(name, itemCreator.apply(ret));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
    return BLOCKS.register(name, sup);
  }

  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, Supplier<Callable<IronChestsTypes>> chestType) {
    return () -> new IronChestBlockItem(block.get(), new Item.Properties().tab(IronChests.IRONCHESTS_ITEM_GROUP), chestType);
  }
}
