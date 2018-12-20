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

import cpw.mods.ironchest.client.gui.chest.GUIChest;
import cpw.mods.ironchest.client.gui.shulker.GUIShulkerChest;
import cpw.mods.ironchest.client.renderer.chest.TileEntityIronChestRenderer;
import cpw.mods.ironchest.client.renderer.shulker.TileEntityIronShulkerBoxRenderer;
import cpw.mods.ironchest.common.CommonProxy;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        for (IronChestType type : IronChestType.values())
        {
            if (type.clazz != null)
                ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new TileEntityIronChestRenderer());
        }

        for (IronShulkerBoxType type : IronShulkerBoxType.values())
        {
            if (type.clazz != null)
                ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new TileEntityIronShulkerBoxRenderer());
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
        else if (te != null && te instanceof TileEntityIronShulkerBox)
        {
            return GUIShulkerChest.GUI.buildGUI(IronShulkerBoxType.values()[ID], player.inventory, (TileEntityIronShulkerBox) te);
        }
        else
        {
            return null;
        }
    }

    @Override
    public World getClientWorld()
    {
        return Minecraft.getMinecraft().world;
    }
}
