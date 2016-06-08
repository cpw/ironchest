package cpw.mods.ironchest;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ValidatingSlot extends Slot
{
    private IronChestType type;

    public ValidatingSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition, IronChestType type)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.type = type;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return this.type.acceptsStack(par1ItemStack);
    }
}
