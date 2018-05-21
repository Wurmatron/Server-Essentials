package wurmcraft.serveressentials.common.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Global;

public class LogHandler {

	public static final Logger logger = LogManager.getLogger (Global.NAME);
	private static final Logger chat = LogManager.getLogger ("Chat");

	public static void log (Level level,String msg) {
		logger.log (level,msg);
	}

	public static void info (String msg) {
		log (Level.INFO,msg);
	}

	public static void warn (String msg) {
		log (Level.WARN,msg);
	}

	public static void all (String msg) {
		log (Level.ALL,msg);
	}

	public static void error (String msg) {
		log (Level.ERROR,msg);
	}

	public static void debug (String msg) {
		if (ConfigHandler.debug)
			log (Level.INFO,msg);
	}

	public static void chat (String msg) {
		chat.log (Level.INFO,msg);
	}
}