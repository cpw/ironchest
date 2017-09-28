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

import com.google.gson.JsonObject;

import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class ShulkerBoxColorRecipeFactory implements IRecipeFactory
{
    @Override
    public IRecipe parse(JsonContext context, JsonObject json)
    {
        return new ShulkerBoxColorRecipe();
    }

    public static class ShulkerBoxColorRecipe extends Impl<IRecipe> implements IRecipe
    {
        public ShulkerBoxColorRecipe()
        {
        }

        /**
         * Used to check if a recipe matches current crafting inventory
         */
        @Override
        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            int i = 0;
            int j = 0;

            for (int k = 0; k < inv.getSizeInventory(); ++k)
            {
                ItemStack itemstack = inv.getStackInSlot(k);

                if (!itemstack.isEmpty())
                {
                    if (Block.getBlockFromItem(itemstack.getItem()) instanceof BlockIronShulkerBox)
                    {
                        ++i;
                    }
                    else
                    {
                        if (itemstack.getItem() != Items.DYE)
                        {
                            return false;
                        }

                        ++j;
                    }

                    if (j > 1 || i > 1)
                    {
                        return false;
                    }
                }
            }

            return i == 1 && j == 1;
        }

        /**
         * Returns an Item that is the result of this recipe
         */
        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemstack2 = inv.getStackInSlot(i);

                if (!itemstack2.isEmpty())
                {
                    if (Block.getBlockFromItem(itemstack2.getItem()) instanceof BlockIronShulkerBox)
                    {
                        itemstack = itemstack2;
                    }
                    else if (itemstack2.getItem() == Items.DYE)
                    {
                        itemstack1 = itemstack2;
                    }
                }
            }

            ItemStack itemstack3 = BlockIronShulkerBox.getColoredItemStack(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()), itemstack.getMetadata());

            if (itemstack.hasTagCompound())
            {
                itemstack3.setTagCompound(itemstack.getTagCompound().copy());
            }

            return itemstack3;
        }

        @Override
        public ItemStack getRecipeOutput()
        {
            return ItemStack.EMPTY;
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
        {
            NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack> withSize(inv.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i);

                if (itemstack.getItem().hasContainerItem(itemstack))
                {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                }
            }

            return nonnulllist;
        }

        @Override
        public boolean isDynamic()
        {
            return true;
        }

        /**
         * Used to determine if this recipe can fit in a grid of the given width/height
         */
        @Override
        public boolean canFit(int width, int height)
        {
            return width * height >= 2;
        }
    }
}
