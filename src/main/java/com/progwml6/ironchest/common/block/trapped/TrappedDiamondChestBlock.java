package com.progwml6.ironchest.common.block.trapped;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedDiamondChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TrappedDiamondChestBlock extends AbstractTrappedIronChestBlock {

  public TrappedDiamondChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.TRAPPED_DIAMOND_CHEST::get, IronChestsTypes.DIAMOND);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TrappedDiamondChestBlockEntity(blockPos, blockState);
  }
}
