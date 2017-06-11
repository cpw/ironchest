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
package cpw.mods.ironchest.common.gui.shulker.slot;

import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ValidatingShulkerBoxSlot extends Slot
{
    public ValidatingShulkerBoxSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        //@formatter:off
        return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockIronShulkerBox) && !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
        //@formatter:on
    }
}
