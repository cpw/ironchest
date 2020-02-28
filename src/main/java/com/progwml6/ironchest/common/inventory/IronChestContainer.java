package com.progwml6.ironchest.common.inventory;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IronChestContainer extends Container {

  private final IInventory inventory;

  private final IronChestsTypes chestType;

  private IronChestContainer(ContainerType<?> containerType, int windowId, PlayerInventory playerInventory) {
    this(containerType, windowId, playerInventory, new Inventory(IronChestsTypes.WOOD.size), IronChestsTypes.WOOD);
  }

  public static IronChestContainer createIronContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.IRON_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.IRON.size), IronChestsTypes.IRON);
  }

  public static IronChestContainer createIronContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.IRON_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.IRON);
  }

  public static IronChestContainer createGoldContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.GOLD_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.GOLD.size), IronChestsTypes.GOLD);
  }

  public static IronChestContainer createGoldContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.GOLD_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.GOLD);
  }

  public static IronChestContainer createDiamondContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.DIAMOND_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.DIAMOND.size), IronChestsTypes.DIAMOND);
  }

  public static IronChestContainer createDiamondContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.DIAMOND_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.DIAMOND);
  }

  public static IronChestContainer createCrystalContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.CRYSTAL_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.CRYSTAL.size), IronChestsTypes.CRYSTAL);
  }

  public static IronChestContainer createCrystalContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.CRYSTAL_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.CRYSTAL);
  }

  public static IronChestContainer createCopperContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.COPPER_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.COPPER.size), IronChestsTypes.COPPER);
  }

  public static IronChestContainer createCopperContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.COPPER_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.COPPER);
  }

  public static IronChestContainer createSilverContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.SILVER_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.CRYSTAL.size), IronChestsTypes.SILVER);
  }

  public static IronChestContainer createSilverContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.SILVER_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.SILVER);
  }

  public static IronChestContainer createObsidianContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.OBSIDIAN_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.OBSIDIAN.size), IronChestsTypes.OBSIDIAN);
  }

  public static IronChestContainer createObsidianContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.OBSIDIAN_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.OBSIDIAN);
  }

  public static IronChestContainer createDirtContainer(int windowId, PlayerInventory playerInventory) {
    return new IronChestContainer(IronChestsContainerTypes.DIRT_CHEST.get(), windowId, playerInventory, new Inventory(IronChestsTypes.DIRT.size), IronChestsTypes.DIRT);
  }

  public static IronChestContainer createDirtContainer(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    return new IronChestContainer(IronChestsContainerTypes.DIRT_CHEST.get(), windowId, playerInventory, inventory, IronChestsTypes.DIRT);
  }

  public IronChestContainer(ContainerType<?> containerType, int windowId, PlayerInventory playerInventory, IInventory inventory, IronChestsTypes chestType) {
    super(containerType, windowId);
    assertInventorySize(inventory, chestType.size);

    this.inventory = inventory;
    this.chestType = chestType;

    inventory.openInventory(playerInventory.player);

    if (chestType == IronChestsTypes.DIRT) {
      this.addSlot(new DirtChestSlot(inventory, 0, 12 + 4 * 18, 8 + 2 * 18));
    }
    else {
      for (int chestRow = 0; chestRow < chestType.getRowCount(); chestRow++) {
        for (int chestCol = 0; chestCol < chestType.rowLength; chestCol++) {
          this.addSlot(new Slot(inventory, chestCol + chestRow * chestType.rowLength, 12 + chestCol * 18, 18 + chestRow * 18));
        }
      }
    }

    int leftCol = (chestType.xSize - 162) / 2 + 1;

    for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
      for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
        this.addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, chestType.ySize - (4 - playerInvRow) * 18 - 10));
      }

    }

    for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
      this.addSlot(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, chestType.ySize - 24));
    }
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return this.inventory.isUsableByPlayer(playerIn);
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index < this.chestType.size) {
        if (!this.mergeItemStack(itemstack1, this.chestType.size, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      }
      else if (!this.mergeItemStack(itemstack1, 0, this.chestType.size, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      }
      else {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }

  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
    super.onContainerClosed(playerIn);
    this.inventory.closeInventory(playerIn);
  }

  @OnlyIn(Dist.CLIENT)
  public IronChestsTypes getChestType() {
    return this.chestType;
  }
}