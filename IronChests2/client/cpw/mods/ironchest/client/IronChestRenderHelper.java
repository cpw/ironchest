/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.client;

import net.minecraft.src.Block;
import net.minecraft.src.ChestItemRenderHelper;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.mod_IronChest;

public class IronChestRenderHelper extends ChestItemRenderHelper {
	@Override
	public void func_35609_a(Block block, int i, float f) {
		if (block==mod_IronChest.ironChestBlock) {
			TileEntityRenderer.instance.renderTileEntityAt(block.getTileEntity(i), 0.0D, 0.0D, 0.0D, 0.0F);
		} else {
			super.func_35609_a(block, i, f);
		}
	}
}
