package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.CrystalChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CrystalChestBlock extends AbstractIronChestBlock {

  public CrystalChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.CRYSTAL_CHEST::get, IronChestsTypes.CRYSTAL);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new CrystalChestBlockEntity(blockPos, blockState);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return level.isClientSide ? createTickerHelper(blockEntityType, this.blockEntityType(), AbstractIronChestBlockEntity::lidAnimateTick) : createTickerHelper(blockEntityType, this.blockEntityType(), CrystalChestBlockEntity::tick);
  }
}
