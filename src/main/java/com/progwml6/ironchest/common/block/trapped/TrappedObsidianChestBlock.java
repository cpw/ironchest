package com.progwml6.ironchest.common.block.trapped;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedObsidianChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TrappedObsidianChestBlock extends AbstractTrappedIronChestBlock {

  public TrappedObsidianChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.TRAPPED_OBSIDIAN_CHEST::get, IronChestsTypes.OBSIDIAN);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TrappedObsidianChestBlockEntity(blockPos, blockState);
  }
}
