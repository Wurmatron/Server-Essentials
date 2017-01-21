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
    private static Property home_name;
    private static Property teleport_cooldown;

    public static void preInit(FMLPreInitializationEvent e) {
        location = e.getSuggestedConfigurationFile();
        config = new Configuration(location);
        loadConfig();
    }

    public static void loadConfig() {
        LogHandler.info("Loading Config");
        debug = config.get(Configuration.CATEGORY_GENERAL, "debug", Defaults.DEBUG, "Enable debug mode");
        Settings.debug = debug.getBoolean();
        home_name = config.get(Configuration.CATEGORY_GENERAL, "home_name", Defaults.DEFAULT_HOME_NAME, "Default home name");
        Settings.home_name = home_name.getString();
        teleport_cooldown = config.get(Configuration.CATEGORY_GENERAL, "teleport_cooldown", Defaults.TELEPORT_COOLDOWN, "Time between teleportation (in seconds)");
        Settings.teleport_cooldown = teleport_cooldown.getInt();

        if (config.hasChanged()) {
            LogHandler.info("Saving Config");
            config.save();
        }
    }
}
