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
import com.progwml6.ironchest.common.blocks.BlockChest;
import com.progwml6.ironchest.common.blocks.IronChestType;
import com.progwml6.ironchest.common.tileentity.TileEntityCrystalChest;
import com.progwml6.ironchest.common.tileentity.TileEntityIronChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Random;

public class TileEntityIronChestRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T>
{
    private Random random;

    private RenderEntityItem itemRenderer;

    private ModelChest model;

    //@formatter:off
    private static float[][] shifts = { 
            { 0.3F, 0.45F, 0.3F }, 
            { 0.7F, 0.45F, 0.3F }, 
            { 0.3F, 0.45F, 0.7F }, 
            { 0.7F, 0.45F, 0.7F }, 
            { 0.3F, 0.1F, 0.3F }, 
            { 0.7F, 0.1F, 0.3F }, 
            { 0.3F, 0.1F, 0.7F }, 
            { 0.7F, 0.1F, 0.7F }, 
            { 0.5F, 0.32F, 0.5F } };
    //@formatter:on

    private static EntityItem customitem = new EntityItem(null);

    public TileEntityIronChestRenderer()
    {
        this.model = new ModelChest();
        this.random = new Random();
    }

    @Override
    public void render(T tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);

        TileEntityIronChest tileEntity = (TileEntityIronChest) tileEntityIn;

        IBlockState iBlockState = tileEntity.hasWorld() ? tileEntity.getBlockState() : (IBlockState) tileEntity.getBlockToUse().getDefaultState().with(BlockChest.FACING, EnumFacing.NORTH);
        IronChestType chestType = IronChestType.IRON;
        IronChestType typeNew = BlockChest.getTypeFromBlock(iBlockState.getBlock());

        if (typeNew != null)
        {
            chestType = typeNew;
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4F, 4F, 1F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(chestType.modelTexture);
        }

        GlStateManager.pushMatrix();

        if (chestType == IronChestType.CRYSTAL)
        {
            GlStateManager.disableCull();
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);

        float f = iBlockState.get(BlockChest.FACING).getHorizontalAngle();

        if (Math.abs(f) > 1.0E-5D)
        {
            GlStateManager.translatef(0.5F, 0.5F, 0.5F);
            GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        }

        if (chestType.isTransparent())
        {
            GlStateManager.scalef(1F, 0.99F, 1F);
        }

        this.rotateChestLid(tileEntityIn, partialTicks, model);
        model.renderAll();

        if (chestType == IronChestType.CRYSTAL)
        {
            GlStateManager.enableCull();
        }

        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        if (chestType.isTransparent() && tileEntity.getDistanceSq(this.rendererDispatcher.entityX, this.rendererDispatcher.entityY, this.rendererDispatcher.entityZ) < 128d)
        {
            this.random.setSeed(254L);

            float shiftX;
            float shiftY;
            float shiftZ;
            int shift = 0;
            float blockScale = 0.70F;
            float timeD = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;

            if (((TileEntityCrystalChest) tileEntity).getTopItems().get(1).isEmpty())
            {
                shift = 8;
                blockScale = 0.85F;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translatef((float) x, (float) y, (float) z);

            customitem.setWorld(this.getWorld());
            customitem.hoverStart = 0F;

            for (ItemStack item : ((TileEntityCrystalChest) tileEntity).getTopItems())
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

                customitem.setItem(item);

                if (this.itemRenderer == null)
                {
                    this.itemRenderer = new RenderEntityItem(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer())
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

                this.itemRenderer.doRender(customitem, 0D, 0D, 0D, 0F, partialTicks);

                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
        }
    }

    private void rotateChestLid(T tileEntityIn, float lidAngleIn, ModelChest modelIn)
    {
        float f = ((IChestLid) tileEntityIn).getLidAngle(lidAngleIn);
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        modelIn.getLid().rotateAngleX = -(f * ((float) Math.PI / 2F));
    }
}
