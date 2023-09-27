package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.regular.CopperChestBlock;
import com.progwml6.ironchest.common.block.regular.CrystalChestBlock;
import com.progwml6.ironchest.common.block.regular.DiamondChestBlock;
import com.progwml6.ironchest.common.block.regular.DirtChestBlock;
import com.progwml6.ironchest.common.block.regular.GoldChestBlock;
import com.progwml6.ironchest.common.block.regular.IronChestBlock;
import com.progwml6.ironchest.common.block.regular.ObsidianChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedCopperChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedCrystalChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedDiamondChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedDirtChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedGoldChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedIronChestBlock;
import com.progwml6.ironchest.common.block.trapped.TrappedObsidianChestBlock;
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
    IronChestsTypes.IRON, false);

  public static final RegistryObject<GoldChestBlock> GOLD_CHEST = register(
    "gold_chest", () -> new GoldChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.GOLD, false);

  public static final RegistryObject<DiamondChestBlock> DIAMOND_CHEST = register(
    "diamond_chest", () -> new DiamondChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.DIAMOND, false);

  public static final RegistryObject<CopperChestBlock> COPPER_CHEST = register(
    "copper_chest", () -> new CopperChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.COPPER, false);

  public static final RegistryObject<CrystalChestBlock> CRYSTAL_CHEST = register(
    "crystal_chest", () -> new CrystalChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.CRYSTAL, false);

  public static final RegistryObject<ObsidianChestBlock> OBSIDIAN_CHEST = register(
    "obsidian_chest", () -> new ObsidianChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.OBSIDIAN, false);

  public static final RegistryObject<DirtChestBlock> DIRT_CHEST = register(
    "dirt_chest", () -> new DirtChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.DIRT, false);

  // Trapped Chests
  public static final RegistryObject<TrappedIronChestBlock> TRAPPED_IRON_CHEST = register(
    "trapped_iron_chest", () -> new TrappedIronChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.IRON, true);

  public static final RegistryObject<TrappedGoldChestBlock> TRAPPED_GOLD_CHEST = register(
    "trapped_gold_chest", () -> new TrappedGoldChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.GOLD, true);

  public static final RegistryObject<TrappedDiamondChestBlock> TRAPPED_DIAMOND_CHEST = register(
    "trapped_diamond_chest", () -> new TrappedDiamondChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.DIAMOND, true);

  public static final RegistryObject<TrappedCopperChestBlock> TRAPPED_COPPER_CHEST = register(
    "trapped_copper_chest", () -> new TrappedCopperChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.COPPER, true);

  public static final RegistryObject<TrappedCrystalChestBlock> TRAPPED_CRYSTAL_CHEST = register(
    "trapped_crystal_chest", () -> new TrappedCrystalChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.CRYSTAL, true);

  public static final RegistryObject<TrappedObsidianChestBlock> TRAPPED_OBSIDIAN_CHEST = register(
    "trapped_obsidian_chest", () -> new TrappedObsidianChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.OBSIDIAN, true);

  public static final RegistryObject<TrappedDirtChestBlock> TRAPPED_DIRT_CHEST = register(
    "trapped_dirt_chest", () -> new TrappedDirtChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)),
    IronChestsTypes.DIRT, true);

  //HELPERS

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, IronChestsTypes chestType, boolean trapped) {
    return register(name, sup, block -> item(block, () -> () -> chestType, () -> () -> trapped));
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
    RegistryObject<T> ret = registerNoItem(name, sup);
    ITEMS.register(name, itemCreator.apply(ret));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
    return BLOCKS.register(name, sup);
  }

  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, Supplier<Callable<IronChestsTypes>> chestType, Supplier<Callable<Boolean>> trapped) {
    return () -> new IronChestBlockItem(block.get(), new Item.Properties().tab(IronChests.IRONCHESTS_ITEM_GROUP), chestType, trapped);
  }
}
