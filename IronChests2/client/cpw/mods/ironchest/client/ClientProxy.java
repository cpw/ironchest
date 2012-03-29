package cpw.mods.ironchest.client;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChestItemRenderHelper;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.MinecraftForgeClient;
import cpw.mods.ironchest.ChestChangerType;
import cpw.mods.ironchest.IProxy;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class ClientProxy implements IProxy {
  @Override
  public void registerRenderInformation() {
    ChestItemRenderHelper.instance = new IronChestRenderHelper();
    MinecraftForgeClient.preloadTexture("/cpw/mods/ironchest/sprites/block_textures.png");
    MinecraftForgeClient.preloadTexture("/cpw/mods/ironchest/sprites/item_textures.png");
  }

  @Override
  public void registerTileEntities() {
    for (IronChestType typ : IronChestType.values()) {
      ModLoader.registerTileEntity(typ.clazz, typ.name(), new TileEntityIronChestRenderer());
    }
  }

  @Override
  public void registerTranslations() {
    for (IronChestType typ : IronChestType.values()) {
      ModLoader.addLocalization(typ.name() + ".name", typ.friendlyName);
    }
    for (ChestChangerType typ : ChestChangerType.values()) {
      ModLoader.addLocalization("item." + typ.itemName + ".name", typ.descriptiveName);
    }
  }

  @Override
  public File getMinecraftDir() {
    return Minecraft.getMinecraftDir();
  }

  @Override
  public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound data) {
    entityitem.item.setTagCompound(data);

  }

  @Override
  public boolean isRemote() {
    return ModLoader.getMinecraftInstance().theWorld.isRemote;
  }

  @Override
  public GuiScreen getGuiScreen(int ID, EntityPlayerSP player, World world, int X, int Y, int Z) {
    TileEntity te = world.getBlockTileEntity(X, Y, Z);
    if (te != null && te instanceof TileEntityIronChest) {
      return GUIChest.GUI.buildGUI(IronChestType.values()[ID], player.inventory, (TileEntityIronChest) te);
    } else {
      return null;
    }
  }

  @Override
  public World getCurrentWorld() {
    return ModLoader.getMinecraftInstance().theWorld;
  }
}
