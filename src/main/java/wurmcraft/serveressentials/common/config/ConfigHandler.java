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

    public static void preInit(FMLPreInitializationEvent e) {
        location = e.getSuggestedConfigurationFile();
        config = new Configuration(location);
        loadConfig();
    }

    public static void loadConfig() {
        LogHandler.info("Loading Config");
        debug = config.get(Configuration.CATEGORY_GENERAL, "debug", Defaults.DEBUG, "Enable debug mode");
        Settings.debug = debug.getBoolean();

        if (config.hasChanged()) {
            LogHandler.info("Saving Config");
            config.save();
        }
    }
}
