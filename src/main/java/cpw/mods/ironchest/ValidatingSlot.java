package cpw.mods.ironchest;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ValidatingSlot extends Slot {
    private IronChestType type;

    public ValidatingSlot(IInventory par1iInventory, int par2, int par3, int par4, IronChestType type)
    {
        super(par1iInventory, par2, par3, par4);
        this.type = type;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return type.acceptsStack(par1ItemStack);
    }
}
