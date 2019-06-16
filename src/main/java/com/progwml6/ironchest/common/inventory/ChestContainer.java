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
package com.progwml6.ironchest.common.inventory;

import com.progwml6.ironchest.common.blocks.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChestContainer extends Container
{
    private final IInventory inventory;

    private final ChestType chestType;

    private ChestContainer(ContainerType<?> containerType, int windowId, PlayerInventory playerInventory)
    {
        this(containerType, windowId, playerInventory, new Inventory(ChestType.WOOD.size), ChestType.WOOD);
    }

    public static ChestContainer createIronContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.IRON_CHEST, windowId, playerInventory, new Inventory(ChestType.IRON.size), ChestType.IRON);
    }

    public static ChestContainer createIronContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.IRON_CHEST, windowId, playerInventory, inventory, ChestType.IRON);
    }

    public static ChestContainer createGoldContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.GOLD_CHEST, windowId, playerInventory, new Inventory(ChestType.GOLD.size), ChestType.GOLD);
    }

    public static ChestContainer createGoldContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.GOLD_CHEST, windowId, playerInventory, inventory, ChestType.GOLD);
    }

    public static ChestContainer createDiamondContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.DIAMOND_CHEST, windowId, playerInventory, new Inventory(ChestType.DIAMOND.size), ChestType.DIAMOND);
    }

    public static ChestContainer createDiamondContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.DIAMOND_CHEST, windowId, playerInventory, inventory, ChestType.DIAMOND);
    }

    public static ChestContainer createCrystalContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.CRYSTAL_CHEST, windowId, playerInventory, new Inventory(ChestType.CRYSTAL.size), ChestType.CRYSTAL);
    }

    public static ChestContainer createCrystalContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.CRYSTAL_CHEST, windowId, playerInventory, inventory, ChestType.CRYSTAL);
    }

    public static ChestContainer createCopperContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.COPPER_CHEST, windowId, playerInventory, new Inventory(ChestType.COPPER.size), ChestType.COPPER);
    }

    public static ChestContainer createCopperContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.COPPER_CHEST, windowId, playerInventory, inventory, ChestType.COPPER);
    }

    public static ChestContainer createSilverContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.SILVER_CHEST, windowId, playerInventory, new Inventory(ChestType.CRYSTAL.size), ChestType.SILVER);
    }

    public static ChestContainer createSilverContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.SILVER_CHEST, windowId, playerInventory, inventory, ChestType.SILVER);
    }

    public static ChestContainer createObsidianContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.OBSIDIAN_CHEST, windowId, playerInventory, new Inventory(ChestType.OBSIDIAN.size), ChestType.OBSIDIAN);
    }

    public static ChestContainer createObsidianContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.OBSIDIAN_CHEST, windowId, playerInventory, inventory, ChestType.OBSIDIAN);
    }

    public static ChestContainer createDirtContainer(int windowId, PlayerInventory playerInventory)
    {
        return new ChestContainer(ChestContainerType.DIRT_CHEST, windowId, playerInventory, new Inventory(ChestType.DIRTCHEST9000.size), ChestType.DIRTCHEST9000);
    }

    public static ChestContainer createDirtContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        return new ChestContainer(ChestContainerType.DIRT_CHEST, windowId, playerInventory, inventory, ChestType.DIRTCHEST9000);
    }

    public ChestContainer(ContainerType<?> containerType, int windowId, PlayerInventory playerInventory, IInventory inventory, ChestType chestType)
    {
        super(containerType, windowId);
        func_216962_a(inventory, chestType.size);

        this.inventory = inventory;
        this.chestType = chestType;

        inventory.openInventory(playerInventory.player);

        if (chestType == ChestType.DIRTCHEST9000)
        {
            this.addSlot(new DirtChestSlot(inventory, 0, 12 + 4 * 18, 8 + 2 * 18));
        }
        else
        {
            for (int chestRow = 0; chestRow < chestType.getRowCount(); chestRow++)
            {
                for (int chestCol = 0; chestCol < chestType.rowLength; chestCol++)
                {
                    this.addSlot(new Slot(inventory, chestCol + chestRow * chestType.rowLength, 12 + chestCol * 18, 18 + chestRow * 18));
                }
            }
        }

        int leftCol = (chestType.xSize - 162) / 2 + 1;

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                this.addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, chestType.ySize - (4 - playerInvRow) * 18 - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            this.addSlot(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, chestType.ySize - 24));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.inventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.chestType.size)
            {
                if (!this.mergeItemStack(itemstack1, this.chestType.size, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.chestType.size, false))
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

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public ChestType getChestType()
    {
        return this.chestType;
    }
}
