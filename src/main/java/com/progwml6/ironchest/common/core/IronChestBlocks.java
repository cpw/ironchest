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
package com.progwml6.ironchest.common.core;

import com.progwml6.ironchest.client.renderer.TileEntityIronChestItemRenderer;
import com.progwml6.ironchest.IronChest;
import com.progwml6.ironchest.common.blocks.BlockChest;
import com.progwml6.ironchest.common.blocks.BlockCopperChest;
import com.progwml6.ironchest.common.blocks.BlockCrystalChest;
import com.progwml6.ironchest.common.blocks.BlockDiamondChest;
import com.progwml6.ironchest.common.blocks.BlockDirtChest;
import com.progwml6.ironchest.common.blocks.BlockGoldChest;
import com.progwml6.ironchest.common.blocks.BlockIronChest;
import com.progwml6.ironchest.common.blocks.BlockObsidianChest;
import com.progwml6.ironchest.common.blocks.BlockSilverChest;
import com.progwml6.ironchest.common.items.ItemChest;
import com.progwml6.ironchest.common.util.BlockNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class IronChestBlocks
{
    public static Properties itemBuilder;

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

            blockRegistry.register(new BlockIronChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockGoldChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockDiamondChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockCopperChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockSilverChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockCrystalChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new BlockObsidianChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 10000.0F)));
            blockRegistry.register(new BlockDirtChest(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            itemBuilder = (new Properties()).group(IronChestCreativeTabs.IRON_CHESTS).setTEISR(() -> TileEntityIronChestItemRenderer::new);

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
