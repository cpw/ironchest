package com.progwml6.ironchest.common.block.trapped;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedCopperChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TrappedCopperChestBlock extends AbstractTrappedIronChestBlock {

  public TrappedCopperChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.TRAPPED_COPPER_CHEST::get, IronChestsTypes.COPPER);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TrappedCopperChestBlockEntity(blockPos, blockState);
  }
}
