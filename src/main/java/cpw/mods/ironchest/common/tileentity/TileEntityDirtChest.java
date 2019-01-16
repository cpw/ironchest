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
package cpw.mods.ironchest.common.tileentity;

import cpw.mods.ironchest.common.blocks.IronChestType;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityDirtChest extends TileEntityIronChest
{
    private static ItemStack dirtChest9000GuideBook = new ItemStack(Items.WRITTEN_BOOK);

    private static boolean bookDataCreated = false;

    public TileEntityDirtChest()
    {
        super(IronChestEntityType.DIRT_CHEST, IronChestType.DIRTCHEST9000, IronChestBlocks.dirtChestBlock);
    }

    @Override
    public void wasPlaced(EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        if (!(itemStack.hasTag() && itemStack.getTag().getBoolean("dirtchest")))
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

    public static void createBookData()
    {
        dirtChest9000GuideBook.setTagInfo("author", new NBTTagString("cpw"));

        dirtChest9000GuideBook.setTagInfo("title", new NBTTagString(I18n.format("book.ironchest.dirtchest9000.title")));

        NBTTagList pages = new NBTTagList();
        pages.add(new NBTTagString(ITextComponent.Serializer.toJson(new TextComponentString(I18n.format("book.ironchest.dirtchest9000.page1")))));
        pages.add(new NBTTagString(ITextComponent.Serializer.toJson(new TextComponentString(I18n.format("book.ironchest.dirtchest9000.page2")))));
        pages.add(new NBTTagString(ITextComponent.Serializer.toJson(new TextComponentString(I18n.format("book.ironchest.dirtchest9000.page3")))));
        pages.add(new NBTTagString(ITextComponent.Serializer.toJson(new TextComponentString(I18n.format("book.ironchest.dirtchest9000.page4")))));
        pages.add(new NBTTagString(ITextComponent.Serializer.toJson(new TextComponentString(I18n.format("book.ironchest.dirtchest9000.page5")))));

        dirtChest9000GuideBook.setTagInfo("pages", pages);

        bookDataCreated = true;
    }
}
