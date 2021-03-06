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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import cpw.mods.ironchest.CommonProxy;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderInformation()
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().registerBuiltInBlocks(IronChest.ironChestBlock);

        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        for (IronChestType chestType : IronChestType.values())
        {
            if (chestType != IronChestType.WOOD)
            {
                Item chestItem = Item.getItemFromBlock(IronChest.ironChestBlock);
                mesher.register(chestItem, chestType.ordinal(), new ModelResourceLocation("ironchest:chest_" + chestType.getName().toLowerCase(), "inventory"));
                ModelBakery.addVariantName(chestItem, "ironchest:chest_" + chestType.getName().toLowerCase());
            }
        }
    }

    @Override
    public <T extends TileEntityIronChest> void registerTileEntitySpecialRenderer(Class<T> type)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(type, new TileEntityIronChestRenderer<T>(type));
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te != null && te instanceof TileEntityIronChest)
        {
            return GUIChest.GUI.buildGUI(IronChestType.values()[ID], player.inventory, (TileEntityIronChest) te);
        } else
        {
            return null;
        }
    }
}
