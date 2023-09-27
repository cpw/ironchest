package com.progwml6.ironchest.common.block.trapped;

import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.AbstractTrappedIronChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class AbstractTrappedIronChestBlock extends AbstractIronChestBlock {

  public AbstractTrappedIronChestBlock(Properties properties, Supplier<BlockEntityType<? extends AbstractIronChestBlockEntity>> blockEntityType, IronChestsTypes type) {
    super(properties, blockEntityType, type);
  }

  @Override
  protected Stat<ResourceLocation> getOpenChestStat() {
    return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
  }

  @Override
  public boolean isSignalSource(BlockState blockState) {
    return true;
  }

  @Override
  public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
    return Mth.clamp(AbstractTrappedIronChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
    return direction == Direction.UP ? blockState.getSignal(blockGetter, blockPos, direction) : 0;
  }
}
