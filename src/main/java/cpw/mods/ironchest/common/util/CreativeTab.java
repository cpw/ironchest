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
package cpw.mods.ironchest.common.util;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs
{
    private ItemStack icon;

    // a vanilla icon in case the other one isn't present
    public CreativeTab(String label, ItemStack backupIcon)
    {
        super(label);

        this.icon = backupIcon;
    }

    public void setDisplayIcon(ItemStack displayIcon)
    {
        if (!displayIcon.isEmpty())
        {
            this.icon = displayIcon;
        }
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getIconItemStack()
    {
        return icon;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem()
    {
        return icon;
    }
}