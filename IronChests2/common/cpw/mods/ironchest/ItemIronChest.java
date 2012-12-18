/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemIronChest extends ItemBlock {

    public ItemIronChest(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i)
    {
        return IronChestType.validateMeta(i);
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        return IronChestType.values()[itemstack.getItemDamage()].name();
    }
}
