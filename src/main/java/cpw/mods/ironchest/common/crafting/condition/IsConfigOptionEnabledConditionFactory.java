package cpw.mods.ironchest.common.crafting.condition;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import cpw.mods.ironchest.common.config.Config;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class IsConfigOptionEnabledConditionFactory implements IConditionFactory
{
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json)
    {
        String configSetting = JsonUtils.getString(json, "config_setting", "");

        switch (configSetting)
        {
        case "enableShulkerBoxRecipes":
            return () -> Config.enableShulkerBoxRecipes;
        default:
            throw new RuntimeException(String.format("Invalid config setting: %s", configSetting));
        }
    }
}
