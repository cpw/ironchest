// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package cpw.mods.ironchest;

import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;

public class ContainerGoldChest extends IronChestContainer
{

    private static final int NUM_ROWS = 9;
	private static final int ROW_LENGTH = 9;
	
	public ContainerGoldChest(IInventory playerInventory, IInventory chestInventory) {
		super(playerInventory,chestInventory);
	}

	@Override
	protected int getRowLength() {
		return ROW_LENGTH;
	}
	
	protected void layoutContainer(IInventory playerInventory, IInventory chestInventory) {
		for(int i = 0; i < NUM_ROWS; i++)
        {
            for(int l = 0; l < NUM_ROWS; l++)
            {
                addSlot(new Slot(chestInventory, l + i * ROW_LENGTH, 12 + l * 18, 8 + i * 18));
            }

        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(playerInventory, i1 + j * 9 + 9, 12 + i1 * 18, 174 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(playerInventory, k, 12 + k * 18, 232));
        }
	}

}
