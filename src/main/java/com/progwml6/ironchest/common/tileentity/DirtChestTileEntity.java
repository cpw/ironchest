/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package com.progwml6.ironchest.common.tileentity;

import com.progwml6.ironchest.common.blocks.ChestType;
import com.progwml6.ironchest.common.core.IronChestBlocks;
import com.progwml6.ironchest.common.inventory.ChestContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DirtChestTileEntity extends IronChestTileEntity
{
    private static ItemStack dirtChest9000GuideBook = new ItemStack(Items.WRITTEN_BOOK);

    private static boolean bookDataCreated = false;

    public DirtChestTileEntity()
    {
        super(ChestTileEntityType.DIRT_CHEST, ChestType.DIRTCHEST9000, IronChestBlocks.dirtChestBlock);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        return ChestContainer.createDirtContainer(id, playerInventory, this);
    }

    @Override
    public void wasPlaced(LivingEntity entityLivingBase, ItemStack itemStack)
    {
        if (!(itemStack.hasTag() && itemStack.getTag().getBoolean("been_placed")))
        {
            this.setInventorySlotContents(0, dirtChest9000GuideBook.copy());
        }

        if (!bookDataCreated)
        {
            createBookData();
        }
    }

    @Override
    public void removeAdornments()
    {
        if (!this.getItems().get(0).isEmpty() && this.getItems().get(0).isItemEqual(dirtChest9000GuideBook))
        {
            this.getItems().set(0, ItemStack.EMPTY);
        }
    }

    private static void createBookData()
    {
        dirtChest9000GuideBook.setTagInfo("author", new StringNBT("cpw"));

        dirtChest9000GuideBook.setTagInfo("title", new StringNBT(I18n.format("book.ironchest.dirtchest9000.title")));

        ListNBT pages = new ListNBT();
        pages.add(new StringNBT(ITextComponent.Serializer.toJson(new TranslationTextComponent("book.ironchest.dirtchest9000.page1"))));
        pages.add(new StringNBT(ITextComponent.Serializer.toJson(new TranslationTextComponent("book.ironchest.dirtchest9000.page2"))));
        pages.add(new StringNBT(ITextComponent.Serializer.toJson(new TranslationTextComponent("book.ironchest.dirtchest9000.page3"))));
        pages.add(new StringNBT(ITextComponent.Serializer.toJson(new TranslationTextComponent("book.ironchest.dirtchest9000.page4"))));
        pages.add(new StringNBT(ITextComponent.Serializer.toJson(new TranslationTextComponent("book.ironchest.dirtchest9000.page5"))));

        dirtChest9000GuideBook.setTagInfo("pages", pages);

        bookDataCreated = true;
    }
}
