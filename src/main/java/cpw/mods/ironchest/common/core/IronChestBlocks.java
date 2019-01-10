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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Builder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class IronChestBlocks
{
    public static Builder itemBuilder;

    public static BlockChest ironChestBlock;

    public static Item ironChestItemBlock;

    public static BlockChest goldChestBlock;

    public static Item goldChestItemBlock;

    public static BlockChest diamondChestBlock;

    public static Item diamondChestItemBlock;

    public static BlockChest copperChestBlock;

    public static Item copperChestItemBlock;

    public static BlockChest silverChestBlock;

    public static Item silverChestItemBlock;

    public static BlockChest crystalChestBlock;

    public static Item crystalChestItemBlock;

    public static BlockChest obsidianChestBlock;

    public static Item obsidianChestItemBlock;

    public static BlockChest dirtChestBlock;

    public static Item dirtChestItemBlock;

    public IronChestBlocks()
    {

    }

    @SubscribeEvent
    public void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> blockRegistry = event.getRegistry();

        blockRegistry.register(ironChestBlock = new BlockIronChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(goldChestBlock = new BlockGoldChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(diamondChestBlock = new BlockDiamondChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(copperChestBlock = new BlockCopperChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(silverChestBlock = new BlockSilverChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(crystalChestBlock = new BlockCrystalChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(obsidianChestBlock = new BlockObsidianChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
        blockRegistry.register(dirtChestBlock = new BlockDirtChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
    }

    @SubscribeEvent
    public void registerItems(final RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> itemRegistry = event.getRegistry();

        itemBuilder = (new Builder()).group(IronChestCreativeTabs.IRON_CHESTS).setTEISR(() -> TileEntityIronChestItemRenderer::new);

        itemRegistry.register(ironChestItemBlock = new ItemChest(ironChestBlock, itemBuilder));
        itemRegistry.register(goldChestItemBlock = new ItemChest(goldChestBlock, itemBuilder));
        itemRegistry.register(diamondChestItemBlock = new ItemChest(diamondChestBlock, itemBuilder));
        itemRegistry.register(copperChestItemBlock = new ItemChest(copperChestBlock, itemBuilder));
        itemRegistry.register(silverChestItemBlock = new ItemChest(silverChestBlock, itemBuilder));
        itemRegistry.register(crystalChestItemBlock = new ItemChest(crystalChestBlock, itemBuilder));
        itemRegistry.register(obsidianChestItemBlock = new ItemChest(obsidianChestBlock, itemBuilder));
        itemRegistry.register(dirtChestItemBlock = new ItemChest(dirtChestBlock, itemBuilder));
    }
}
