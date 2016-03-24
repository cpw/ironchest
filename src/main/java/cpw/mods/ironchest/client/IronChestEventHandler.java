package cpw.mods.ironchest.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IronChestEventHandler
{
    public static IronChestEventHandler INSTANCE = new IronChestEventHandler();

    @SubscribeEvent
    public void onPreTextureStiching(TextureStitchEvent.Pre event)
    {
        if (event.map == FMLClientHandler.instance().getClient().getTextureMapBlocks())
        {
            event.map.registerSprite(new ResourceLocation("ironchest:blocks/copperbreak"));
            event.map.registerSprite(new ResourceLocation("ironchest:blocks/crystalbreak"));
            event.map.registerSprite(new ResourceLocation("ironchest:blocks/diamondbreak"));
            event.map.registerSprite(new ResourceLocation("ironchest:blocks/goldbreak"));
            event.map.registerSprite(new ResourceLocation("ironchest:blocks/ironbreak"));
            event.map.registerSprite(new ResourceLocation("ironchest:blocks/silverbreak"));
        }
    }
}
