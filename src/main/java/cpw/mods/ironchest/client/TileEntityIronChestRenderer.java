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

import java.util.Random;

import com.google.common.primitives.SignedBytes;

import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityIronChestRenderer extends TileEntitySpecialRenderer<TileEntityIronChest>
{
    private Random random;
    private RenderEntityItem itemRenderer;
    private ModelChest model;

    private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F },
            { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F }, };
    private static EntityItem customitem = new EntityItem(null);
    private static float halfPI = (float) (Math.PI / 2D);

    public TileEntityIronChestRenderer()
    {
        this.model = new ModelChest();
        this.random = new Random();
    }

    @Override
    public void renderTileEntityAt(TileEntityIronChest tile, double x, double y, double z, float partialTick, int breakStage)
    {
        if (tile == null || tile.isInvalid())
        {
            return;
        }

        EnumFacing facing = EnumFacing.SOUTH;
        IronChestType type = tile.getType();

        if (tile.hasWorldObj() && tile.getWorld().getBlockState(tile.getPos()).getBlock() == IronChest.ironChestBlock)
        {
            facing = tile.getFacing();
            IBlockState state = tile.getWorld().getBlockState(tile.getPos());
            type = state.getValue(BlockIronChest.VARIANT_PROP);
        }

        if (breakStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[breakStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4F, 4F, 1F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(type.modelTexture);
        }
        GlStateManager.pushMatrix();
        if (type == IronChestType.CRYSTAL)
        {
            GlStateManager.disableCull();
        }
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate((float) x, (float) y + 1F, (float) z + 1F);
        GlStateManager.scale(1F, -1F, -1F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);

        switch (facing)
        {
        case NORTH:
        {
            GlStateManager.rotate(180F, 0F, 1F, 0F);
            break;
        }
        case SOUTH:
        {
            GlStateManager.rotate(0F, 0F, 1F, 0F);
            break;
        }
        case WEST:
        {
            GlStateManager.rotate(90F, 0F, 1F, 0F);
            break;
        }
        case EAST:
        {
            GlStateManager.rotate(270F, 0F, 1F, 0F);
            break;
        }
        default:
        {
            GlStateManager.rotate(0F, 0F, 1F, 0F);
            break;
        }
        }

        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float lidangle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;
        lidangle = 1F - lidangle;
        lidangle = 1F - lidangle * lidangle * lidangle;

        if (type.isTransparent())
        {
            GlStateManager.scale(1F, 0.99F, 1F);
        }

        this.model.chestLid.rotateAngleX = -lidangle * halfPI;
        // Render the chest itself
        this.model.renderAll();
        if (breakStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
        if (type == IronChestType.CRYSTAL)
        {
            GlStateManager.enableCull();
        }
        GlStateManager.popMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);

        if (type.isTransparent()
                && tile.getDistanceSq(this.rendererDispatcher.entityX, this.rendererDispatcher.entityY, this.rendererDispatcher.entityZ) < 128d)
        {
            this.random.setSeed(254L);
            float shiftX;
            float shiftY;
            float shiftZ;
            int shift = 0;
            float blockScale = 0.70F;
            float timeD = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTick;
            if (tile.getTopItemStacks()[1] == null)
            {
                shift = 8;
                blockScale = 0.85F;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y, (float) z);

            customitem.setWorld(this.getWorld());
            customitem.hoverStart = 0F;
            for (ItemStack item : tile.getTopItemStacks())
            {
                if (shift > shifts.length)
                {
                    break;
                }
                if (item == null)
                {
                    shift++;
                    continue;
                }
                shiftX = shifts[shift][0];
                shiftY = shifts[shift][1];
                shiftZ = shifts[shift][2];
                shift++;
                GlStateManager.pushMatrix();
                GlStateManager.translate(shiftX, shiftY, shiftZ);
                GlStateManager.rotate(timeD, 0F, 1F, 0F);
                GlStateManager.scale(blockScale, blockScale, blockScale);
                customitem.setEntityItemStack(item);

                if (this.itemRenderer == null)
                {
                    this.itemRenderer = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
                        @Override
                        public int getModelCount(ItemStack stack)
                        {
                            return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 15) + 1);
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

                this.itemRenderer.doRender(customitem, 0D, 0D, 0D, 0F, partialTick);
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
        }

    }
}
