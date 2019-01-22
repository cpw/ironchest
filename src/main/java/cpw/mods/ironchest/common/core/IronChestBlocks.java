/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.common.core;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.client.renderer.TileEntityIronChestItemRenderer;
import cpw.mods.ironchest.common.blocks.BlockChest;
import cpw.mods.ironchest.common.blocks.BlockCopperChest;
import cpw.mods.ironchest.common.blocks.BlockCrystalChest;
import cpw.mods.ironchest.common.blocks.BlockDiamondChest;
import cpw.mods.ironchest.common.blocks.BlockDirtChest;
import cpw.mods.ironchest.common.blocks.BlockGoldChest;
import cpw.mods.ironchest.common.blocks.BlockIronChest;
import cpw.mods.ironchest.common.blocks.BlockObsidianChest;
import cpw.mods.ironchest.common.blocks.BlockSilverChest;
import cpw.mods.ironchest.common.items.ItemChest;
import cpw.mods.ironchest.common.util.BlockNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Builder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class IronChestBlocks
{
    public static Builder itemBuilder;

    @ObjectHolder(BlockNames.IRON_CHEST)
    public static BlockChest ironChestBlock;

    @ObjectHolder(BlockNames.IRON_CHEST)
    public static Item ironChestItemBlock;

    @ObjectHolder(BlockNames.GOLD_CHEST)
    public static BlockChest goldChestBlock;

    @ObjectHolder(BlockNames.GOLD_CHEST)
    public static Item goldChestItemBlock;

    @ObjectHolder(BlockNames.DIAMOND_CHEST)
    public static BlockChest diamondChestBlock;

    @ObjectHolder(BlockNames.DIAMOND_CHEST)
    public static Item diamondChestItemBlock;

    @ObjectHolder(BlockNames.COPPER_CHEST)
    public static BlockChest copperChestBlock;

    @ObjectHolder(BlockNames.COPPER_CHEST)
    public static Item copperChestItemBlock;

    @ObjectHolder(BlockNames.SILVER_CHEST)
    public static BlockChest silverChestBlock;

    @ObjectHolder(BlockNames.SILVER_CHEST)
    public static Item silverChestItemBlock;

    @ObjectHolder(BlockNames.CRYSTAL_CHEST)
    public static BlockChest crystalChestBlock;

    @ObjectHolder(BlockNames.CRYSTAL_CHEST)
    public static Item crystalChestItemBlock;

    @ObjectHolder(BlockNames.OBSIDIAN_CHEST)
    public static BlockChest obsidianChestBlock;

    @ObjectHolder(BlockNames.OBSIDIAN_CHEST)
    public static Item obsidianChestItemBlock;

    @ObjectHolder(BlockNames.DIRT_CHEST)
    public static BlockChest dirtChestBlock;

    @ObjectHolder(BlockNames.DIRT_CHEST)
    public static Item dirtChestItemBlock;

    public IronChestBlocks()
    {

    }

    @Mod.EventBusSubscriber(modid = IronChest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event)
        {
            IForgeRegistry<Block> blockRegistry = event.getRegistry();

            blockRegistry.register(new BlockIronChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockGoldChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockDiamondChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockCopperChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockSilverChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockCrystalChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockObsidianChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 10000.0F)));
            blockRegistry.register(new BlockDirtChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            itemBuilder = (new Builder()).group(IronChestCreativeTabs.IRON_CHESTS).setTEISR(() -> TileEntityIronChestItemRenderer::new);

            itemRegistry.register(new ItemChest(ironChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(goldChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(diamondChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(copperChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(silverChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(crystalChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(obsidianChestBlock, itemBuilder));
            itemRegistry.register(new ItemChest(dirtChestBlock, itemBuilder));
        }
    }
}
