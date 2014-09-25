package cpw.mods.ironchest.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHelper 
{
    public static void loadInventoryModel(Item item, int metadata, String itemName)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().loadItemModel(item, metadata, new ModelResourceLocation(itemName, "inventory"));
    }

    public static void loadInventoryModel(Block block, int metadata, String blockName)
    {
        loadInventoryModel(Item.getItemFromBlock(block), metadata, blockName);
    }

    public static void loadInventoryModel(Block block, String blockName)
    {
        loadInventoryModel(block, 0, blockName);
    }

    public static void loadInventoryModel(Item item, String itemName)
    {
        loadInventoryModel(item, 0, itemName);
    }
}
