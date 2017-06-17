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
package cpw.mods.ironchest.common.blocks.chest;

import cpw.mods.ironchest.common.gui.chest.slot.ValidatingChestSlot;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityCopperChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityCrystalChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityDiamondChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityDirtChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityGoldChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityObsidianChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntitySilverChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum IronChestType implements IStringSerializable
{
    //@formatter:off
    IRON(54, 9, true, "iron_chest.png", TileEntityIronChest.class, 184, 202),
    GOLD(81, 9, true, "gold_chest.png", TileEntityGoldChest.class, 184, 256),
    DIAMOND(108, 12, true, "diamond_chest.png", TileEntityDiamondChest.class, 184, 256),
    COPPER(45, 9, false, "copper_chest.png", TileEntityCopperChest.class, 184, 184),
    SILVER(72, 9, false, "silver_chest.png", TileEntitySilverChest.class, 184, 238),
    CRYSTAL(108, 12, true, "crystal_chest.png", TileEntityCrystalChest.class, 238, 256),
    OBSIDIAN(108, 12, false, "obsidian_chest.png", TileEntityObsidianChest.class, 238, 256),
    DIRTCHEST9000(1, 1, false, "dirt_chest.png", TileEntityDirtChest.class, 184, 184),
    WOOD(0, 0, false, "", null, 0, 0);
    //@formatter:on

    public static final IronChestType VALUES[] = values();

    public final String name;

    public final int size;

    public final int rowLength;

    public final boolean tieredChest;

    public final ResourceLocation modelTexture;

    private String breakTexture;

    public final Class<? extends TileEntityIronChest> clazz;

    public final int xSize;

    public final int ySize;

    //@formatter:off
    IronChestType(int size, int rowLength, boolean tieredChest, String modelTexture, Class<? extends TileEntityIronChest> clazz, int xSize, int ySize)
    //@formatter:on
    {
        this.name = this.name().toLowerCase();
        this.size = size;
        this.rowLength = rowLength;
        this.tieredChest = tieredChest;
        this.modelTexture = new ResourceLocation("ironchest", "textures/model/chest/" + modelTexture);
        this.clazz = clazz;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public String getBreakTexture()
    {
        if (this.breakTexture == null)
        {
            switch (this)
            {
            case DIRTCHEST9000:
            {
                this.breakTexture = "minecraft:blocks/dirt";
                break;
            }
            case OBSIDIAN:
            {
                this.breakTexture = "minecraft:blocks/obsidian";
                break;
            }
            case WOOD:
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

    public boolean isValidForCreativeMode()
    {
        return this != WOOD;
    }

    public boolean isExplosionResistant()
    {
        return this == OBSIDIAN;
    }

    public Slot makeSlot(IInventory chestInventory, int index, int x, int y)
    {
        return new ValidatingChestSlot(chestInventory, index, x, y, this);
    }

    private static final Item DIRT_ITEM = Item.getItemFromBlock(Blocks.DIRT);

    public boolean acceptsStack(ItemStack itemstack)
    {
        if (this == DIRTCHEST9000)
        {
            return itemstack.isEmpty() || itemstack.getItem() == DIRT_ITEM;
        }

        return true;
    }

    public void adornItemDrop(ItemStack item)
    {
        if (this == DIRTCHEST9000)
        {
            item.setTagInfo("dirtchest", new NBTTagByte((byte) 1));
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
