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

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.client.gui.chest.GUIChest;
import cpw.mods.ironchest.client.gui.shulker.GUIShulkerChest;
import cpw.mods.ironchest.client.renderer.chest.TileEntityIronChestRenderer;
import cpw.mods.ironchest.client.renderer.shulker.TileEntityIronShulkerBoxRenderer;
import cpw.mods.ironchest.common.CommonProxy;
import cpw.mods.ironchest.common.ICContent;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
        // Chests Start
        Item chestItem = Item.getItemFromBlock(ICContent.ironChestBlock);

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
            ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "iron_chest_upgrades"), "variant=" + type.itemName.toLowerCase()));
            //@formatter:on
        }
        // Chests End

        // Shulkers Start
        for (Block shulker : ICContent.SHULKER_BLOCKS)
        {
            Item shulkerBoxItem = Item.getItemFromBlock(shulker);

            for (IronShulkerBoxType type : IronShulkerBoxType.values())
            {
                if (type != IronShulkerBoxType.VANILLA)
                {
                    //@formatter:off
                    ModelLoader.setCustomModelResourceLocation(shulkerBoxItem, type.ordinal(), new ModelResourceLocation(shulkerBoxItem.getRegistryName(), "variant=" + type.getName()));
                    //@formatter:on
                }
            }
        }

        for (IronShulkerBoxType type : IronShulkerBoxType.values())
        {
            ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new TileEntityIronShulkerBoxRenderer());
        }

        for (ShulkerBoxChangerType type : ShulkerBoxChangerType.VALUES)
        {
            //@formatter:off
            ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "iron_shulker_box_upgrades"), "variant=" + type.itemName.toLowerCase()));
            //@formatter:on
        }
        // Shulker End
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
