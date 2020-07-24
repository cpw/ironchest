package com.progwml6.ironchest.common.ai;

import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class IronChestCatSitOnBlockGoal extends CatSitOnBlockGoal {

  public IronChestCatSitOnBlockGoal(CatEntity catEntity, double p_i50330_2_) {
    super(catEntity, p_i50330_2_);
  }

  @Override
  protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
    if (!worldIn.isAirBlock(pos.up())) {
      return false;
    }
    else {
      BlockState blockstate = worldIn.getBlockState(pos);
      Block block = blockstate.getBlock();

      if (block instanceof GenericIronChestBlock) {
        return GenericIronChestTileEntity.getPlayersUsing(worldIn, pos) < 1;
      }

      return super.shouldMoveTo(worldIn, pos);
    }
  }
}
