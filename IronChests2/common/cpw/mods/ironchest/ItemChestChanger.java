package cpw.mods.ironchest;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;

public class ItemChestChanger extends Item implements ITextureProvider {

	private ChestChangerType type;

	public ItemChestChanger(int id, ChestChangerType type) {
		super(id);
		setMaxStackSize(1);
		this.type=type;
		setIconIndex(type.ordinal());
		setItemName(type.itemName);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side) {
		TileEntity te=world.getBlockTileEntity(X,Y,Z);
		if (te!=null && te instanceof TileEntityIronChest) {
			TileEntityIronChest ironchest=(TileEntityIronChest)te;
			TileEntityIronChest newchest=ironchest.applyUpgradeItem(this);
			if (newchest==null) {
				return false;
			}
			world.setBlockTileEntity(X, Y, Z, newchest);
			world.setBlockMetadataWithNotify(X, Y, Z, newchest.getType().ordinal());
			world.notifyBlocksOfNeighborChange(X, Y, Z, world.getBlockId(X, Y, Z));
			world.markBlockNeedsUpdate(X, Y, Z);
			stack.stackSize=0;
			return true;
		} else {
			return false;
		}
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
