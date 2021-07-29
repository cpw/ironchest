package com.progwml6.ironchest.common.item;

import com.progwml6.ironchest.client.model.inventory.IronChestItemStackRenderer;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.entity.CopperChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.CrystalChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.DiamondChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.DirtChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.GoldChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.IronChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.ObsidianChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.SilverChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.DistExecutor;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IronChestBlockItem extends BlockItem {

  protected Supplier<IronChestsTypes> type;

  public IronChestBlockItem(Block block, Properties properties, Supplier<Callable<IronChestsTypes>> type) {
    super(block, properties);

    IronChestsTypes tempType = DistExecutor.callWhenOn(Dist.CLIENT, type);

    this.type = tempType == null ? null : () -> tempType;
  }

  @Override
  public void initializeClient(Consumer<IItemRenderProperties> consumer) {
    super.initializeClient(consumer);

    consumer.accept(new IItemRenderProperties() {
      @Override
      public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        Supplier<BlockEntity> modelToUse;

        switch (type.get()) {
          case GOLD -> modelToUse = () -> new GoldChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.GOLD_CHEST.get().defaultBlockState());
          case DIAMOND -> modelToUse = () -> new DiamondChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.DIAMOND_CHEST.get().defaultBlockState());
          case COPPER -> modelToUse = () -> new CopperChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.COPPER_CHEST.get().defaultBlockState());
          case SILVER -> modelToUse = () -> new SilverChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.SILVER_CHEST.get().defaultBlockState());
          case CRYSTAL -> modelToUse = () -> new CrystalChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.CRYSTAL_CHEST.get().defaultBlockState());
          case OBSIDIAN -> modelToUse = () -> new ObsidianChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.OBSIDIAN_CHEST.get().defaultBlockState());
          case DIRT -> modelToUse = () -> new DirtChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.DIRT_CHEST.get().defaultBlockState());
          default -> modelToUse = () -> new IronChestBlockEntity(BlockPos.ZERO, IronChestsBlocks.IRON_CHEST.get().defaultBlockState());
        }

        return new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), modelToUse);
      }
    });
  }
}
