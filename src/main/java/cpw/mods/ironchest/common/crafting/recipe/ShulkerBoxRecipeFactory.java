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
package cpw.mods.ironchest.common.crafting.recipe;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShulkerBoxRecipeFactory implements IRecipeFactory
{
    @Override
    public IRecipe parse(JsonContext context, JsonObject json)
    {
        ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

        ShapedPrimer primer = new ShapedPrimer();
        primer.width = recipe.getRecipeWidth();
        primer.height = recipe.getRecipeHeight();
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = recipe.getIngredients();

        return new ShulkerBoxRecipe(new ResourceLocation(IronChest.MOD_ID, "shulker_box_crafting"), recipe.getRecipeOutput(), primer);
    }

    public static class ShulkerBoxRecipe extends ShapedOreRecipe
    {
        public ShulkerBoxRecipe(ResourceLocation group, ItemStack result, ShapedPrimer primer)
        {
            super(group, result, primer);
        }

        @Override
        @Nonnull
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1)
        {
            ItemStack newOutput = this.output.copy();

            ItemStack itemstack = ItemStack.EMPTY;

            for (int i = 0; i < var1.getSizeInventory(); ++i)
            {
                ItemStack stack = var1.getStackInSlot(i);

                if (!stack.isEmpty())
                {
                    if (Block.getBlockFromItem(stack.getItem()) instanceof BlockIronShulkerBox || Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox)
                    {
                        itemstack = stack;
                    }
                }
            }

            if (itemstack.hasTagCompound())
            {
                newOutput.setTagCompound(itemstack.getTagCompound().copy());
            }

            return newOutput;
        }
    }
}
