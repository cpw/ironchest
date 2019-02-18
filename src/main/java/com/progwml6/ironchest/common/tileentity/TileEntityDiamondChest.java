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

import com.progwml6.ironchest.client.gui.GUIChest;
import com.progwml6.ironchest.common.blocks.IronChestType;
import com.progwml6.ironchest.common.core.IronChestBlocks;

public class TileEntityDiamondChest extends TileEntityIronChest
{
    public TileEntityDiamondChest()
    {
        super(IronChestEntityType.DIAMOND_CHEST, IronChestType.DIAMOND, IronChestBlocks.diamondChestBlock);
    }

    @Override
    public String getGuiID()
    {
        return GUIChest.GUI.DIAMOND.getGuiId().toString();
    }
}
