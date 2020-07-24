package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.CopperChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CopperChestBlock extends GenericIronChestBlock {

  public CopperChestBlock(Properties properties) {
    super(IronChestsTypes.COPPER, IronChestsTileEntityTypes.COPPER_CHEST::get, properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new CopperChestTileEntity();
  }
}
