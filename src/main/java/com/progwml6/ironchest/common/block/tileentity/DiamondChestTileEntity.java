package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class DiamondChestTileEntity extends GenericIronChestTileEntity {

  public DiamondChestTileEntity() {
    super(IronChestsTileEntityTypes.DIAMOND_CHEST.get(), IronChestsTypes.DIAMOND, IronChestsBlocks.DIAMOND_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return IronChestContainer.createDiamondContainer(id, playerInventory, this);
  }
}
