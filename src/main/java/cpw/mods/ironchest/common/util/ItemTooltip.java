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

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemTooltip extends Item
{
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
    {
        addOptionalTooltip(stack, tooltip);

        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    public static void addOptionalTooltip(ItemStack stack, List<String> tooltip)
    {
        if (I18n.canTranslate(stack.getUnlocalizedName() + ".tooltip"))
        {
            tooltip.addAll(getTooltips(TextFormatting.GRAY.toString() + translateRecursive(stack.getUnlocalizedName() + ".tooltip")));
        }
        else if (I18n.canTranslate(stack.getUnlocalizedName() + ".tooltip"))
        {
            tooltip.addAll(getTooltips(TextFormatting.GRAY.toString() + translateRecursive(stack.getUnlocalizedName() + ".tooltip")));
        }
    }

    public static String translateRecursive(String key, Object... params)
    {
        return I18n.translateToLocal(I18n.translateToLocalFormatted(key, params));
    }

    public static List<String> getTooltips(String text)
    {
        List<String> list = Lists.newLinkedList();
        if (text == null)
            return list;
        int j = 0;
        int k;
        while ((k = text.indexOf("\\n", j)) >= 0)
        {
            list.add(text.substring(j, k));
            j = k + 2;
        }

        list.add(text.substring(j, text.length()));

        return list;
    }
}
