package com.progwml6.ironchest.common.item;

import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ChestUpgradeItem extends Item {

  private final IronChestsUpgradeType type;

  public ChestUpgradeItem(IronChestsUpgradeType type, Properties properties) {
    super(properties);
    this.type = type;
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    //tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").applyTextStyle(TextFormatting.GRAY));
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
    PlayerEntity entityPlayer = context.getPlayer();
    BlockPos blockPos = context.getPos();
    World world = context.getWorld();
    ItemStack itemStack = context.getItem();

    if (world.isRemote) {
      return ActionResultType.PASS;
    }

    if (entityPlayer == null) {
      return ActionResultType.PASS;
    }

    if (this.type.canUpgrade(IronChestsTypes.WOOD)) {
      if (!(world.getBlockState(blockPos).getBlock() instanceof ChestBlock)) {
        return ActionResultType.PASS;
      }
    }
    else {
      if (world.getBlockState(blockPos).getBlock().getDefaultState() != IronChestsTypes.get(this.type.source).getDefaultState()) {
        return ActionResultType.PASS;
      }
    }

    TileEntity tileEntity = world.getTileEntity(blockPos);

    if (this.type.canUpgrade(IronChestsTypes.WOOD)) {
      if (!(tileEntity instanceof ChestTileEntity)) {
        return ActionResultType.PASS;
      }
    }

    GenericIronChestTileEntity newChest = null;
    ITextComponent customName = null;
    NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    Direction chestFacing = Direction.NORTH;

    if (tileEntity != null) {
      if (tileEntity instanceof GenericIronChestTileEntity) {
        GenericIronChestTileEntity chest = (GenericIronChestTileEntity) tileEntity;
        BlockState chestState = world.getBlockState(blockPos);

        if (GenericIronChestTileEntity.getPlayersUsing(world, blockPos) > 0) {
          return ActionResultType.PASS;
        }

        if (!chest.canOpen(entityPlayer)) {
          return ActionResultType.PASS;
        }

        chestContents = chest.getItems();
        chestFacing = chestState.get(GenericIronChestBlock.FACING);
        customName = chest.getCustomName();
        newChest = this.type.target.makeEntity();

        if (newChest == null) {
          return ActionResultType.PASS;
        }
      }
      else if (tileEntity instanceof ChestTileEntity) {
        BlockState chestState = world.getBlockState(blockPos);
        chestFacing = chestState.get(ChestBlock.FACING);
        ChestTileEntity chest = (ChestTileEntity) tileEntity;

        if (ChestTileEntity.getPlayersUsing(world, blockPos) > 0) {
          return ActionResultType.PASS;
        }

        if (!chest.canOpen(entityPlayer)) {
          return ActionResultType.PASS;
        }

        if (!this.type.canUpgrade(IronChestsTypes.WOOD)) {
          return ActionResultType.PASS;
        }

        chestContents = NonNullList.withSize(chest.getSizeInventory(), ItemStack.EMPTY);

        for (int slot = 0; slot < chestContents.size(); slot++) {
          chestContents.set(slot, chest.getStackInSlot(slot));
        }

        customName = chest.getCustomName();

        newChest = this.type.target.makeEntity();
      }
    }

    tileEntity.updateContainingBlockInfo();

    world.removeTileEntity(blockPos);
    world.removeBlock(blockPos, false);

    BlockState iBlockState = IronChestsTypes.get(this.type.target).getDefaultState().with(GenericIronChestBlock.FACING, chestFacing);

    world.setBlockState(blockPos, iBlockState, 3);
    world.setTileEntity(blockPos, newChest);

    world.notifyBlockUpdate(blockPos, iBlockState, iBlockState, 3);

    TileEntity tileEntity2 = world.getTileEntity(blockPos);

    if (tileEntity2 instanceof GenericIronChestTileEntity) {
      if (customName != null) {
        ((GenericIronChestTileEntity) tileEntity2).setCustomName(customName);
      }

      ((GenericIronChestTileEntity) tileEntity2).setItems(chestContents);
    }

    if (!entityPlayer.abilities.isCreativeMode) {
      itemStack.shrink(1);
    }

    return ActionResultType.SUCCESS;
  }

}
