package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CrystalChestBlock extends GenericIronChestBlock {

  public CrystalChestBlock(Properties properties) {
    super(IronChestsTypes.CRYSTAL, IronChestsTileEntityTypes.CRYSTAL_CHEST::get, properties);
  }


  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new CrystalChestTileEntity();
  }
}
