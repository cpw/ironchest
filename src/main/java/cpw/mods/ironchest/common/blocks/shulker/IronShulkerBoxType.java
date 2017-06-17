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
package cpw.mods.ironchest.common.blocks.shulker;

import cpw.mods.ironchest.common.gui.shulker.slot.ValidatingShulkerBoxSlot;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityCopperShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityCrystalShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityDiamondShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityGoldShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityObsidianShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntitySilverShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;

public enum IronShulkerBoxType implements IStringSerializable
{
    //@formatter:off
    IRON(54, 9, true, "_iron.png", TileEntityIronShulkerBox.class, 184, 202),
    GOLD(81, 9, true, "_gold.png", TileEntityGoldShulkerBox.class, 184, 256),
    DIAMOND(108, 12, true, "_diamond.png", TileEntityDiamondShulkerBox.class, 184, 256),
    COPPER(45, 9, false, "_copper.png", TileEntityCopperShulkerBox.class, 184, 184),
    SILVER(72, 9, false, "_silver.png", TileEntitySilverShulkerBox.class, 184, 238),
    CRYSTAL(108, 12, true, "_crystal.png", TileEntityCrystalShulkerBox.class, 238, 256),
    OBSIDIAN(108, 12, false, "_obsidian.png", TileEntityObsidianShulkerBox.class, 238, 256),
    VANILLA(0, 0, false, "", null, 0, 0);
    //@formatter:on

    public static final IronShulkerBoxType VALUES[] = values();

    public final String name;

    public final int size;

    public final int rowLength;

    public final boolean tieredShulkerBox;

    public final String modelTexture;

    public final Class<? extends TileEntityIronShulkerBox> clazz;

    public final int xSize;

    public final int ySize;

    private String breakTexture;

    //@formatter:off
    IronShulkerBoxType(int size, int rowLength, boolean tieredShulkerBox, String modelTexture, Class<? extends TileEntityIronShulkerBox> clazz, int xSize, int ySize)
    //@formatter:on
    {
        this.name = this.name().toLowerCase();
        this.size = size;
        this.rowLength = rowLength;
        this.tieredShulkerBox = tieredShulkerBox;
        this.modelTexture = modelTexture;
        this.clazz = clazz;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public String getBreakTexture()
    {
        if (this.breakTexture == null)
        {
            switch (this)
            {
            case OBSIDIAN:
            {
                this.breakTexture = "minecraft:blocks/obsidian";
                break;
            }
            case VANILLA:
            {
                this.breakTexture = "minecraft:blocks/planks_oak";
                break;
            }
            default:
            {
                this.breakTexture = "ironchest:blocks/" + this.getName() + "break";
            }
            }
        }

        return this.breakTexture;
    }

    public int getRowCount()
    {
        return this.size / this.rowLength;
    }

    public boolean isTransparent()
    {
        return this == CRYSTAL;
    }

    public boolean isValidForCreativeMode()
    {
        return this != VANILLA;
    }

    public boolean isExplosionResistant()
    {
        return this == OBSIDIAN;
    }

    public Slot makeSlot(IInventory chestInventory, int index, int x, int y)
    {
        return new ValidatingShulkerBoxSlot(chestInventory, index, x, y);
    }

    public TileEntityIronShulkerBox makeEntity(EnumDyeColor colorIn)
    {
        switch (this)
        {
        case IRON:
            return new TileEntityIronShulkerBox(colorIn);
        case GOLD:
            return new TileEntityGoldShulkerBox(colorIn);
        case DIAMOND:
            return new TileEntityDiamondShulkerBox(colorIn);
        case COPPER:
            return new TileEntityCopperShulkerBox(colorIn);
        case SILVER:
            return new TileEntitySilverShulkerBox(colorIn);
        case CRYSTAL:
            return new TileEntityCrystalShulkerBox(colorIn);
        case OBSIDIAN:
            return new TileEntityObsidianShulkerBox(colorIn);
        default:
            return null;
        }
    }
}
