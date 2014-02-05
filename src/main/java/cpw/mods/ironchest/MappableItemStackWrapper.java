package cpw.mods.ironchest;

import net.minecraft.item.ItemStack;

public class MappableItemStackWrapper {
    private ItemStack wrap;

    public MappableItemStackWrapper(ItemStack toWrap)
    {
        wrap = toWrap;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof MappableItemStackWrapper)) return false;
        MappableItemStackWrapper isw = (MappableItemStackWrapper) obj;
        if (wrap.getHasSubtypes())
        {
            return isw.wrap.isItemEqual(wrap);
        }
        else
        {
            return isw.wrap == wrap;
        }
    }

    @Override
    public int hashCode()
    {
        return System.identityHashCode(wrap);
    }
}
