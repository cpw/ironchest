package com.progwml6.ironchest.common.block.trapped.entity;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class AbstractTrappedIronChestBlockEntity extends AbstractIronChestBlockEntity {

  protected AbstractTrappedIronChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, IronChestsTypes chestTypeIn, Supplier<Block> blockToUseIn) {
    super(blockEntityType, blockPos, blockState, chestTypeIn, blockToUseIn);
  }

  @Override
  protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int previousCount, int newCount) {
    super.signalOpenCount(level, blockPos, blockState, previousCount, newCount);

    if (previousCount != newCount) {
      Block block = blockState.getBlock();

      level.updateNeighborsAt(blockPos, block);
      level.updateNeighborsAt(blockPos.below(), block);
    }
  }
}
