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
package com.progwml6.ironchest.common.blocks;

import com.progwml6.ironchest.common.core.IronChestBlocks;
import com.progwml6.ironchest.common.gui.slot.ValidatingChestSlot;
import com.progwml6.ironchest.common.util.BlockNames;
import com.progwml6.ironchest.common.tileentity.TileEntityCopperChest;
import com.progwml6.ironchest.common.tileentity.TileEntityCrystalChest;
import com.progwml6.ironchest.common.tileentity.TileEntityDiamondChest;
import com.progwml6.ironchest.common.tileentity.TileEntityDirtChest;
import com.progwml6.ironchest.common.tileentity.TileEntityGoldChest;
import com.progwml6.ironchest.common.tileentity.TileEntityIronChest;
import com.progwml6.ironchest.common.tileentity.TileEntityObsidianChest;
import com.progwml6.ironchest.common.tileentity.TileEntitySilverChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum IronChestType implements IStringSerializable
{
    //@formatter:off
    IRON(54, 9, true, "iron_chest.png", TileEntityIronChest.class, 184, 202, BlockNames.IRON_CHEST),
    GOLD(81, 9, true, "gold_chest.png", TileEntityGoldChest.class, 184, 256, BlockNames.GOLD_CHEST),
    DIAMOND(108, 12, true, "diamond_chest.png", TileEntityDiamondChest.class, 184, 256, BlockNames.DIAMOND_CHEST),
    COPPER(45, 9, false, "copper_chest.png", TileEntityCopperChest.class, 184, 184, BlockNames.COPPER_CHEST),
    SILVER(72, 9, false, "silver_chest.png", TileEntitySilverChest.class, 184, 238, BlockNames.SILVER_CHEST),
    CRYSTAL(108, 12, true, "crystal_chest.png", TileEntityCrystalChest.class, 238, 256, BlockNames.CRYSTAL_CHEST),
    OBSIDIAN(108, 12, false, "obsidian_chest.png", TileEntityObsidianChest.class, 238, 256, BlockNames.OBSIDIAN_CHEST),
    DIRTCHEST9000(1, 1, false, "dirt_chest.png", TileEntityDirtChest.class, 184, 184, BlockNames.DIRT_CHEST),
    WOOD(0, 0, false, "", null, 0, 0, null);
    //@formatter:on

    public final String name;

    public final int size;

    public final int rowLength;

    public final boolean tieredChest;

    public final ResourceLocation modelTexture;

    public final Class<? extends TileEntity> clazz;

    public final int xSize;

    public final int ySize;

    public final String itemName;

    IronChestType(int size, int rowLength, boolean tieredChest, String modelTexture, Class<? extends TileEntityIronChest> clazz, int xSize, int ySize,
            String itemName)
    {
        this.name = this.name().toLowerCase();
        this.size = size;
        this.rowLength = rowLength;
        this.tieredChest = tieredChest;
        this.modelTexture = new ResourceLocation("ironchest", "textures/model/" + modelTexture);
        this.clazz = clazz;
        this.xSize = xSize;
        this.ySize = ySize;
        this.itemName = itemName;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public int getRowCount()
    {
        return this.size / this.rowLength;
    }

    public boolean isTransparent()
    {
        return this == CRYSTAL;
    }

    public Slot makeSlot(IInventory chestInventory, int index, int x, int y)
    {
        return new ValidatingChestSlot(chestInventory, index, x, y, this);
    }

    @SuppressWarnings("deprecation")
    private static final Item DIRT_ITEM = Item.getItemFromBlock(Blocks.DIRT);

    public boolean acceptsStack(ItemStack itemstack)
    {
        if (this == DIRTCHEST9000)
        {
            return itemstack.isEmpty() || itemstack.getItem() == DIRT_ITEM;
        }

        return true;
    }

    public static IronChestType get(ResourceLocation resourceLocation)
    {
        switch (resourceLocation.toString())
        {
            case BlockNames.IRON_CHEST:
                return IRON;
            case BlockNames.GOLD_CHEST:
                return GOLD;
            case BlockNames.DIAMOND_CHEST:
                return DIAMOND;
            case BlockNames.COPPER_CHEST:
                return COPPER;
            case BlockNames.SILVER_CHEST:
                return SILVER;
            case BlockNames.CRYSTAL_CHEST:
                return CRYSTAL;
            case BlockNames.OBSIDIAN_CHEST:
                return OBSIDIAN;
            case BlockNames.DIRT_CHEST:
                return DIRTCHEST9000;
            default:
                return WOOD;
        }
    }

    public static IBlockState get(IronChestType type)
    {
        switch (type)
        {
            case IRON:
                return IronChestBlocks.ironChestBlock.getDefaultState();
            case GOLD:
                return IronChestBlocks.goldChestBlock.getDefaultState();
            case DIAMOND:
                return IronChestBlocks.diamondChestBlock.getDefaultState();
            case COPPER:
                return IronChestBlocks.copperChestBlock.getDefaultState();
            case SILVER:
                return IronChestBlocks.silverChestBlock.getDefaultState();
            case CRYSTAL:
                return IronChestBlocks.crystalChestBlock.getDefaultState();
            case OBSIDIAN:
                return IronChestBlocks.obsidianChestBlock.getDefaultState();
            case DIRTCHEST9000:
                return IronChestBlocks.dirtChestBlock.getDefaultState();
            default:
                return null;
        }
    }

    public TileEntityIronChest makeEntity()
    {
        switch (this)
        {
            case IRON:
                return new TileEntityIronChest();
            case GOLD:
                return new TileEntityGoldChest();
            case DIAMOND:
                return new TileEntityDiamondChest();
            case COPPER:
                return new TileEntityCopperChest();
            case SILVER:
                return new TileEntitySilverChest();
            case CRYSTAL:
                return new TileEntityCrystalChest();
            case OBSIDIAN:
                return new TileEntityObsidianChest();
            case DIRTCHEST9000:
                return new TileEntityDirtChest();
            default:
                return null;
        }
    }
}
