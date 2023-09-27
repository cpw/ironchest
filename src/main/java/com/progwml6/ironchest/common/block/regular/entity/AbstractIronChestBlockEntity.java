package com.progwml6.ironchest.common.block.regular.entity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class AbstractIronChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {

  private static final int EVENT_SET_OPEN_COUNT = 1;
  private NonNullList<ItemStack> items;

  private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
    protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
      AbstractIronChestBlockEntity.playSound(level, pos, blockState, SoundEvents.CHEST_OPEN);
    }

    protected void onClose(Level level, BlockPos pos, BlockState blockState) {
      AbstractIronChestBlockEntity.playSound(level, pos, blockState, SoundEvents.CHEST_CLOSE);
    }

    protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previousCount, int newCount) {
      AbstractIronChestBlockEntity.this.signalOpenCount(level, pos, blockState, previousCount, newCount);
    }

    protected boolean isOwnContainer(Player player) {
      if (!(player.containerMenu instanceof IronChestMenu)) {
        return false;
      } else {
        Container container = ((IronChestMenu) player.containerMenu).getContainer();
        return container instanceof AbstractIronChestBlockEntity || container instanceof CompoundContainer && ((CompoundContainer) container).contains(AbstractIronChestBlockEntity.this);
      }
    }
  };

  private final ChestLidController chestLidController = new ChestLidController();

  private final IronChestsTypes chestType;
  private final Supplier<Block> blockToUse;

  protected AbstractIronChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, IronChestsTypes chestTypeIn, Supplier<Block> blockToUseIn) {
    super(blockEntityType, blockPos, blockState);

    this.items = NonNullList.<ItemStack>withSize(chestTypeIn.size, ItemStack.EMPTY);
    this.chestType = chestTypeIn;
    this.blockToUse = blockToUseIn;
  }

  @Override
  public int getContainerSize() {
    return this.getItems().size();
  }

  @Override
  protected Component getDefaultName() {
    return new TranslatableComponent(IronChests.MOD_ID + ".container." + this.chestType.getId() + "_chest");
  }

  @Override
  public void load(CompoundTag compoundTag) {
    super.load(compoundTag);

    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

    if (!this.tryLoadLootTable(compoundTag)) {
      ContainerHelper.loadAllItems(compoundTag, this.items);
    }
  }

  @Override
  public CompoundTag save(CompoundTag compoundTag) {
    super.save(compoundTag);

    if (!this.trySaveLootTable(compoundTag)) {
      ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    return compoundTag;
  }

  public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, AbstractIronChestBlockEntity chestBlockEntity) {
    chestBlockEntity.chestLidController.tickLid();
  }

  static void playSound(Level level, BlockPos blockPos, BlockState blockState, SoundEvent soundEvent) {
    double d0 = (double) blockPos.getX() + 0.5D;
    double d1 = (double) blockPos.getY() + 0.5D;
    double d2 = (double) blockPos.getZ() + 0.5D;

    level.playSound((Player) null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
  }

  @Override
  public boolean triggerEvent(int id, int type) {
    if (id == 1) {
      this.chestLidController.shouldBeOpen(type > 0);
      return true;
    } else {
      return super.triggerEvent(id, type);
    }
  }

  @Override
  public void startOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  public void stopOpen(Player player) {
    if (!this.remove && !player.isSpectator()) {
      this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  public NonNullList<ItemStack> getItems() {
    return this.items;
  }

  @Override
  public void setItems(NonNullList<ItemStack> itemsIn) {
    this.items = NonNullList.<ItemStack>withSize(this.getChestType().size, ItemStack.EMPTY);

    for (int i = 0; i < itemsIn.size(); i++) {
      if (i < this.items.size()) {
        this.getItems().set(i, itemsIn.get(i));
      }
    }
  }

  @Override
  public float getOpenNess(float partialTicks) {
    return this.chestLidController.getOpenness(partialTicks);
  }

  public static int getOpenCount(BlockGetter blockGetter, BlockPos blockPos) {
    BlockState blockstate = blockGetter.getBlockState(blockPos);

    if (blockstate.hasBlockEntity()) {
      BlockEntity blockentity = blockGetter.getBlockEntity(blockPos);

      if (blockentity instanceof AbstractIronChestBlockEntity) {
        return ((AbstractIronChestBlockEntity) blockentity).openersCounter.getOpenerCount();
      }
    }

    return 0;
  }

  public void recheckOpen() {
    if (!this.remove) {
      this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int previousCount, int newCount) {
    Block block = blockState.getBlock();
    level.blockEvent(blockPos, block, 1, newCount);
  }

  public void wasPlaced(@Nullable LivingEntity livingEntity, ItemStack stack) {
  }

  public void removeAdornments() {
  }

  public IronChestsTypes getChestType() {
    IronChestsTypes type = IronChestsTypes.IRON;

    if (this.hasLevel()) {
      IronChestsTypes typeFromBlock = AbstractIronChestBlock.getTypeFromBlock(this.getBlockState().getBlock());

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
