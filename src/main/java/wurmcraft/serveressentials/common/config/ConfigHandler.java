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
	private static Property respawn_point;
	private static Property tpa_timeout;
	private static Property default_channel;
	private static Property forceChannelOnJoin;
	private static Property chatFormat;
	private static Property currencySymbol;
	private static Property messageFormat;
	private static Property securityModule;
	private static Property trustedStaff;

	public static void preInit (FMLPreInitializationEvent e) {
		location = e.getSuggestedConfigurationFile ();
		config = new Configuration (location);
		loadConfig ();
	}

	private static void loadConfig () {
		LogHandler.info ("Loading Config");
		debug = config.get (Configuration.CATEGORY_GENERAL,"debug",Defaults.DEBUG,"Enable debug mode");
		Settings.debug = debug.getBoolean ();
		home_name = config.get (Configuration.CATEGORY_GENERAL,"home_name",Defaults.DEFAULT_HOME_NAME,"Default home name");
		Settings.home_name = home_name.getString ();
		teleport_cooldown = config.get (Configuration.CATEGORY_GENERAL,"teleport_cooldown",Defaults.TELEPORT_COOLDOWN,"Time between teleportation (in seconds)");
		Settings.teleport_cooldown = teleport_cooldown.getInt ();
		respawn_point = config.get (Configuration.CATEGORY_GENERAL,"respawn_point",Defaults.RESPAWN_POINT,"Respawn point (Home, Spawn and Default)");
		Settings.respawn_point = respawn_point.getString ();
		tpa_timeout = config.get (Configuration.CATEGORY_GENERAL,"tpa_timeout",Defaults.TPA_TIMEOUT,"Time for Tpa request timeout");
		Settings.tpa_timeout = tpa_timeout.getInt ();
		default_channel = config.get (Configuration.CATEGORY_GENERAL,"defaultChannel",Defaults.DEFAULT_CHANNEL,"Default channels players startout in");
		Settings.default_channel = default_channel.getString ();
		forceChannelOnJoin = config.get (Configuration.CATEGORY_GENERAL,"forceDefaultChannelOnJoin",Defaults.FORCECHANNELONJOIN,"Shouuldplayer be played in this channel on join?");
		Settings.forceChannelOnJoin = forceChannelOnJoin.getBoolean ();
		chatFormat = config.get (Configuration.CATEGORY_GENERAL,"chatFormat",Defaults.CHATFORMAT,"Formatting for how the chat is displayed");
		Settings.chatFormat = chatFormat.getString ();
		currencySymbol = config.get (Configuration.CATEGORY_GENERAL,"currencySymbol",Defaults.CURRENCY_SYMBOL,"Symbol used for the server currency");
		Settings.currencySymbol = currencySymbol.getString ();
		messageFormat = config.get (Configuration.CATEGORY_GENERAL,"messageFormat",Defaults.MESSAGEFORMAT,"Formatting for how a message is displayed");
		Settings.messageFormat = messageFormat.getString ();
		trustedStaff = config.get (Configuration.CATEGORY_GENERAL,"trustedStaff","","Security protection against unauthorized \"things\"");
		Settings.trustedStaff = trustedStaff.getString ();
		securityModule = config.get (Configuration.CATEGORY_GENERAL,"securityModule",false,"Enable the Server-Essentials Security");
		Settings.securityModule = securityModule.getBoolean ();

		if (config.hasChanged ()) {
			LogHandler.info ("Saving Config");
			config.save ();
		}
	}
}
