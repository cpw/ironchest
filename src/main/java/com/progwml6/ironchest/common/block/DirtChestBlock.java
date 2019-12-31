package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.DirtChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import com.progwml6.ironchest.common.block.tileentity.ObsidianChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class DirtChestBlock extends GenericIronChestBlock {

  public DirtChestBlock(Properties properties) {
    super(IronChestsTypes.DIRTCHEST9000, IronChestsTileEntityTypes.DIRT_CHEST::get, properties);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new DirtChestTileEntity();
  }
}
