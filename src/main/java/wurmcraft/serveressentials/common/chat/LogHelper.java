package wurmcraft.serveressentials.common.chat;

import joptsimple.internal.Strings;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.config.ConfigHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogHelper {

	public static final File LOG_LOCATION = new File (ConfigHandler.saveLocation + File.separator,"Logs" + File.separator);
	private static long LAST_SAVE = System.currentTimeMillis ();
	private static HashMap <Channel, List <String>> chatLog = new HashMap <> ();

	public static void checkAndSave () {
		if (System.currentTimeMillis () >= (LAST_SAVE + (ConfigHandler.logInterval * 1000)) && chatLog.size () > 0)
			save ();
	}

	private static void save () {
		if (!LOG_LOCATION.exists ())
			LOG_LOCATION.mkdir ();
		for (Channel channel : chatLog.keySet ()) {
			File file = new File (LOG_LOCATION + File.separator + channel.getName () + ".log");
			List <String> log = chatLog.get (channel);
			try {
				if (!file.exists ())
					file.createNewFile ();
				FileOutputStream steam = new FileOutputStream (file,true);
				steam.write (Strings.join (log,"").getBytes ());
				log.clear ();
			} catch (IOException e) {
				e.printStackTrace ();
			}

		}
	}

	public static void addChat (Channel channel,String msg) {
		if (chatLog.get (channel) != null) {
			List <String> log = chatLog.get (channel);
			log.add (msg + "\n");
		} else {
			List <String> log = new ArrayList <> ();
			log.add (msg + "\n");
			chatLog.put (channel,log);
		}
	}
}
