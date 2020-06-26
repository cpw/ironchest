package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class GenericIronChestTileEntity extends LockableLootTileEntity implements IChestLid, ITickableTileEntity {

  private NonNullList<ItemStack> chestContents;
  protected float lidAngle;
  protected float prevLidAngle;
  protected int numPlayersUsing;
  private int ticksSinceSync;
  private IronChestsTypes chestType;
  private Supplier<Block> blockToUse;

  protected GenericIronChestTileEntity(TileEntityType<?> typeIn, IronChestsTypes chestTypeIn, Supplier<Block> blockToUseIn) {
    super(typeIn);

    this.chestContents = NonNullList.<ItemStack>withSize(chestTypeIn.size, ItemStack.EMPTY);
    this.chestType = chestTypeIn;
    this.blockToUse = blockToUseIn;
  }

  @Override
  public int getSizeInventory() {
    return this.getItems().size();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemstack : this.chestContents) {
      if (!itemstack.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Override
  protected ITextComponent getDefaultName() {
    return new TranslationTextComponent(IronChests.MODID + ".container."+ this.chestType.getId() + "_chest");
  }

  @Override
  public void func_230337_a_(BlockState state, CompoundNBT compound) {
    super.func_230337_a_(state, compound);

    this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

    if (!this.checkLootAndRead(compound)) {
      ItemStackHelper.loadAllItems(compound, this.chestContents);
    }
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);

    if (!this.checkLootAndWrite(compound)) {
      ItemStackHelper.saveAllItems(compound, this.chestContents);
    }

    return compound;
  }

  @Override
  public void tick() {
    int i = this.pos.getX();
    int j = this.pos.getY();
    int k = this.pos.getZ();
    ++this.ticksSinceSync;
    this.numPlayersUsing = getNumberOfPlayersUsing(this.world, this, this.ticksSinceSync, i, j, k, this.numPlayersUsing);
    this.prevLidAngle = this.lidAngle;
    float f = 0.1F;
    if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
      this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
    }

    if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
      float f1 = this.lidAngle;
      if (this.numPlayersUsing > 0) {
        this.lidAngle += 0.1F;
      }
      else {
        this.lidAngle -= 0.1F;
      }

      if (this.lidAngle > 1.0F) {
        this.lidAngle = 1.0F;
      }

      float f2 = 0.5F;
      if (this.lidAngle < 0.5F && f1 >= 0.5F) {
        this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
      }

      if (this.lidAngle < 0.0F) {
        this.lidAngle = 0.0F;
      }
    }
  }

  public static int getNumberOfPlayersUsing(World worldIn, LockableTileEntity lockableTileEntity, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
    if (!worldIn.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
      numPlayersUsing = getNumberOfPlayersUsing(worldIn, lockableTileEntity, x, y, z);
    }

    return numPlayersUsing;
  }

  public static int getNumberOfPlayersUsing(World world, LockableTileEntity lockableTileEntity, int x, int y, int z) {
    int i = 0;

    for (PlayerEntity playerentity : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((double) ((float) x - 5.0F), (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
      if (playerentity.openContainer instanceof IronChestContainer) {
        ++i;
      }
    }

    return i;
  }

  private void playSound(SoundEvent soundIn) {
    double d0 = (double) this.pos.getX() + 0.5D;
    double d1 = (double) this.pos.getY() + 0.5D;
    double d2 = (double) this.pos.getZ() + 0.5D;

    this.world.playSound((PlayerEntity) null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
  }

  @Override
  public boolean receiveClientEvent(int id, int type) {
    if (id == 1) {
      this.numPlayersUsing = type;
      return true;
    }
    else {
      return super.receiveClientEvent(id, type);
    }
  }

  @Override
  public void openInventory(PlayerEntity player) {
    if (!player.isSpectator()) {
      if (this.numPlayersUsing < 0) {
        this.numPlayersUsing = 0;
      }

      ++this.numPlayersUsing;
      this.onOpenOrClose();
    }
  }

  @Override
  public void closeInventory(PlayerEntity player) {
    if (!player.isSpectator()) {
      --this.numPlayersUsing;
      this.onOpenOrClose();
    }
  }

  protected void onOpenOrClose() {
    Block block = this.getBlockState().getBlock();

    if (block instanceof GenericIronChestBlock) {
      this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
      this.world.notifyNeighborsOfStateChange(this.pos, block);
    }
  }

  @Override
  public NonNullList<ItemStack> getItems() {
    return this.chestContents;
  }

  @Override
  public void setItems(NonNullList<ItemStack> itemsIn) {
    this.chestContents = NonNullList.<ItemStack>withSize(this.getChestType().size, ItemStack.EMPTY);

    for (int i = 0; i < itemsIn.size(); i++) {
      if (i < this.chestContents.size()) {
        this.getItems().set(i, itemsIn.get(i));
      }
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public float getLidAngle(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
  }

  public static int getPlayersUsing(IBlockReader reader, BlockPos posIn) {
    BlockState blockstate = reader.getBlockState(posIn);
    if (blockstate.hasTileEntity()) {
      TileEntity tileentity = reader.getTileEntity(posIn);
      if (tileentity instanceof GenericIronChestTileEntity) {
        return ((GenericIronChestTileEntity) tileentity).numPlayersUsing;
      }
    }

    return 0;
  }

  @Override
  protected Container createMenu(int windowId, PlayerInventory playerInventory) {
    return IronChestContainer.createIronContainer(windowId, playerInventory, this);
  }

  public void wasPlaced(LivingEntity livingEntity, ItemStack stack) {
  }

  public void removeAdornments() {
  }

  public IronChestsTypes getChestType() {
    IronChestsTypes type = IronChestsTypes.IRON;

    if (this.hasWorld()) {
      IronChestsTypes typeFromBlock = GenericIronChestBlock.getTypeFromBlock(this.getBlockState().getBlock());

      if (typeFromBlock != null) {
        type = typeFromBlock;
      }
    }

    return type;
  }

  public Block getBlockToUse() {
    return this.blockToUse.get();
  }
}
