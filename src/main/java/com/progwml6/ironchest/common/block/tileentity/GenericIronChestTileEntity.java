package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(value = Dist.CLIENT, _interface = LidBlockEntity.class)
public class GenericIronChestTileEntity extends RandomizableContainerBlockEntity implements LidBlockEntity, TickableBlockEntity {

  private NonNullList<ItemStack> chestContents;
  protected float lidAngle;
  protected float prevLidAngle;
  protected int numPlayersUsing;
  private int ticksSinceSync;
  private IronChestsTypes chestType;
  private Supplier<Block> blockToUse;

  protected GenericIronChestTileEntity(BlockEntityType<?> typeIn, IronChestsTypes chestTypeIn, Supplier<Block> blockToUseIn) {
    super(typeIn);

    this.chestContents = NonNullList.<ItemStack>withSize(chestTypeIn.size, ItemStack.EMPTY);
    this.chestType = chestTypeIn;
    this.blockToUse = blockToUseIn;
  }

  @Override
  public int getContainerSize() {
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
  protected Component getDefaultName() {
    return new TranslatableComponent(IronChests.MODID + ".container." + this.chestType.getId() + "_chest");
  }

  @Override
  public void load(BlockState state, CompoundTag compound) {
    super.load(state, compound);

    this.chestContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

    if (!this.tryLoadLootTable(compound)) {
      ContainerHelper.loadAllItems(compound, this.chestContents);
    }
  }

  @Override
  public CompoundTag save(CompoundTag compound) {
    super.save(compound);

    if (!this.trySaveLootTable(compound)) {
      ContainerHelper.saveAllItems(compound, this.chestContents);
    }

    return compound;
  }

  @Override
  public void tick() {
    int i = this.worldPosition.getX();
    int j = this.worldPosition.getY();
    int k = this.worldPosition.getZ();
    ++this.ticksSinceSync;
    this.numPlayersUsing = getNumberOfPlayersUsing(this.level, this, this.ticksSinceSync, i, j, k, this.numPlayersUsing);
    this.prevLidAngle = this.lidAngle;
    float f = 0.1F;
    if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
      this.playSound(SoundEvents.CHEST_OPEN);
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
        this.playSound(SoundEvents.CHEST_CLOSE);
      }

      if (this.lidAngle < 0.0F) {
        this.lidAngle = 0.0F;
      }
    }
  }

  public static int getNumberOfPlayersUsing(Level worldIn, BaseContainerBlockEntity lockableTileEntity, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
    if (!worldIn.isClientSide && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
      numPlayersUsing = getNumberOfPlayersUsing(worldIn, lockableTileEntity, x, y, z);
    }

    return numPlayersUsing;
  }

  public static int getNumberOfPlayersUsing(Level world, BaseContainerBlockEntity lockableTileEntity, int x, int y, int z) {
    int i = 0;

    for (Player playerentity : world.getEntitiesOfClass(Player.class, new AABB((double) ((float) x - 5.0F), (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
      if (playerentity.containerMenu instanceof IronChestContainer) {
        ++i;
      }
    }

    return i;
  }

  private void playSound(SoundEvent soundIn) {
    double d0 = (double) this.worldPosition.getX() + 0.5D;
    double d1 = (double) this.worldPosition.getY() + 0.5D;
    double d2 = (double) this.worldPosition.getZ() + 0.5D;

    this.level.playSound((Player) null, d0, d1, d2, soundIn, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
  }

  @Override
  public boolean triggerEvent(int id, int type) {
    if (id == 1) {
      this.numPlayersUsing = type;
      return true;
    }
    else {
      return super.triggerEvent(id, type);
    }
  }

  @Override
  public void startOpen(Player player) {
    if (!player.isSpectator()) {
      if (this.numPlayersUsing < 0) {
        this.numPlayersUsing = 0;
      }

      ++this.numPlayersUsing;
      this.onOpenOrClose();
    }
  }

  @Override
  public void stopOpen(Player player) {
    if (!player.isSpectator()) {
      --this.numPlayersUsing;
      this.onOpenOrClose();
    }
  }

  protected void onOpenOrClose() {
    Block block = this.getBlockState().getBlock();

    if (block instanceof GenericIronChestBlock) {
      this.level.blockEvent(this.worldPosition, block, 1, this.numPlayersUsing);
      this.level.updateNeighborsAt(this.worldPosition, block);
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
  public float getOpenNess(float partialTicks) {
    return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
  }

  public static int getPlayersUsing(BlockGetter reader, BlockPos posIn) {
    BlockState blockstate = reader.getBlockState(posIn);
    if (blockstate.hasTileEntity()) {
      BlockEntity tileentity = reader.getBlockEntity(posIn);
      if (tileentity instanceof GenericIronChestTileEntity) {
        return ((GenericIronChestTileEntity) tileentity).numPlayersUsing;
      }
    }

    return 0;
  }

  @Override
  protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory) {
    return IronChestContainer.createIronContainer(windowId, playerInventory, this);
  }

  public void wasPlaced(LivingEntity livingEntity, ItemStack stack) {
  }

  public void removeAdornments() {
  }

  public IronChestsTypes getChestType() {
    IronChestsTypes type = IronChestsTypes.IRON;

    if (this.hasLevel()) {
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
