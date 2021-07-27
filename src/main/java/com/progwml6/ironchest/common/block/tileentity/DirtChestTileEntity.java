package com.progwml6.ironchest.common.block.tileentity;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class DirtChestTileEntity extends GenericIronChestTileEntity {

  private static ItemStack dirtChest9000GuideBook = new ItemStack(Items.WRITTEN_BOOK);

  private static boolean bookDataCreated = false;

  public DirtChestTileEntity() {
    super(IronChestsTileEntityTypes.DIRT_CHEST.get(), IronChestsTypes.DIRT, IronChestsBlocks.DIRT_CHEST::get);

    if (!bookDataCreated) {
      //createBookData();
    }
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
    return IronChestContainer.createDirtContainer(id, playerInventory, this);
  }

  @Override
  public void wasPlaced(LivingEntity entityLivingBase, ItemStack itemStack) {
    if (!(itemStack.hasTag() && itemStack.getTag().getBoolean("been_placed"))) {
      //TODO FIX BOOK
      //this.setInventorySlotContents(0, dirtChest9000GuideBook.copy());
    }

    if (!bookDataCreated) {
      //createBookData();
    }
  }

  @Override
  public void removeAdornments() {
    if (!this.getItems().get(0).isEmpty() && this.getItems().get(0).sameItem(dirtChest9000GuideBook)) {
      this.getItems().set(0, ItemStack.EMPTY);
    }
  }

  private static void createBookData() {
    dirtChest9000GuideBook.addTagElement("author", StringTag.valueOf("cpw"));

    dirtChest9000GuideBook.addTagElement("title", StringTag.valueOf(I18n.get("book.ironchest.dirtchest9000.title")));

    ListTag pages = new ListTag();
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page1"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page2"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page3"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page4"))));
    pages.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("book.ironchest.dirtchest9000.page5"))));

    dirtChest9000GuideBook.addTagElement("pages", pages);

    bookDataCreated = true;
  }
}
