package com.progwml6.ironchest.common.ai;

import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class IronChestCatSitOnBlockGoal extends CatSitOnBlockGoal {

  public IronChestCatSitOnBlockGoal(Cat catEntity, double p_i50330_2_) {
    super(catEntity, p_i50330_2_);
  }

  @Override
  protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
    if (!worldIn.isEmptyBlock(pos.above())) {
      return false;
    }
    else {
      BlockState blockstate = worldIn.getBlockState(pos);
      Block block = blockstate.getBlock();

      if (block instanceof GenericIronChestBlock) {
        return GenericIronChestTileEntity.getPlayersUsing(worldIn, pos) < 1;
      }

      return super.isValidTarget(worldIn, pos);
    }
  }
}
