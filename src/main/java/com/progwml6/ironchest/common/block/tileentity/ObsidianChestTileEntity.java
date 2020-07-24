package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class ObsidianChestTileEntity extends GenericIronChestTileEntity {

  public ObsidianChestTileEntity() {
    super(IronChestsTileEntityTypes.OBSIDIAN_CHEST.get(), IronChestsTypes.OBSIDIAN, IronChestsBlocks.OBSIDIAN_CHEST::get);
  }

  @Override
  protected Container createMenu(int id, PlayerInventory playerInventory) {
    return IronChestContainer.createObsidianContainer(id, playerInventory, this);
  }
}
