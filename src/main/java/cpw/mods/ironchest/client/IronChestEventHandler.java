package cpw.mods.ironchest.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

public class IronChestEventHandler
{
    public static IronChestEventHandler INSTANCE = new IronChestEventHandler();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPreTextureStiching(TextureStitchEvent.Pre event)
    {
        if (event.getMap() == FMLClientHandler.instance().getClient().getTextureMapBlocks())
        {
            event.getMap().registerSprite(new ResourceLocation("ironchest:blocks/copperbreak"));
            event.getMap().registerSprite(new ResourceLocation("ironchest:blocks/crystalbreak"));
            event.getMap().registerSprite(new ResourceLocation("ironchest:blocks/diamondbreak"));
            event.getMap().registerSprite(new ResourceLocation("ironchest:blocks/goldbreak"));
            event.getMap().registerSprite(new ResourceLocation("ironchest:blocks/ironbreak"));
            event.getMap().registerSprite(new ResourceLocation("ironchest:blocks/silverbreak"));
        }
    }
}
