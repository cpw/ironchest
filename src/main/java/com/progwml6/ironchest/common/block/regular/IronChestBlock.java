package com.progwml6.ironchest.common.block.regular;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.IronChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class IronChestBlock extends AbstractIronChestBlock {

  public IronChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.IRON_CHEST::get, IronChestsTypes.IRON);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new IronChestBlockEntity(blockPos, blockState);
  }
}
