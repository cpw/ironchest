package com.progwml6.ironchest.common.block.entity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import com.progwml6.ironchest.common.network.InventoryTopStacksSyncPacket;
import com.progwml6.ironchest.common.network.IronChestNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalChestBlockEntity extends AbstractIronChestBlockEntity {

  private NonNullList<ItemStack> topStacks;
  private boolean inventoryTouched;
  private boolean hadStuff;

  public CrystalChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(IronChestsBlockEntityTypes.CRYSTAL_CHEST.get(), blockPos, blockState, IronChestsTypes.CRYSTAL, IronChestsBlocks.CRYSTAL_CHEST::get);

    this.topStacks = NonNullList.withSize(8, ItemStack.EMPTY);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return IronChestMenu.createCrystalContainer(containerId, playerInventory, this);
  }

  public static void tick(Level level, BlockPos blockPos, BlockState blockState, AbstractIronChestBlockEntity chestBlockEntity) {
    if (chestBlockEntity instanceof CrystalChestBlockEntity crystalChestBlockEntity) {
      if (!level.isClientSide && crystalChestBlockEntity.inventoryTouched) {
        crystalChestBlockEntity.inventoryTouched = false;

        crystalChestBlockEntity.sortTopStacks();
      }
    }
  }

  @Override
  public void setItems(NonNullList<ItemStack> contents) {
    super.setItems(contents);

    this.inventoryTouched = true;
  }

  @Override
  public ItemStack getItem(int index) {
    this.inventoryTouched = true;

    return super.getItem(index);
  }

  public NonNullList<ItemStack> getTopItems() {
    return this.topStacks;
  }

  private void sortTopStacks() {
    if (!this.getChestType().isTransparent() || (this.level != null && this.level.isClientSide)) {
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

    if (!hasStuff && this.hadStuff) {
      this.hadStuff = false;

      for (int i = 0; i < this.getTopItems().size(); i++) {
        this.getTopItems().set(i, ItemStack.EMPTY);
      }

      if (this.level != null) {
        BlockState blockState = this.level.getBlockState(this.worldPosition);

        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 3);
      }

      return;
    }

    this.hadStuff = true;

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

    if (this.level != null) {
      BlockState blockState = this.level.getBlockState(this.worldPosition);

      this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 3);
    }

    this.sendTopStacksPacket();
  }

  public NonNullList<ItemStack> buildItemStackDataList() {
    if (this.getChestType().isTransparent()) {
      NonNullList<ItemStack> sortList = NonNullList.<ItemStack>withSize(this.getTopItems().size(), ItemStack.EMPTY);

      int pos = 0;

      for (ItemStack is : this.topStacks) {
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

  protected void sendTopStacksPacket() {
    NonNullList<ItemStack> stacks = this.buildItemStackDataList();

    if (this.level != null && this.level instanceof ServerLevel && !this.level.isClientSide) {
      IronChestNetwork.getInstance().sendToClientsAround(new InventoryTopStacksSyncPacket(stacks, this.worldPosition), (ServerLevel) this.level, this.worldPosition);
    }
  }

  public void receiveMessageFromServer(NonNullList<ItemStack> topStacks) {
    this.topStacks = topStacks;
  }
}
