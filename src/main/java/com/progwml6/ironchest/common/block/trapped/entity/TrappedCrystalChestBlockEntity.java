package com.progwml6.ironchest.common.block.trapped.entity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.ICrystalChest;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TrappedCrystalChestBlockEntity extends AbstractTrappedIronChestBlockEntity implements ICrystalChest {

  private NonNullList<ItemStack> topStacks;
  private boolean inventoryTouched;
  private boolean hadStuff;

  public TrappedCrystalChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(IronChestsBlockEntityTypes.TRAPPED_CRYSTAL_CHEST.get(), blockPos, blockState, IronChestsTypes.CRYSTAL, IronChestsBlocks.TRAPPED_CRYSTAL_CHEST::get);

    this.topStacks = NonNullList.withSize(8, ItemStack.EMPTY);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return IronChestMenu.createCrystalContainer(containerId, playerInventory, this);
  }

  public static void tick(Level level, BlockPos blockPos, BlockState blockState, AbstractIronChestBlockEntity chestBlockEntity) {
    if (chestBlockEntity instanceof TrappedCrystalChestBlockEntity crystalChest) {
      if (!level.isClientSide && crystalChest.inventoryTouched) {
        crystalChest.inventoryTouched = false;

        crystalChest.sortTopStacks();
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

  @Override
  public NonNullList<ItemStack> getTopItems() {
    return this.topStacks;
  }

  @Override
  public Level getChestLevel() {
    return this.level;
  }

  @Override
  public BlockPos getChestWorldPosition() {
    return this.worldPosition;
  }

  @Override
  public void receiveMessageFromServer(NonNullList<ItemStack> topStacks) {
    this.topStacks = topStacks;
  }

  @Override
  public void setHadStuff(boolean hadStuff) {
    this.hadStuff = hadStuff;
  }

  @Override
  public boolean getHadStuff() {
    return this.hadStuff;
  }
}
