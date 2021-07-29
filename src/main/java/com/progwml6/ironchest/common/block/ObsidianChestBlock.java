package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.entity.ObsidianChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ObsidianChestBlock extends AbstractIronChestBlock {

  public ObsidianChestBlock(BlockBehaviour.Properties properties) {
    super(properties, IronChestsBlockEntityTypes.OBSIDIAN_CHEST::get, IronChestsTypes.OBSIDIAN);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ObsidianChestBlockEntity(blockPos, blockState);
  }
}
