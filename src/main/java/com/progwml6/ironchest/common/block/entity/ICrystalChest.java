package com.progwml6.ironchest.common.block.entity;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.network.InventoryTopStacksSyncPacket;
import com.progwml6.ironchest.common.network.IronChestNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ICrystalChest {

  default NonNullList<ItemStack> buildItemStackDataList() {
    if (this.getChestType().isTransparent()) {
      NonNullList<ItemStack> sortList = NonNullList.<ItemStack>withSize(this.getTopItems().size(), ItemStack.EMPTY);

      int pos = 0;

      for (ItemStack is : this.getTopItems()) {
        if (!is.isEmpty()) {
          sortList.set(pos, is);
        } else {
          sortList.set(pos, ItemStack.EMPTY);
        }

        pos++;
      }

      return sortList;
    }

    return NonNullList.<ItemStack>withSize(this.getTopItems().size(), ItemStack.EMPTY);
  }

  IronChestsTypes getChestType();

  default void sendTopStacksPacket() {
    NonNullList<ItemStack> stacks = this.buildItemStackDataList();

    if (this.getChestLevel() != null && this.getChestLevel() instanceof ServerLevel && !this.getChestLevel().isClientSide) {
      IronChestNetwork.getInstance().sendToClientsAround(new InventoryTopStacksSyncPacket(stacks, this.getChestWorldPosition()), (ServerLevel) this.getChestLevel(), this.getChestWorldPosition());
    }
  }

  NonNullList<ItemStack> getTopItems();

  Level getChestLevel();

  BlockPos getChestWorldPosition();

  void receiveMessageFromServer(NonNullList<ItemStack> topStacks);

  default void sortTopStacks() {
    if (!this.getChestType().isTransparent() || (this.getChestLevel() != null && this.getChestLevel().isClientSide)) {
      return;
    }

    NonNullList<ItemStack> tempCopy = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);

    boolean hasStuff = false;

    int compressedIdx = 0;

    mainLoop:
    for (int i = 0; i < this.getContainerSize(); i++) {
      ItemStack itemStack = this.getItems().get(i);

      if (!itemStack.isEmpty()) {
        for (int j = 0; j < compressedIdx; j++) {
          ItemStack tempCopyStack = tempCopy.get(j);

          if (ItemStack.isSameIgnoreDurability(tempCopyStack, itemStack)) {
            if (itemStack.getCount() != tempCopyStack.getCount()) {
              tempCopyStack.grow(itemStack.getCount());
            }

            continue mainLoop;
          }
        }

        tempCopy.set(compressedIdx, itemStack.copy());

        compressedIdx++;

        hasStuff = true;
      }
    }

    if (!hasStuff && this.getHadStuff()) {
      this.setHadStuff(false);

      for (int i = 0; i < this.getTopItems().size(); i++) {
        this.getTopItems().set(i, ItemStack.EMPTY);
      }

      if (this.getChestLevel() != null) {
        BlockState blockState = this.getChestLevel().getBlockState(this.getChestWorldPosition());

        this.getChestLevel().sendBlockUpdated(this.getChestWorldPosition(), blockState, blockState, 3);
      }

      return;
    }

    this.setHadStuff(true);

    tempCopy.sort((stack1, stack2) -> {
      if (stack1.isEmpty()) {
        return 1;
      } else if (stack2.isEmpty()) {
        return -1;
      } else {
        return stack2.getCount() - stack1.getCount();
      }
    });

    int slot = 0;

    for (ItemStack itemStack : tempCopy) {
      if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
        if (slot == this.getTopItems().size()) {
          break;
        }

        this.getTopItems().set(slot, itemStack);

        slot++;
      }
    }

    for (int i = slot; i < this.getTopItems().size(); i++) {
      this.getTopItems().set(i, ItemStack.EMPTY);
    }

    if (this.getChestLevel() != null) {
      BlockState blockState = this.getChestLevel().getBlockState(this.getChestWorldPosition());

      this.getChestLevel().sendBlockUpdated(this.getChestWorldPosition(), blockState, blockState, 3);
    }

    this.sendTopStacksPacket();
  }

  NonNullList<ItemStack> getItems();

  void setHadStuff(boolean hadStuff);
  
  boolean getHadStuff();

  int getContainerSize();
}
