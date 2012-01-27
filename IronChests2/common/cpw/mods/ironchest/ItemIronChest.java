package cpw.mods.ironchest;

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
		if (i<IronChestType.values().length) {
			return i;
		} else {
			return 0;
		}
	}
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return IronChestType.values()[itemstack.getItemDamage()].name();
	}
}
