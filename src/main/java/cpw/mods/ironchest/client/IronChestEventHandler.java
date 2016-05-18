package cpw.mods.ironchest.client;

import cpw.mods.ironchest.IronChestType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IronChestEventHandler
{
    public static IronChestEventHandler INSTANCE = new IronChestEventHandler();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPreTextureStiching(TextureStitchEvent.Pre event)
    {
        if (event.getMap() == FMLClientHandler.instance().getClient().getTextureMapBlocks())
        {
            for (IronChestType t : IronChestType.VALUES)
            {
                event.getMap().registerSprite(new ResourceLocation(t.getBreakTexture()));
            }
        }
    }
}
