package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class SilverChestTileEntity extends GenericIronChestTileEntity {

  public SilverChestTileEntity() {
    super(IronChestsTileEntityTypes.SILVER_CHEST.get(), IronChestsTypes.SILVER, IronChestsBlocks.SILVER_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return IronChestContainer.createSilverContainer(id, playerInventory, this);
  }
}