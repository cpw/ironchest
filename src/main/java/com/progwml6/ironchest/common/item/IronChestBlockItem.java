package com.progwml6.ironchest.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IronChestBlockItem extends BlockItem {

  protected Supplier<BlockEntityWithoutLevelRenderer> itemRenderer;

  public IronChestBlockItem(Block block, Properties properties, Supplier<Callable<BlockEntityWithoutLevelRenderer>> itemRenderer) {
    super(block, properties);

    BlockEntityWithoutLevelRenderer tmp = net.minecraftforge.fml.DistExecutor.callWhenOn(Dist.CLIENT, itemRenderer);
    this.itemRenderer = tmp == null ? null : () -> tmp;
  }

  @Override
  public void initializeClient(Consumer<IItemRenderProperties> consumer) {
    super.initializeClient(consumer);

    consumer.accept(new IItemRenderProperties() {
      @Override
      public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        System.out.println(itemRenderer);
        return itemRenderer.get();
      }
    });
  }
}
