package com.progwml6.ironchest.common.block.trapped;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.TrappedCrystalChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TrappedCrystalChestBlock extends AbstractTrappedIronChestBlock {

  public TrappedCrystalChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.TRAPPED_CRYSTAL_CHEST::get, IronChestsTypes.CRYSTAL);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new TrappedCrystalChestBlockEntity(blockPos, blockState);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return level.isClientSide ? createTickerHelper(blockEntityType, this.blockEntityType(), AbstractIronChestBlockEntity::lidAnimateTick) : createTickerHelper(blockEntityType, this.blockEntityType(), TrappedCrystalChestBlockEntity::tick);
  }
}
