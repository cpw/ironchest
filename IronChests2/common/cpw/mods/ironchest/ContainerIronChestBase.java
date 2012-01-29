package cpw.mods.ironchest;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public abstract class ContainerIronChestBase extends Container {
	public ContainerIronChestBase(IInventory playerInventory, IInventory chestInventory) {
        numRows = chestInventory.getSizeInventory() / getRowLength();
        chest = chestInventory;
        chestInventory.openChest();
        layoutContainer(playerInventory, chestInventory);

    }

	protected abstract void layoutContainer(IInventory playerInventory, IInventory chestInventory);
	protected abstract int getRowLength();
	
    public boolean canInteractWith(EntityPlayer player)
    {
        return chest.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < numRows * getRowLength())
            {
                if(!mergeItemStack(itemstack1, numRows * getRowLength(), inventorySlots.size(), true))
                {
                    return null;
                }
            } else
            if(!mergeItemStack(itemstack1, 0, numRows * getRowLength(), false))
            {
                return null;
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        super.onCraftGuiClosed(entityplayer);
        chest.closeChest();
    }

    private IInventory chest;
    private int numRows;
}
