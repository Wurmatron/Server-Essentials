package wurmcraft.serveressentials.common.config;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wurmcraft.serveressentials.common.utils.LogHandler;

import java.io.File;

public class ConfigHandler {

    public static File location;
    public static Configuration config;

    private static Property debug;
    private static Property default_home_name;

    public static void preInit(FMLPreInitializationEvent e) {
        location = e.getSuggestedConfigurationFile();
        config = new Configuration(location);
        loadConfig();
    }

    public static void loadConfig() {
        LogHandler.info("Loading Config");
        debug = config.get(Configuration.CATEGORY_GENERAL, "debug", Defaults.DEBUG, "Enable debug mode");
        Settings.debug = debug.getBoolean();
        default_home_name = config.get(Configuration.CATEGORY_GENERAL, "default_home_name", Defaults.DEFAULT_HOME_NAME, "Default home name");
        Settings.default_home_name = default_home_name.getString();


        if (config.hasChanged()) {
            LogHandler.info("Saving Config");
            config.save();
        }
    }
}
