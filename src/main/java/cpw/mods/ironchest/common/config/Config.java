package cpw.mods.ironchest.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.ironchest.IronChest;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public final class Config
{
    public static Config instance = new Config();

    public static Logger log = LogManager.getLogger(IronChest.MOD_ID + "-" + "Config");

    private static final String ENABLE_DISABLE = "ENABLE-DISABLE";

    private Config()
    {
    }

    public static void load(FMLPreInitializationEvent event)
    {
        configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.2", false);
        configFile.load();

        syncConfig();
    }

    public static boolean syncConfig()
    {
        enableShulkerBoxRecipes = configFile.get(ENABLE_DISABLE, "Enable Shulker Box Recipes", enableShulkerBoxRecipes).getBoolean(enableShulkerBoxRecipes);
        addShulkerBoxesToCreative = configFile.get(ENABLE_DISABLE, "Add Shulker Boxes to Creative Menu", addShulkerBoxesToCreative).getBoolean(addShulkerBoxesToCreative);

        // save changes if any
        boolean changed = false;

        if (configFile.hasChanged())
        {
            configFile.save();
            changed = true;
        }

        return changed;
    }

    //@formatter:off
    public static boolean enableShulkerBoxRecipes = true;
    public static boolean addShulkerBoxesToCreative = true;
    
    static Configuration configFile;
    //@formatter:on
}
