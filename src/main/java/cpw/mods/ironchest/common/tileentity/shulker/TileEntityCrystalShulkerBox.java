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
package cpw.mods.ironchest.common.tileentity.shulker;

import javax.annotation.Nullable;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import net.minecraft.item.EnumDyeColor;

public class TileEntityCrystalShulkerBox extends TileEntityIronShulkerBox
{
    public TileEntityCrystalShulkerBox()
    {
        this(null);
    }

    public TileEntityCrystalShulkerBox(@Nullable EnumDyeColor colorIn)
    {
        super(colorIn, IronShulkerBoxType.CRYSTAL);
    }
}
