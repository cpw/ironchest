package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.GoldChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class GoldChestBlock extends GenericIronChestBlock {

  public GoldChestBlock(Properties properties) {
    super(IronChestsTypes.GOLD, IronChestsTileEntityTypes.GOLD_CHEST::get, properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new GoldChestTileEntity();
  }
}
