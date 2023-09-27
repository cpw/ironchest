package com.progwml6.ironchest.common.block.trapped.entity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class TrappedObsidianChestBlockEntity extends AbstractTrappedIronChestBlockEntity {

  public TrappedObsidianChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(IronChestsBlockEntityTypes.TRAPPED_OBSIDIAN_CHEST.get(), blockPos, blockState, IronChestsTypes.OBSIDIAN, IronChestsBlocks.TRAPPED_OBSIDIAN_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return IronChestMenu.createObsidianContainer(containerId, playerInventory, this);
  }
}
