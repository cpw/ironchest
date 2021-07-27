package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import com.progwml6.ironchest.common.block.tileentity.ObsidianChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.SilverChestTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ObsidianChestBlock extends GenericIronChestBlock {

  public ObsidianChestBlock(Properties properties) {
    super(IronChestsTypes.OBSIDIAN, IronChestsTileEntityTypes.OBSIDIAN_CHEST::get, properties);
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new ObsidianChestTileEntity();
  }
}
