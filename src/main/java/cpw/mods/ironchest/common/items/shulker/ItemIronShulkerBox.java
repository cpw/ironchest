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
package cpw.mods.ironchest.common.items.shulker;

import java.util.Locale;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemIronShulkerBox extends ItemBlock
{
    private final String colorName;

    public ItemIronShulkerBox(Block block, EnumDyeColor colorIn)
    {
        super(block);

        this.setRegistryName(block.getRegistryName());
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.colorName = colorIn.getName();
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int meta = itemstack.getMetadata();

        if (meta < IronShulkerBoxType.VALUES.length)
        {
            return "tile.ironchest.shulker_box." + IronShulkerBoxType.VALUES[itemstack.getMetadata()].name().toLowerCase(Locale.US) + "." + this.colorName;
        }
        else
        {
            return super.getUnlocalizedName(itemstack);
        }
    }
}
