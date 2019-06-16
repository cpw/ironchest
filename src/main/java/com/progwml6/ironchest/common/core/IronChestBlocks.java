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

import com.progwml6.ironchest.IronChest;
import com.progwml6.ironchest.client.renderer.IronChestItemStackTileEntityRenderer;
import com.progwml6.ironchest.common.blocks.ChestBlock;
import com.progwml6.ironchest.common.blocks.CopperChestBlock;
import com.progwml6.ironchest.common.blocks.CrystalChestBlock;
import com.progwml6.ironchest.common.blocks.DiamondChestBlock;
import com.progwml6.ironchest.common.blocks.DirtChestBlock;
import com.progwml6.ironchest.common.blocks.GoldChestBlock;
import com.progwml6.ironchest.common.blocks.IronChestBlock;
import com.progwml6.ironchest.common.blocks.ObsidianChestBlock;
import com.progwml6.ironchest.common.blocks.SilverChestBlock;
import com.progwml6.ironchest.common.items.ChestItem;
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
    public static ChestBlock ironChestBlock;

    @ObjectHolder(BlockNames.IRON_CHEST)
    public static Item ironChestItemBlock;

    @ObjectHolder(BlockNames.GOLD_CHEST)
    public static ChestBlock goldChestBlock;

    @ObjectHolder(BlockNames.GOLD_CHEST)
    public static Item goldChestItemBlock;

    @ObjectHolder(BlockNames.DIAMOND_CHEST)
    public static ChestBlock diamondChestBlock;

    @ObjectHolder(BlockNames.DIAMOND_CHEST)
    public static Item diamondChestItemBlock;

    @ObjectHolder(BlockNames.COPPER_CHEST)
    public static ChestBlock copperChestBlock;

    @ObjectHolder(BlockNames.COPPER_CHEST)
    public static Item copperChestItemBlock;

    @ObjectHolder(BlockNames.SILVER_CHEST)
    public static ChestBlock silverChestBlock;

    @ObjectHolder(BlockNames.SILVER_CHEST)
    public static Item silverChestItemBlock;

    @ObjectHolder(BlockNames.CRYSTAL_CHEST)
    public static ChestBlock crystalChestBlock;

    @ObjectHolder(BlockNames.CRYSTAL_CHEST)
    public static Item crystalChestItemBlock;

    @ObjectHolder(BlockNames.OBSIDIAN_CHEST)
    public static ChestBlock obsidianChestBlock;

    @ObjectHolder(BlockNames.OBSIDIAN_CHEST)
    public static Item obsidianChestItemBlock;

    @ObjectHolder(BlockNames.DIRT_CHEST)
    public static ChestBlock dirtChestBlock;

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

            blockRegistry.register(new IronChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new GoldChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new DiamondChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new CopperChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new SilverChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new CrystalChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
            blockRegistry.register(new ObsidianChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 10000.0F)));
            blockRegistry.register(new DirtChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            itemBuilder = (new Properties()).group(IronChestItemGroups.IRON_CHESTS).setTEISR(() -> IronChestItemStackTileEntityRenderer::new);

            itemRegistry.register(new ChestItem(ironChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(goldChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(diamondChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(copperChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(silverChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(crystalChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(obsidianChestBlock, itemBuilder));
            itemRegistry.register(new ChestItem(dirtChestBlock, itemBuilder));
        }
    }
}
