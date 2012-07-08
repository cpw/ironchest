package cpw.mods.ironchest.client;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public final class ItemWrap {
	private int index;

	private int damage;

	public ItemWrap(ItemStack itemstack) {
		this(itemstack.itemID, itemstack.getItemDamage());
	}

	public ItemWrap(int index, int damage) {
		this.index = index;
		this.damage = damage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == ItemWrap.class)
			return obj.hashCode() == hashCode();
		if (obj.getClass() == ItemStack.class)
			return ((ItemStack) obj).itemID == index && ((ItemStack) obj).getItemDamage() == damage;
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return (index << 16) + (damage + 0x7FFF);
	}
}
