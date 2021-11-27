package com.progwml6.ironchest.common.ai;

import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class IronChestCatSitOnBlockGoal extends CatSitOnBlockGoal {

  public IronChestCatSitOnBlockGoal(Cat cat, double p_i50330_2_) {
    super(cat, p_i50330_2_);
  }

  @Override
  protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
    if (!worldIn.isEmptyBlock(pos.above())) {
      return false;
    } else {
      BlockState blockstate = worldIn.getBlockState(pos);
      Block block = blockstate.getBlock();

      if (block instanceof AbstractIronChestBlock) {
        return AbstractIronChestBlockEntity.getOpenCount(worldIn, pos) < 1;
      }

      return super.isValidTarget(worldIn, pos);
    }
  }
}
