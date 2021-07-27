package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.IronChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

public class IronChestBlock extends GenericIronChestBlock {

  public IronChestBlock(Block.Properties properties) {
    super(IronChestsTypes.IRON, IronChestsTileEntityTypes.IRON_CHEST::get, properties);
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new IronChestTileEntity();
  }
}
