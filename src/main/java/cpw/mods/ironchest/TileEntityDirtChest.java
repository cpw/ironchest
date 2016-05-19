package cpw.mods.ironchest;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class TileEntityDirtChest extends TileEntityIronChest
{
    private static ItemStack dirtChest9000GuideBook = new ItemStack(Items.WRITTEN_BOOK);

    static
    {
        dirtChest9000GuideBook.setTagInfo("author", new NBTTagString("cpw"));
        dirtChest9000GuideBook.setTagInfo("title", new NBTTagString(I18n.translateToLocal("book.ironchest:dirtchest9000.title")));
        NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.ironchest:dirtchest9000.page1")));
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.ironchest:dirtchest9000.page2")));
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.ironchest:dirtchest9000.page3")));
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.ironchest:dirtchest9000.page4")));
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.ironchest:dirtchest9000.page5")));
        dirtChest9000GuideBook.setTagInfo("pages", pages);
    }

    public TileEntityDirtChest()
    {
        super(IronChestType.DIRTCHEST9000);
    }

    @Override
    public void wasPlaced(EntityLivingBase entityliving, ItemStack itemStack)
    {
        if (!(itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("dirtchest")))
        {
            this.setInventorySlotContents(0, dirtChest9000GuideBook.copy());
        }
    }

    @Override
    public void removeAdornments()
    {
        if (this.chestContents[0] != null && this.chestContents[0].isItemEqual(dirtChest9000GuideBook))
        {
            this.chestContents[0] = null;
        }
    }
}