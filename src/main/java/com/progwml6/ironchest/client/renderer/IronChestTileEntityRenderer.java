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
package com.progwml6.ironchest.client.renderer;

import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.platform.GlStateManager;
import com.progwml6.ironchest.common.blocks.ChestBlock;
import com.progwml6.ironchest.common.blocks.ChestType;
import com.progwml6.ironchest.common.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.tileentity.IronChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class IronChestTileEntityRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T>
{
    private final ChestModel chestModel = new ChestModel();

    private static ItemEntity customItem;

    private Random random = new Random();

    private ItemRenderer itemRenderer;

    private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F }, { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F } };

    @Override
    public void render(T tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);

        IronChestTileEntity tileEntity = (IronChestTileEntity) tileEntityIn;

        BlockState blockstate = tileEntity.hasWorld() ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = ChestType.IRON;
        ChestType actualType = ChestBlock.getTypeFromBlock(blockstate.getBlock());

        if (actualType != null)
        {
            chestType = actualType;
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(new ResourceLocation("ironchest", "textures/model/" + chestType.modelTexture));
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        GlStateManager.pushMatrix();

        if (chestType == ChestType.CRYSTAL)
        {
            GlStateManager.disableCull();
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);

        float f = blockstate.get(ChestBlock.FACING).getHorizontalAngle();
        if ((double) Math.abs(f) > 1.0E-5D)
        {
            GlStateManager.translatef(0.5F, 0.5F, 0.5F);
            GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        }

        if (chestType.isTransparent())
        {
            GlStateManager.scalef(1F, 0.99F, 1F);
        }

        this.rotateChestLid(tileEntityIn, partialTicks, this.chestModel);
        this.chestModel.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (chestType == ChestType.CRYSTAL)
        {
            GlStateManager.enableCull();
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        if (this.rendererDispatcher.renderInfo != null)
        {
            if (chestType.isTransparent() && tileEntity.getDistanceSq(this.rendererDispatcher.renderInfo.getProjectedView().x, this.rendererDispatcher.renderInfo.getProjectedView().y, this.rendererDispatcher.renderInfo.getProjectedView().z) < 128d)
            {
                this.random.setSeed(254L);
                float shiftX;
                float shiftY;
                float shiftZ;
                int shift = 0;
                float blockScale = 0.70F;
                float timeD = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;

                if (((CrystalChestTileEntity) tileEntity).getTopItems().get(1).isEmpty())
                {
                    shift = 8;
                    blockScale = 0.85F;
                }

                GlStateManager.pushMatrix();
                GlStateManager.translatef((float) x, (float) y, (float) z);

                if (customItem == null)
                {
                    customItem = new ItemEntity(EntityType.ITEM, this.getWorld());
                }

                for (ItemStack item : ((CrystalChestTileEntity) tileEntity).getTopItems())
                {
                    if (shift > shifts.length || shift > 8)
                    {
                        break;
                    }

                    if (item.isEmpty())
                    {
                        shift++;
                        continue;
                    }

                    shiftX = shifts[shift][0];
                    shiftY = shifts[shift][1];
                    shiftZ = shifts[shift][2];
                    shift++;

                    GlStateManager.pushMatrix();
                    GlStateManager.translatef(shiftX, shiftY, shiftZ);
                    GlStateManager.rotatef(timeD, 0F, 1F, 0F);
                    GlStateManager.scalef(blockScale, blockScale, blockScale);

                    customItem.setItem(item);

                    if (this.itemRenderer == null)
                    {
                        this.itemRenderer = new ItemRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer())
                        {
                            @Override
                            public int getModelCount(ItemStack stack)
                            {
                                return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
                            }

                            @Override
                            public boolean shouldBob()
                            {
                                return false;
                            }

                            @Override
                            public boolean shouldSpreadItems()
                            {
                                return true;
                            }
                        };
                    }

                    this.itemRenderer.doRender(customItem, 0D, 0D, 0D, 0F, partialTicks);

                    GlStateManager.popMatrix();
                }

                GlStateManager.popMatrix();
            }
        }
    }

    private void rotateChestLid(T tileEntity, float partialTicks, ChestModel chestModel)
    {
        float f = ((IChestLid) tileEntity).getLidAngle(partialTicks);
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        chestModel.getLid().rotateAngleX = -(f * ((float) Math.PI / 2F));
    }
}
