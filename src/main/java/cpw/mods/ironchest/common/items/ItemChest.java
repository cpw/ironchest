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
package cpw.mods.ironchest.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemChest extends ItemBlock
{
    public ItemChest(Block block, Builder builder)
    {
        super(block, builder);

        this.setRegistryName(block.getRegistryName());
    }
}
