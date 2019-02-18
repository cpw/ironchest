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
package com.progwml6.ironchest.common.items;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTooltip extends Item
{
    public ItemTooltip(Properties properties)
    {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        addOptionalTooltip(stack, tooltip);

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static void addOptionalTooltip(ItemStack stack, List<ITextComponent> tooltip)
    {
        if (I18n.hasKey(stack.getDisplayName() + ".tooltip"))
        {
            for (String tooltipString : getTooltips(TextFormatting.GRAY.toString() + translateRecursive(stack.getDisplayName() + ".tooltip")))
            {
                tooltip.add(new TextComponentString(tooltipString));
            }
        }
        else if (I18n.hasKey(stack.getDisplayName() + ".tooltip"))
        {
            for (String tooltipString : getTooltips(TextFormatting.GRAY.toString() + translateRecursive(stack.getDisplayName() + ".tooltip")))
            {
                tooltip.add(new TextComponentString(tooltipString));
            }
        }
    }

    public static String translateRecursive(String key, Object... params)
    {
        return I18n.format(I18n.format(key, params));
    }

    public static List<String> getTooltips(String text)
    {
        List<String> list = Lists.newLinkedList();
        if (text == null)
        {
            return list;
        }
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
