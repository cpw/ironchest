/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ObjectArrays;

public class RegistryHelper 
{
    /**
     * A temporary workaround whilst GameRegistry hasn't been fully updated to support 1.8
     */
    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name, Object... itemCtorArgs)
    {
        block = GameRegistry.registerBlock(block, itemclass, name, itemCtorArgs);
        Item associatedItem = GameRegistry.findItem("ironchest", name);
        
        Map itemBlockMap = (Map)ObfuscationReflectionHelper.getPrivateValue(Item.class, null, "field_179220_a");
        
        if (!itemBlockMap.containsKey(block)) itemBlockMap.put(block, associatedItem);
        
        Iterator iterator = block.getBlockState().getValidStates().iterator();
        
        while (iterator.hasNext())
        {
            IBlockState iblockstate = (IBlockState)iterator.next();
            int id = Block.blockRegistry.getIDForObject(block) << 4 | block.getMetaFromBlockState(iblockstate);
            Block.field_176229_d.func_148746_a(iblockstate, id);
        }
        
        return block;
    }
}
