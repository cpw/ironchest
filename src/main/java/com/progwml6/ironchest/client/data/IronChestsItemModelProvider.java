package com.progwml6.ironchest.client.data;

import com.progwml6.ironchest.IronChests;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public class IronChestsItemModelProvider extends ItemModelProvider {

  public IronChestsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, IronChests.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {

  }

  @Override
  public String getName() {
    return "Iron Chests Item Models";
  }
}
