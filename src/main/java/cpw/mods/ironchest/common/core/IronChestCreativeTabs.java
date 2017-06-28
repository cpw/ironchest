package cpw.mods.ironchest.common.core;

import javax.annotation.Nonnull;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.util.CreativeTabItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class IronChestCreativeTabs
{
    private IronChestCreativeTabs()
    {
    }

    public static final CreativeTabs tabIronChests = new CreativeTabs("ironchest")
    {
        @Override
        @Nonnull
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Item.getItemFromBlock(IronChestBlocks.ironChestBlock), 1, IronChestType.IRON.ordinal());
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> listIn)
        {
            CreativeTabItems.getSubItems(listIn);
        }
    };
}
