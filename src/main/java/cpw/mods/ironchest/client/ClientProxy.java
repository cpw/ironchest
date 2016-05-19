/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.client;

import cpw.mods.ironchest.ChestChangerType;
import cpw.mods.ironchest.CommonProxy;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderInformation()
    {
        Item chestItem = Item.getItemFromBlock(IronChest.ironChestBlock);

        for (IronChestType type : IronChestType.values())
        {
            if (type != IronChestType.WOOD)
            {
                //@formatter:off
                ModelLoader.setCustomModelResourceLocation(chestItem, type.ordinal(), new ModelResourceLocation(chestItem.getRegistryName(), "variant=" + type.getName()));
                //@formatter:on
            }

            ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new TileEntityIronChestRenderer());
        }

        for (ChestChangerType type : ChestChangerType.VALUES)
        {
            //@formatter:off
            ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "ItemChestUpgrade"), "variant=" + type.itemName.toLowerCase()));
            //@formatter:on
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te != null && te instanceof TileEntityIronChest)
        {
            return GUIChest.GUI.buildGUI(IronChestType.values()[ID], player.inventory, (TileEntityIronChest) te);
        }
        else
        {
            return null;
        }
    }
}
