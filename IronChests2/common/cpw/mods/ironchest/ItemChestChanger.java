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

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;

public class ItemChestChanger extends Item {

	private ChestChangerType type;

	public ItemChestChanger(int id, ChestChangerType type) {
		super(id);
		setMaxStackSize(1);
		this.type=type;
		setIconIndex(type.ordinal());
		setItemName(type.itemName);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side) {
		if (world.isRemote)
			return false;
		TileEntity te=world.getBlockTileEntity(X,Y,Z);
		TileEntityIronChest newchest;
		if (te!=null && te instanceof TileEntityIronChest) {
			TileEntityIronChest ironchest=(TileEntityIronChest)te;
			newchest=ironchest.applyUpgradeItem(this);
			if (newchest==null) {
				return false;
			}
		} else if (te!=null && te instanceof TileEntityChest) {
		  TileEntityChest tec = (TileEntityChest) te;
	    if (tec.numUsingPlayers > 0) {
	      return false;
	    }
	    if (!getType().canUpgrade(IronChestType.WOOD)) {
	      return false;
	    }
	    // Force old TE out of the world so that adjacent chests can update
	    newchest = IronChestType.makeEntity(getTargetChestOrdinal(IronChestType.WOOD.ordinal()));
	    int newSize = newchest.chestContents.length;
	    ItemStack[] chestContents = ObfuscationReflectionHelper.getPrivateValue(TileEntityChest.class, tec, 0);
	    System.arraycopy(chestContents, 0, newchest.chestContents, 0, Math.min(newSize, chestContents.length));
	    BlockIronChest block = IronChest.ironChestBlock;
	    block.dropContent(newSize, tec, world, tec.xCoord, tec.yCoord, tec.zCoord);
	    newchest.setFacing((byte)tec.getBlockMetadata());
	    newchest.sortTopStacks();
	    for (int i = 0; i< Math.min(newSize, chestContents.length); i++)
	    {
	      chestContents[i]=null;
	    }
	    // Clear the old block out
      world.setBlock(X, Y, Z, 0);
      // Force the Chest TE to reset it's knowledge of neighbouring blocks
      tec.updateContainingBlockInfo();
      // Force the Chest TE to update any neighbours so they update next tick
      tec.checkForAdjacentChests();
      // And put in our block instead
	    world.setBlock(X, Y, Z, block.blockID);
		} else {
			return false;
		}
    world.setBlockTileEntity(X, Y, Z, newchest);
    world.setBlockMetadataWithNotify(X, Y, Z, newchest.getType().ordinal());
    world.notifyBlocksOfNeighborChange(X, Y, Z, world.getBlockId(X, Y, Z));
    world.markBlockNeedsUpdate(X, Y, Z);
    world.markBlocksDirty(X, Y, Z, X, Y, Z);
    stack.stackSize=0;
    return true;
	}

	@Override
	public String getTextureFile() {
		return "/cpw/mods/ironchest/sprites/item_textures.png";
	}

	public int getTargetChestOrdinal(int sourceOrdinal) {
		return type.getTarget();
	}

	public ChestChangerType getType() {
		return type;
	}
}
