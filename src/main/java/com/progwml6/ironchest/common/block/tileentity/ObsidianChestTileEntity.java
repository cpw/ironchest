package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ObsidianChestTileEntity extends GenericIronChestTileEntity {

  public ObsidianChestTileEntity() {
    super(IronChestsTileEntityTypes.OBSIDIAN_CHEST.get(), IronChestsTypes.OBSIDIAN, IronChestsBlocks.OBSIDIAN_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return IronChestContainer.createObsidianContainer(id, playerInventory, this);
  }
}
