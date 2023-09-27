package com.progwml6.ironchest.common.item;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.block.trapped.entity.AbstractTrappedIronChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class ChestUpgradeItem extends Item {

  private final IronChestsUpgradeType type;

  public ChestUpgradeItem(IronChestsUpgradeType type, Properties properties) {
    super(properties);
    this.type = type;
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
    Player entityPlayer = context.getPlayer();
    BlockPos blockPos = context.getClickedPos();
    Level world = context.getLevel();
    ItemStack itemStack = context.getItemInHand();
    boolean passed = false;

    if (world.isClientSide) {
      return InteractionResult.PASS;
    }

    if (entityPlayer == null) {
      return InteractionResult.PASS;
    }

    if (this.type.canUpgrade(IronChestsTypes.WOOD)) {
      if (!(world.getBlockState(blockPos).getBlock() instanceof ChestBlock)) {
        return InteractionResult.PASS;
      }
    } else {
      for (Block block : IronChestsTypes.get(this.type.source)) {
        if (!passed)
          passed = world.getBlockState(blockPos).getBlock().defaultBlockState() == block.defaultBlockState();
      }
    }

    if (!passed) {
      return InteractionResult.PASS;
    }


    BlockEntity tileEntity = world.getBlockEntity(blockPos);

    if (this.type.canUpgrade(IronChestsTypes.WOOD)) {
      if (!(tileEntity instanceof ChestBlockEntity)) {
        return InteractionResult.PASS;
      }
    }

    AbstractIronChestBlockEntity newChest = null;
    Component customName = null;
    NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    Direction chestFacing = Direction.NORTH;
    BlockState iBlockState = IronChestsBlocks.COPPER_CHEST.get().defaultBlockState();

    if (tileEntity != null) {
      if (tileEntity instanceof AbstractIronChestBlockEntity chest) {
        BlockState chestState = world.getBlockState(blockPos);

        if (AbstractIronChestBlockEntity.getOpenCount(world, blockPos) > 0) {
          return InteractionResult.PASS;
        }

        if (!chest.canOpen(entityPlayer)) {
          return InteractionResult.PASS;
        }

        boolean trapped = tileEntity instanceof AbstractTrappedIronChestBlockEntity;

        if(trapped) {
          iBlockState = IronChestsTypes.get(this.type.target).get(1).defaultBlockState();
        } else {
          iBlockState = IronChestsTypes.get(this.type.target).get(0).defaultBlockState();
        }

        chestContents = chest.getItems();
        chestFacing = chestState.getValue(AbstractIronChestBlock.FACING);
        customName = chest.getCustomName();
        iBlockState = iBlockState.setValue(AbstractIronChestBlock.FACING, chestFacing);

        newChest = this.type.target.makeEntity(blockPos, iBlockState, trapped);
      } else if (tileEntity instanceof ChestBlockEntity chest) {
        BlockState chestState = world.getBlockState(blockPos);
        chestFacing = chestState.getValue(ChestBlock.FACING);

        if (ChestBlockEntity.getOpenCount(world, blockPos) > 0) {
          return InteractionResult.PASS;
        }

        if (!chest.canOpen(entityPlayer)) {
          return InteractionResult.PASS;
        }

        if (!this.type.canUpgrade(IronChestsTypes.WOOD)) {
          return InteractionResult.PASS;
        }

        chestContents = NonNullList.withSize(chest.getContainerSize(), ItemStack.EMPTY);

        for (int slot = 0; slot < chestContents.size(); slot++) {
          chestContents.set(slot, chest.getItem(slot));
        }

        customName = chest.getCustomName();

        boolean trapped = tileEntity instanceof TrappedChestBlockEntity;

        if(trapped) {
          iBlockState = IronChestsTypes.get(this.type.source).get(1).defaultBlockState();
        } else {
          iBlockState = IronChestsTypes.get(this.type.source).get(0).defaultBlockState();
        }

        iBlockState = iBlockState.setValue(AbstractIronChestBlock.FACING, chestFacing);

        newChest = this.type.target.makeEntity(blockPos, iBlockState, trapped);
      }
    }

    if (newChest == null) {
      return InteractionResult.PASS;
    }

    world.removeBlockEntity(blockPos);
    world.removeBlock(blockPos, false);

    world.setBlock(blockPos, iBlockState, 3);
    world.setBlockEntity(newChest);

    world.sendBlockUpdated(blockPos, iBlockState, iBlockState, 3);

    BlockEntity tileEntity2 = world.getBlockEntity(blockPos);

    if (tileEntity2 instanceof AbstractIronChestBlockEntity) {
      if (customName != null) {
        ((AbstractIronChestBlockEntity) tileEntity2).setCustomName(customName);
      }

      ((AbstractIronChestBlockEntity) tileEntity2).setItems(chestContents);
    }

    if (!entityPlayer.getAbilities().instabuild) {
      itemStack.shrink(1);
    }

    return InteractionResult.SUCCESS;
  }

}
