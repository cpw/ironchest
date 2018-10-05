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
package cpw.mods.ironchest.common.items.chest;

import java.util.Locale;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemIronChest extends ItemBlock
{
    public ItemIronChest(Block block)
    {
        super(block);

        this.setRegistryName(block.getRegistryName());
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
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

        if (meta < IronChestType.VALUES.length)
        {
            return "tile.ironchest.chest." + IronChestType.VALUES[itemstack.getMetadata()].name().toLowerCase(Locale.US);
        }
        else
        {
            return super.getUnlocalizedName(itemstack);
        }
    }
}
