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
package com.progwml6.ironchest.client.renderer;

import com.progwml6.ironchest.common.blocks.ChestBlock;
import com.progwml6.ironchest.common.blocks.ChestType;
import com.progwml6.ironchest.common.tileentity.CopperChestTileEntity;
import com.progwml6.ironchest.common.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.tileentity.DiamondChestTileEntity;
import com.progwml6.ironchest.common.tileentity.DirtChestTileEntity;
import com.progwml6.ironchest.common.tileentity.GoldChestTileEntity;
import com.progwml6.ironchest.common.tileentity.IronChestTileEntity;
import com.progwml6.ironchest.common.tileentity.ObsidianChestTileEntity;
import com.progwml6.ironchest.common.tileentity.SilverChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IronChestItemStackTileEntityRenderer extends ItemStackTileEntityRenderer
{
    private static final IronChestTileEntity IRON_CHEST = new IronChestTileEntity();

    private static final GoldChestTileEntity GOLD_CHEST = new GoldChestTileEntity();

    private static final DiamondChestTileEntity DIAMOND_CHEST = new DiamondChestTileEntity();

    private static final CopperChestTileEntity COPPER_CHEST = new CopperChestTileEntity();

    private static final SilverChestTileEntity SILVER_CHEST = new SilverChestTileEntity();

    private static final CrystalChestTileEntity CRYSTAL_CHEST = new CrystalChestTileEntity();

    private static final ObsidianChestTileEntity OBSIDIAN_CHEST = new ObsidianChestTileEntity();

    private static final DirtChestTileEntity DIRT_CHEST = new DirtChestTileEntity();

    private static final IronChestTileEntity[] CHESTS = { IRON_CHEST, GOLD_CHEST, DIAMOND_CHEST, COPPER_CHEST, SILVER_CHEST, CRYSTAL_CHEST, OBSIDIAN_CHEST, DIRT_CHEST };

    public static IronChestItemStackTileEntityRenderer instance = new IronChestItemStackTileEntityRenderer();

    @Override
    public void renderByItem(ItemStack itemStackIn)
    {
        Item item = itemStackIn.getItem();

        if (Block.getBlockFromItem(item) instanceof ChestBlock)
        {
            ChestType typeOut = ChestBlock.getTypeFromItem(item);
            if (typeOut == null)
            {
                TileEntityRendererDispatcher.instance.renderAsItem(IRON_CHEST);
            }
            else
            {
                TileEntityRendererDispatcher.instance.renderAsItem(CHESTS[typeOut.ordinal()]);
            }
        }
        else
        {
            super.renderByItem(itemStackIn);
        }
    }
}
