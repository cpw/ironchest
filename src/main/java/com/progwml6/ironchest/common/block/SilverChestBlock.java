package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.entity.SilverChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SilverChestBlock extends AbstractIronChestBlock {

  public SilverChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.SILVER_CHEST::get, IronChestsTypes.SILVER);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SilverChestBlockEntity(blockPos, blockState);
  }
}
