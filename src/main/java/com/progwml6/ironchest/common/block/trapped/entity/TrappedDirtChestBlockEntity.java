package com.progwml6.ironchest.common.block.trapped.entity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;

public class TrappedDirtChestBlockEntity extends AbstractTrappedIronChestBlockEntity {

  private static ItemStack DIRT_CHEST_BOOK = new ItemStack(Items.WRITTEN_BOOK);

  private static boolean bookDataCreated = false;

  public TrappedDirtChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(IronChestsBlockEntityTypes.TRAPPED_DIRT_CHEST.get(), blockPos, blockState, IronChestsTypes.DIRT, IronChestsBlocks.TRAPPED_DIRT_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return IronChestMenu.createDirtContainer(containerId, playerInventory, this);
  }

  @Override
  public void wasPlaced(@Nullable LivingEntity livingEntity, ItemStack itemStack) {
    if (!(itemStack.hasTag() && Objects.requireNonNull(itemStack.getTag()).getBoolean("been_placed"))) {
      //TODO FIX BOOK
      //this.setInventorySlotContents(0, DIRT_CHEST_BOOK.copy());
    }

    if (!bookDataCreated) {
      //createBookData();
    }
  }

  @Override
  public void removeAdornments() {
    if (!this.getItems().get(0).isEmpty() && this.getItems().get(0).sameItem(DIRT_CHEST_BOOK)) {
      this.getItems().set(0, ItemStack.EMPTY);
    }
  }

  private static void createBookData() {
    DIRT_CHEST_BOOK.addTagElement("author", StringTag.valueOf("cpw"));

    DIRT_CHEST_BOOK.addTagElement("title", StringTag.valueOf(I18n.get("book.ironchest.dirtchest9000.title")));

    ListTag pages = new ListTag();
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page1"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page2"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page3"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page4"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page5"))));

    DIRT_CHEST_BOOK.addTagElement("pages", pages);

    bookDataCreated = true;
  }
}
