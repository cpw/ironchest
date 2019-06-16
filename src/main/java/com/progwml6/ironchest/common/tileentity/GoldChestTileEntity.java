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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class GoldChestTileEntity extends IronChestTileEntity
{
    public GoldChestTileEntity()
    {
        super(ChestTileEntityType.GOLD_CHEST, ChestType.GOLD, IronChestBlocks.goldChestBlock);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        return ChestContainer.createGoldContainer(id, playerInventory, this);
    }
}
