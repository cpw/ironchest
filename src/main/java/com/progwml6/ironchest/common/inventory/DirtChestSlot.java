package com.progwml6.ironchest.common.inventory;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DirtChestSlot extends Slot {

  public DirtChestSlot(Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.isEmpty() || stack.getItem() == Item.byBlock(Blocks.DIRT);
  }
}