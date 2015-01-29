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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemChestChanger extends Item 
{
    private ChestChangerType type;

    public ItemChestChanger(ChestChangerType type)
    {
        this.type = type;
        
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ironchest:" + type.name());
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if (world.isRemote) return false;
		TileEntity te = world.getTileEntity(pos);
		TileEntityIronChest newchest = new TileEntityIronChest();
		ItemStack[] chestContents = new ItemStack[27];
		if (te != null) {
			if (te instanceof TileEntityIronChest) {
				chestContents = ((TileEntityIronChest) te).chestContents;
				newchest = IronChestType.makeEntity(this.getTargetChestOrdinal(this.type.ordinal()));
				if (newchest == null) return false;
			} else if (te instanceof TileEntityChest) {
				if (((TileEntityChest) te).numPlayersUsing > 0) return false;
				if (!getType().canUpgrade(IronChestType.WOOD)) return false;
				chestContents = new ItemStack[((TileEntityChest) te).getSizeInventory()];
				for (int i = 0; i < chestContents.length; i ++) chestContents[i] = ((TileEntityChest) te).getStackInSlot(i);
				newchest = IronChestType.makeEntity(IronChestType.IRON.ordinal());
			}
		}
		
		te.updateContainingBlockInfo();
		if (te instanceof TileEntityChest) ((TileEntityChest) te).checkForAdjacentChests();
		
		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
		
		world.setTileEntity(pos, newchest);
		world.setBlockState(pos, IronChest.ironChestBlock.getStateFromMeta(newchest.getType().ordinal()), 3);
		
		world.markBlockForUpdate(pos);
		
		TileEntity te2 = world.getTileEntity(pos);
		if (te2 instanceof TileEntityIronChest) {
			((TileEntityIronChest) te2).setContents(chestContents);
		}

		stack.stackSize = player.capabilities.isCreativeMode ? stack.stackSize : stack.stackSize - 1;
		return true;
    }

    public int getTargetChestOrdinal(int sourceOrdinal)
    {
        return type.getTarget();
    }

    public ChestChangerType getType()
    {
        return type;
    }
}
