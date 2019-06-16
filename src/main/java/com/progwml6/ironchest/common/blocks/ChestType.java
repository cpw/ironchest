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
import com.progwml6.ironchest.common.tileentity.CopperChestTileEntity;
import com.progwml6.ironchest.common.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.tileentity.DiamondChestTileEntity;
import com.progwml6.ironchest.common.tileentity.DirtChestTileEntity;
import com.progwml6.ironchest.common.tileentity.GoldChestTileEntity;
import com.progwml6.ironchest.common.tileentity.IronChestTileEntity;
import com.progwml6.ironchest.common.tileentity.ObsidianChestTileEntity;
import com.progwml6.ironchest.common.tileentity.SilverChestTileEntity;
import com.progwml6.ironchest.common.util.BlockNames;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum ChestType implements IStringSerializable
{
    IRON(54, 9, "iron_chest.png", IronChestTileEntity.class, BlockNames.IRON_CHEST, 184, 222, new ResourceLocation("ironchest", "textures/gui/iron_container.png"), 256, 256),
    GOLD(81, 9, "gold_chest.png", GoldChestTileEntity.class, BlockNames.GOLD_CHEST, 184, 276, new ResourceLocation("ironchest", "textures/gui/gold_container.png"), 256, 276),
    DIAMOND(108, 12, "diamond_chest.png", DiamondChestTileEntity.class, BlockNames.DIAMOND_CHEST, 238, 276, new ResourceLocation("ironchest", "textures/gui/diamond_container.png"), 256, 276),
    COPPER(45, 9, "copper_chest.png", CopperChestTileEntity.class, BlockNames.COPPER_CHEST, 184, 204, new ResourceLocation("ironchest", "textures/gui/copper_container.png"), 256, 256),
    SILVER(72, 9, "silver_chest.png", SilverChestTileEntity.class, BlockNames.SILVER_CHEST, 184, 258, new ResourceLocation("ironchest", "textures/gui/silver_container.png"), 256, 276),
    CRYSTAL(108, 12, "crystal_chest.png", CrystalChestTileEntity.class, BlockNames.CRYSTAL_CHEST, 238, 276, new ResourceLocation("ironchest", "textures/gui/diamond_container.png"), 256, 276),
    OBSIDIAN(108, 12, "obsidian_chest.png", ObsidianChestTileEntity.class, BlockNames.OBSIDIAN_CHEST, 238, 276, new ResourceLocation("ironchest", "textures/gui/diamond_container.png"), 256, 276),
    DIRTCHEST9000(1, 1, "dirt_chest.png", DirtChestTileEntity.class, BlockNames.DIRT_CHEST, 184, 184, new ResourceLocation("ironchest", "textures/gui/dirt_container.png"), 256, 256),
    WOOD(0, 0, "", null, null, 0, 0, null, 0, 0);

    public static final ChestType VALUES[] = values();

    public final String name;

    public final int size;

    public final int rowLength;

    public final String modelTexture;

    public final Class<? extends TileEntity> clazz;

    public final String itemName;

    public final int xSize;

    public final int ySize;

    public final ResourceLocation guiTexture;

    public final int textureXSize;

    public final int textureYSize;

    ChestType(int size, int rowLength, String modelTexture, Class<? extends IronChestTileEntity> clazz, String itemName, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize)
    {
        this.name = this.name().toLowerCase();
        this.size = size;
        this.rowLength = rowLength;
        this.modelTexture = modelTexture;
        this.clazz = clazz;
        this.itemName = itemName;
        this.xSize = xSize;
        this.ySize = ySize;
        this.guiTexture = guiTexture;
        this.textureXSize = textureXSize;
        this.textureYSize = textureYSize;
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

    public static ChestType get(ResourceLocation resourceLocation)
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

    public static BlockState get(ChestType type)
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

    public IronChestTileEntity makeEntity()
    {
        switch (this)
        {
            case IRON:
                return new IronChestTileEntity();
            case GOLD:
                return new GoldChestTileEntity();
            case DIAMOND:
                return new DiamondChestTileEntity();
            case COPPER:
                return new CopperChestTileEntity();
            case SILVER:
                return new SilverChestTileEntity();
            case CRYSTAL:
                return new CrystalChestTileEntity();
            case OBSIDIAN:
                return new ObsidianChestTileEntity();
            case DIRTCHEST9000:
                return new DirtChestTileEntity();
            default:
                return null;
        }
    }
}
