package cpw.mods.ironchest;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;

public class TileEntityDirtChest extends TileEntityIronChest {
    private static ItemStack dirtChest9000GuideBook = new ItemStack(Item.writtenBook);
    static {
        dirtChest9000GuideBook.setTagInfo("author", new NBTTagString("author", "cpw"));
        dirtChest9000GuideBook.setTagInfo("title", new NBTTagString("title", StatCollector.translateToLocal("book.ironchest:dirtchest9000.title")));
        NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString("1", StatCollector.translateToLocal("book.ironchest:dirtchest9000.page1")));
        pages.appendTag(new NBTTagString("2", StatCollector.translateToLocal("book.ironchest:dirtchest9000.page2")));
        pages.appendTag(new NBTTagString("3", StatCollector.translateToLocal("book.ironchest:dirtchest9000.page3")));
        pages.appendTag(new NBTTagString("4", StatCollector.translateToLocal("book.ironchest:dirtchest9000.page4")));
        pages.appendTag(new NBTTagString("5", StatCollector.translateToLocal("book.ironchest:dirtchest9000.page5")));
        dirtChest9000GuideBook.setTagInfo("pages", pages);
    }
    public TileEntityDirtChest() {
        super(IronChestType.DIRTCHEST9000);
    }
    
    @Override
    public void wasPlaced(EntityLivingBase entityliving, ItemStack itemStack)
    {
        if (!(itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("dirtchest"))) {
            setInventorySlotContents(0, dirtChest9000GuideBook.copy());
        }
    }
    
    @Override
    public void removeAdornments()
    {
        if (chestContents[0] != null && chestContents[0].isItemEqual(dirtChest9000GuideBook)) {
            chestContents[0] = null;
        }
    }
}
