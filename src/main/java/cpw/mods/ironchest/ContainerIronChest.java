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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import invtweaks.api.container.ChestContainer;

@ChestContainer(isLargeChest = true)
public class ContainerIronChest extends Container {
    private IronChestType type;
    private EntityPlayer player;
    private IInventory chest;

    public ContainerIronChest(IInventory playerInventory, IInventory chestInventory, IronChestType type, int xSize, int ySize)
    {
        chest = chestInventory;
        player = ((InventoryPlayer) playerInventory).player;
        this.type = type;
        chestInventory.openInventory();
        layoutContainer(playerInventory, chestInventory, type, xSize, ySize);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return chest.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < type.size)
            {
                if (!mergeItemStack(itemstack1, type.size, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!type.acceptsStack(itemstack1))
            {
                return null;
            }
            else if (!mergeItemStack(itemstack1, 0, type.size, false))
            {
                return null;
            }
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        chest.closeInventory();
    }

    protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, IronChestType type, int xSize, int ySize)
    {
        if (type == IronChestType.DIRTCHEST9000) {
            addSlotToContainer(type.makeSlot(chestInventory, 0, 12 + 4 * 18, 8 + 2 * 18));
        } else {
            for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++)
            {
                for (int chestCol = 0; chestCol < type.getRowLength(); chestCol++)
                {
                    addSlotToContainer(type.makeSlot(chestInventory, chestCol + chestRow * type.getRowLength(), 12 + chestCol * 18, 8 + chestRow * 18));
                }
            }
        }

        int leftCol = (xSize - 162) / 2 + 1;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18
                        - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
        }
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }
    
    @ChestContainer.RowSizeCallback
    public int getNumColumns() {
        return type.getRowLength();
    }
}
