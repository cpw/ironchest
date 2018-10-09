/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package cpw.mods.ironchest.common.lib;

import javax.annotation.Nonnull;

import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class ICShulkerInventoryHandler implements IItemHandlerModifiable
{
    TileEntityIronShulkerBox inv;

    public ICShulkerInventoryHandler(TileEntityIronShulkerBox inventory)
    {
        this.inv = inventory;
    }

    @Override
    public int getSlots()
    {
        return this.inv.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return this.inv.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
            return stack;
        stack = stack.copy();

        if (!inv.isItemValidForSlot(slot, stack))
            return stack;

        int offsetSlot = slot;
        ItemStack currentStack = inv.getItems().get(offsetSlot);

        if (currentStack.isEmpty())
        {
            int accepted = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
            if (accepted < stack.getCount())
            {
                if (!simulate)
                {
                    inv.getItems().set(offsetSlot, stack.splitStack(accepted));
                    inv.markDirty();
                    return stack;
                }
                else
                {
                    stack.shrink(accepted);
                    return stack;
                }
            }
            else
            {
                if (!simulate)
                {
                    inv.getItems().set(offsetSlot, stack);
                    inv.markDirty();
                }
                return ItemStack.EMPTY;
            }
        }
        else
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, currentStack))
                return stack;

            int accepted = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()) - currentStack.getCount();
            if (accepted < stack.getCount())
            {
                if (!simulate)
                {
                    ItemStack newStack = stack.splitStack(accepted);
                    newStack.grow(currentStack.getCount());
                    inv.getItems().set(offsetSlot, newStack);
                    inv.markDirty();
                    return stack;
                }
                else
                {
                    stack.shrink(accepted);
                    return stack;
                }
            }
            else
            {
                if (!simulate)
                {
                    ItemStack newStack = stack.copy();
                    newStack.grow(currentStack.getCount());
                    inv.getItems().set(offsetSlot, newStack);
                    inv.markDirty();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.EMPTY;

        int offsetSlot = slot;
        ItemStack currentStack = inv.getItems().get(offsetSlot);

        if (currentStack.isEmpty())
            return ItemStack.EMPTY;

        int extracted = Math.min(currentStack.getCount(), amount);

        ItemStack copy = currentStack.copy();
        copy.setCount(extracted);
        if (!simulate)
        {
            if (extracted < currentStack.getCount())
                currentStack.shrink(extracted);
            else
                currentStack = ItemStack.EMPTY;
            inv.getItems().set(offsetSlot, currentStack);
            inv.markDirty();
        }
        return copy;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return getInv().getInventoryStackLimit();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        inv.getItems().set(slot, stack);
        inv.markDirty();
    }

    public IInventory getInv()
    {
        return inv;
    }
}
