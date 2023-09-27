package com.progwml6.ironchest.common.block.regular;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.CopperChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CopperChestBlock extends AbstractIronChestBlock {

  public CopperChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.COPPER_CHEST::get, IronChestsTypes.COPPER);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new CopperChestBlockEntity(blockPos, blockState);
  }
}
