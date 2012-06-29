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
package cpw.mods.ironchest;

import java.util.ArrayList;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemIronChest extends ItemBlock {

	public ItemIronChest(int id) {
		super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int i) {
	  return IronChestType.validateMeta(i);
	}
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return IronChestType.values()[itemstack.getItemDamage()].name();
	}

	@Override
	public void addCreativeItems(@SuppressWarnings("rawtypes") ArrayList itemList) {
	}
}
