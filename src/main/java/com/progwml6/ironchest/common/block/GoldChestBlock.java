package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.entity.GoldChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GoldChestBlock extends AbstractIronChestBlock {

  public GoldChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.GOLD_CHEST::get, IronChestsTypes.GOLD);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new GoldChestBlockEntity(blockPos, blockState);
  }
}
