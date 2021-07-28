package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.entity.DiamondChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class DiamondChestBlock extends AbstractIronChestBlock {

  public DiamondChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.DIAMOND_CHEST::get, IronChestsTypes.DIAMOND);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DiamondChestBlockEntity(blockPos, blockState);
  }
}
