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
