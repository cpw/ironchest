package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.DiamondChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class DiamondChestBlock extends GenericIronChestBlock {

  public DiamondChestBlock(Properties properties) {
    super(IronChestsTypes.DIAMOND, IronChestsTileEntityTypes.DIAMOND_CHEST::get, properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new DiamondChestTileEntity();
  }
}