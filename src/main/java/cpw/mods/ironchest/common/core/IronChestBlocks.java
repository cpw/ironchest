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
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IronChestBlocks
{
    //@formatter:off
    public static BlockChest ironChestBlock = new BlockIronChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item ironChestItemBlock = new ItemChest(ironChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));

    public static BlockChest goldChestBlock = new BlockGoldChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item goldChestItemBlock = new ItemChest(goldChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));

    public static BlockChest diamondChestBlock = new BlockDiamondChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item diamondChestItemBlock = new ItemChest(diamondChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));

    public static BlockChest copperChestBlock = new BlockCopperChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item copperChestItemBlock = new ItemChest(copperChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));

    public static BlockChest silverChestBlock = new BlockSilverChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item silverChestItemBlock = new ItemChest(silverChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));

    public static BlockChest crystalChestBlock = new BlockCrystalChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item crystalChestItemBlock = new ItemChest(crystalChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));
    
    public static BlockChest obsidianChestBlock = new BlockObsidianChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item obsidianChestItemBlock = new ItemChest(obsidianChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));

    public static BlockChest dirtChestBlock = new BlockDirtChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F));
    public static Item dirtChestItemBlock = new ItemChest(dirtChestBlock, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS));
    //@formatter:on

    public IronChestBlocks()
    {

    }

    public void registerBlocks()
    {
        // Chest Start
        GameRegistry.findRegistry(Block.class).registerAll(ironChestBlock, goldChestBlock, diamondChestBlock, copperChestBlock, silverChestBlock, crystalChestBlock, obsidianChestBlock, dirtChestBlock);
        // Chest End
    }

    public void registerItems()
    {
        // Chest Start
        GameRegistry.findRegistry(Item.class).registerAll(ironChestItemBlock, goldChestItemBlock, diamondChestItemBlock, copperChestItemBlock, silverChestItemBlock, crystalChestItemBlock, obsidianChestItemBlock, dirtChestItemBlock);
        // Chest End
    }

    /*@SubscribeEvent
    public static void registerBlocks(final Register<Block> event)
    {
        System.out.println("hello from registerBlocks");
        IForgeRegistry<Block> blockRegistry = event.getRegistry();
    
        // Chest Start
        for (IronChestType type : IronChestType.values())
        {
            if (type.itemName != null)
            {
                blockRegistry.register(new BlockIronChest(Block.Builder.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F), type, type.itemName));
            }
        }
        // Chest End
    }
    
    @SubscribeEvent
    public static void registerItems(final Register<Item> event)
    {
        System.out.println("hello from registerItems");
        IForgeRegistry<Item> itemRegistry = event.getRegistry();
    
        // Chest Start
        for (IronChestType type : IronChestType.values())
        {
            if (type != IronChestType.WOOD)
            {
                itemRegistry.register(new ItemChest(type.block, (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS)));
            }
        }
        // Chest End
    }*/
}
