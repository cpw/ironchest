package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Collections;

public class CrystalChestTileEntity extends GenericIronChestTileEntity {

  private NonNullList<ItemStack> topStacks;

  private boolean inventoryTouched;

  private boolean hadStuff;

  public CrystalChestTileEntity() {
    super(IronChestsTileEntityTypes.CRYSTAL_CHEST.get(), IronChestsTypes.CRYSTAL, IronChestsBlocks.CRYSTAL_CHEST::get);
    this.topStacks = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return IronChestContainer.createCrystalContainer(id, playerInventory, this);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.world.isRemote && this.inventoryTouched) {
      this.inventoryTouched = false;

      this.sortTopStacks();
    }
  }

  @Override
  public void setItems(NonNullList<ItemStack> contents) {
    super.setItems(contents);

    this.inventoryTouched = true;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    this.inventoryTouched = true;

    return super.getStackInSlot(index);
  }

  public NonNullList<ItemStack> getTopItems() {
    return this.topStacks;
  }

  private void sortTopStacks() {
    if (!this.getChestType().isTransparent() || (this.world != null && this.world.isRemote)) {
      return;
    }

    NonNullList<ItemStack> tempCopy = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

    boolean hasStuff = false;

    int compressedIdx = 0;

    mainLoop:
    for (int i = 0; i < this.getSizeInventory(); i++) {
      ItemStack itemStack = this.getItems().get(i);

      if (!itemStack.isEmpty()) {
        for (int j = 0; j < compressedIdx; j++) {
          ItemStack tempCopyStack = tempCopy.get(j);

          if (ItemStack.areItemsEqualIgnoreDurability(tempCopyStack, itemStack)) {
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

      if (this.world != null) {
        BlockState iblockstate = this.world.getBlockState(this.pos);

        this.world.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 3);
      }

      return;
    }

    this.hadStuff = true;

    Collections.sort(tempCopy, (stack1, stack2) -> {
      if (stack1.isEmpty()) {
        return 1;
      }
      else if (stack2.isEmpty()) {
        return -1;
      }
      else {
        return stack2.getCount() - stack1.getCount();
      }
    });

    int p = 0;

    for (ItemStack element : tempCopy) {
      if (!element.isEmpty() && element.getCount() > 0) {
        if (p == this.getTopItems().size()) {
          break;
        }

        this.getTopItems().set(p, element);

        p++;
      }
    }

    for (int i = p; i < this.getTopItems().size(); i++) {
      this.getTopItems().set(i, ItemStack.EMPTY);
    }

    if (this.world != null) {
      BlockState iblockstate = this.world.getBlockState(this.pos);

      this.world.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 3);
    }

    sendTopStacksPacket();
  }

  public NonNullList<ItemStack> buildItemStackDataList() {
    if (this.getChestType().isTransparent()) {
      NonNullList<ItemStack> sortList = NonNullList.<ItemStack>withSize(this.getTopItems().size(), ItemStack.EMPTY);

      int pos = 0;

      for (ItemStack is : this.topStacks) {
        if (!is.isEmpty()) {
          sortList.set(pos, is);
        }
        else {
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

    //PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> (Chunk) this.getWorld().getChunk(this.getPos())), new PacketTopStackSyncChest(this.getWorld().getDimension().getType().getId(), this.getPos(), stacks));
  }

  public void receiveMessageFromServer(NonNullList<ItemStack> topStacks) {
    this.topStacks = topStacks;
  }
}