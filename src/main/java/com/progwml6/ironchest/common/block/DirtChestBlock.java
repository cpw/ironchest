package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.entity.DirtChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class DirtChestBlock extends AbstractIronChestBlock {

  public DirtChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.DIRT_CHEST::get, IronChestsTypes.DIRT);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DirtChestBlockEntity(blockPos, blockState);
  }
}
