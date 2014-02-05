/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.client;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.primitives.SignedBytes;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.MappableItemStackWrapper;
import cpw.mods.ironchest.TileEntityIronChest;

public class TileEntityIronChestRenderer extends TileEntitySpecialRenderer {
    @SuppressWarnings("unused")
    private static Map<MappableItemStackWrapper, Integer> renderList = new HashMap<MappableItemStackWrapper, Integer>();

    private static Map<IronChestType, ResourceLocation> locations;
    static {
        Builder<IronChestType, ResourceLocation> builder = ImmutableMap.<IronChestType,ResourceLocation>builder();
        for (IronChestType typ : IronChestType.values()) {
            builder.put(typ, new ResourceLocation("ironchest","textures/model/"+typ.getModelTexture()));
        }
        locations = builder.build();
    }
    private Random random;

    @SuppressWarnings("unused")
    private RenderBlocks renderBlocks;

    private RenderItem itemRenderer;

    private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F },
            { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F }, };

    public TileEntityIronChestRenderer()
    {
        model = new ModelChest();
        random = new Random();
        renderBlocks = new RenderBlocks();
        itemRenderer = new RenderItem() {
            @Override
            public byte getMiniBlockCount(ItemStack stack, byte original) {
                return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 15) + 1);
            }
            @Override
            public byte getMiniItemCount(ItemStack stack, byte original) {
                return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 7) + 1);
            }
            @Override
            public boolean shouldBob() {
                return false;
            }
            @Override
            public boolean shouldSpreadItems() {
                return false;
            }
        };
        itemRenderer.setRenderManager(RenderManager.instance);
    }

    public void render(TileEntityIronChest tile, double x, double y, double z, float partialTick) {
        if (tile == null) {
            return;
        }
        int facing = 3;
        IronChestType type = tile.getType();
        if (tile != null && tile.hasWorldObj()) {
            facing = tile.getFacing();
            type = tile.getType();
            int typ = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
            type = IronChestType.values()[typ];
        }
        bindTexture(locations.get(type));
        glPushMatrix();
        glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        glScalef(1.0F, -1F, -1F);
        glTranslatef(0.5F, 0.5F, 0.5F);
        int k = 0;
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }
        glRotatef(k, 0.0F, 1.0F, 0.0F);
        glTranslatef(-0.5F, -0.5F, -0.5F);
        float lidangle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;
        lidangle = 1.0F - lidangle;
        lidangle = 1.0F - lidangle * lidangle * lidangle;
        model.chestLid.rotateAngleX = -((lidangle * 3.141593F) / 2.0F);
        // Render the chest itself
        model.renderAll();
        glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
        glPopMatrix();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (type.isTransparent() && tile.getDistanceFrom(this.field_147501_a.field_147560_j, this.field_147501_a.field_147561_k, this.field_147501_a.field_147558_l) < 128d) {
            random.setSeed(254L);
            float shiftX;
            float shiftY;
            float shiftZ;
            int shift = 0;
            float blockScale = 0.70F;
            float timeD = (float) (360.0 * (double) (System.currentTimeMillis() & 0x3FFFL) / (double) 0x3FFFL);
            if (tile.getTopItemStacks()[1] == null) {
                shift = 8;
                blockScale = 0.85F;
            }
            glPushMatrix();
            glDisable(2896 /* GL_LIGHTING */);
            glTranslatef((float) x, (float) y, (float) z);
            EntityItem customitem = new EntityItem(field_147501_a.field_147550_f);
            customitem.hoverStart = 0f;
            for (ItemStack item : tile.getTopItemStacks()) {
                if (shift > shifts.length) {
                    break;
                }
                if (item == null) {
                    shift++;
                    continue;
                }
                shiftX = shifts[shift][0];
                shiftY = shifts[shift][1];
                shiftZ = shifts[shift][2];
                shift++;
                glPushMatrix();
                glTranslatef(shiftX, shiftY, shiftZ);
                glRotatef(timeD, 0.0F, 1.0F, 0.0F);
                glScalef(blockScale, blockScale, blockScale);
                customitem.setEntityItemStack(item);
                itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);
                glPopMatrix();
            }
            glEnable(2896 /* GL_LIGHTING */);
            glPopMatrix();
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick)
    {
        render((TileEntityIronChest) tileentity, x, y, z, partialTick);
    }

    private ModelChest model;
}
