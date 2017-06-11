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
package cpw.mods.ironchest.common.gui.shulker;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@ChestContainer(isLargeChest = true)
public class ContainerIronShulkerBox extends Container
{
    private IronShulkerBoxType type;

    private EntityPlayer player;

    private IInventory inventory;

    public ContainerIronShulkerBox(IInventory playerInventory, IInventory shulkerBoxInventory, IronShulkerBoxType type, int xSize, int ySize)
    {
        this.inventory = shulkerBoxInventory;
        this.player = ((InventoryPlayer) playerInventory).player;
        this.type = type;
        shulkerBoxInventory.openInventory(this.player);
        this.layoutContainer(playerInventory, shulkerBoxInventory, type, xSize, ySize);
    }

    protected void layoutContainer(IInventory playerInventory, IInventory shulkerBoxInventory, IronShulkerBoxType type, int xSize, int ySize)
    {
        for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++)
        {
            for (int chestCol = 0; chestCol < type.rowLength; chestCol++)
            {
                this.addSlotToContainer(type.makeSlot(shulkerBoxInventory, chestCol + chestRow * type.rowLength, 12 + chestCol * 18, 8 + chestRow * 18));
            }
        }

        int leftCol = (xSize - 162) / 2 + 1;

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                //@formatter:off
                this.addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
                //@formatter:on
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            this.addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
        }
    }

    public EntityPlayer getPlayer()
    {
        return this.player;
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.inventory.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.type.size)
            {
                if (!this.mergeItemStack(itemstack1, this.type.size, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.type.size, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        this.inventory.closeInventory(playerIn);
    }

    @ChestContainer.RowSizeCallback
    public int getNumColumns()
    {
        return this.type.rowLength;
    }
}
