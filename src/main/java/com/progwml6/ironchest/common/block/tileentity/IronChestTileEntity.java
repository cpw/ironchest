package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;

public class IronChestTileEntity extends GenericIronChestTileEntity {

  public IronChestTileEntity() {
    super(IronChestsTileEntityTypes.IRON_CHEST.get(), IronChestsTypes.IRON, IronChestsBlocks.IRON_CHEST::get);
  }
}
