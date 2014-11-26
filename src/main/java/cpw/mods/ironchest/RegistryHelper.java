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

import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class RegistryHelper 
{
    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name, Object... itemCtorArgs)
    {
        block = GameRegistry.registerBlock(block, itemclass, name, itemCtorArgs);

        Iterator iterator = block.getBlockState().getValidStates().iterator();

        while (iterator.hasNext())
        {
            IBlockState iblockstate = (IBlockState)iterator.next();
            int id = Block.blockRegistry.getIDForObject(block) << 4 | block.getMetaFromState(iblockstate);
            Block.BLOCK_STATE_IDS.put(iblockstate, id);
        }

        return block;
    }
}
