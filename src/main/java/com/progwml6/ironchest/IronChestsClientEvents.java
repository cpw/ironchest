package com.progwml6.ironchest;

import com.progwml6.ironchest.client.render.IronChestRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IronChests.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IronChestsClientEvents {

  public static final ModelLayerLocation IRON_CHEST = new ModelLayerLocation(new ResourceLocation(IronChests.MOD_ID, "iron_chest"), "main");

  @SubscribeEvent
  public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(IRON_CHEST, IronChestRenderer::createBodyLayer);
  }
}
