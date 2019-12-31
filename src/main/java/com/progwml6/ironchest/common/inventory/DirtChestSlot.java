package com.progwml6.ironchest.common.inventory;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DirtChestSlot extends Slot {

  public DirtChestSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return stack.isEmpty() || stack.getItem() == Item.getItemFromBlock(Blocks.DIRT);
  }
}