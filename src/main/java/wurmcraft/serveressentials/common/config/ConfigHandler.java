package wurmcraft.serveressentials.common.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wurmcraft.serveressentials.common.api.permissions.Rank;
import wurmcraft.serveressentials.common.api.storage.AutoRank;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.api.storage.Kit;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.reference.Global;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.LogHandler;
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.TeamManager;

import java.io.*;
import java.util.ArrayList;

public class ConfigHandler {

	public static final File saveLocation = new File (FMLCommonHandler.instance ().getMinecraftServerInstance ().getDataDirectory () + File.separator + Global.NAME.replaceAll (" ","-"));

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
	private static Property onlineTimeMaxPrint;
	private static Property logChat;
	private static Property logInterval;
	private static Property spamLimit;
	private static Property mailFormat;
	private static Property buySign;
	private static Property sellSign;
	private static Property colorSign;
	private static Property commandSign;
	private static Property lang;

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
		forceChannelOnJoin = config.get (Configuration.CATEGORY_GENERAL,"forceDefaultChannelOnJoin",Defaults.FORCECHANNELONJOIN,"Should player be played in this channel on join?");
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
		onlineTimeMaxPrint = config.get (Configuration.CATEGORY_GENERAL,"onlineTimeMaxPrint",Defaults.ONLINETIMEMAXPRINT,"Maximum number of usernames to be printed for 'onlinetime' command",1,30);
		Settings.onlineTimeMaxPrint = onlineTimeMaxPrint.getInt ();
		logChat = config.get (Configuration.CATEGORY_GENERAL,"logChat",Defaults.LOGCHAT,"Should the server log chat?");
		Settings.logChat = logChat.getBoolean ();
		logInterval = config.get (Configuration.CATEGORY_GENERAL,"logInterval",Defaults.LOGINTERVAL,"How long before saving chat? (Seconds)");
		Settings.logInterval = logInterval.getInt ();
		spamLimit = config.get (Configuration.CATEGORY_GENERAL,"spamLimit",Defaults.SPAMLIMIT,"How many times a player can type the same message");
		Settings.spamLimit = spamLimit.getInt ();
		mailFormat = config.get (Configuration.CATEGORY_GENERAL,"mailFormat",Defaults.MAILFORMAT,"How mail is displayed");
		Settings.mailFormat = mailFormat.getString ();
		buySign = config.get (Global.CATEGORY_ECO,"buySign",true,"Buy Sign Enabled?");
		Settings.buySign = buySign.getBoolean ();
		sellSign = config.get (Global.CATEGORY_ECO,"sellSign",true,"Sell Sign Enabled?");
		Settings.sellSign = sellSign.getBoolean ();
		colorSign = config.get (Global.CATEGORY_ECO,"colorSign",true,"Color Sign Enabled?");
		Settings.colorSign = colorSign.getBoolean ();
		commandSign = config.get (Global.CATEGORY_ECO,"commandSign",true,"Command Sign Enabled?");
		Settings.commandSign = commandSign.getBoolean ();
		lang = config.get (Configuration.CATEGORY_GENERAL,"lang","en_us","Language used for commands");
		Settings.lang = lang.getString ();

		if (config.hasChanged ()) {
			LogHandler.info ("Saving Config");
			config.save ();
		}
	}

	public static void createDefaultChannels () {
		Channel globalChannel = new Channel (Defaults.DEFAULT_CHANNEL,"&9[G]",true,false,Channel.Type.PUBLIC,"",new String[] {"Wurmatron Wurm","\"Demi San\" \"Demi God\""});
		Channel staffChannel = new Channel ("Staff","&4[S]",false,false,Channel.Type.RANK,"Admin",null);
		Channel teamChannel = new Channel ("Team","&a[T]",true,false,Channel.Type.TEAM,"",null);
		DataHelper2.createIfNonExist (Keys.CHANNEL,globalChannel);
		DataHelper2.createIfNonExist (Keys.CHANNEL,staffChannel);
		DataHelper2.createIfNonExist (Keys.CHANNEL,teamChannel);
	}

	public static void createDefaultRank () {
		File groupLocation = new File (saveLocation + File.separator + Keys.RANK.name ());
		if (!groupLocation.exists () || groupLocation.listFiles ().length <= 0) {
			Rank defaultGroup = new Rank ("Default",true,"[Default]","",null,new String[] {"common.*","teleport.*"});
			Rank memberGroup = new Rank ("Member",false,"[Member]","",new String[] {"Default"},new String[] {"perk.*"});
			Rank adminGroup = new Rank ("Admin",false,"[Admin]","",new String[] {defaultGroup.getName ()},new String[] {"*"});
			DataHelper2.createIfNonExist (Keys.RANK,defaultGroup);
			DataHelper2.createIfNonExist (Keys.RANK,adminGroup);
			DataHelper2.createIfNonExist (Keys.RANK,memberGroup);
			loadRanks ();
		} else
			groupLocation.mkdirs ();
	}

	public static void loadAllKits () {
		File kitLocation = new File (saveLocation + File.separator + Keys.KIT.name ());
		if (kitLocation.exists ())
			for (File file : kitLocation.listFiles ())
				DataHelper2.load (file,Keys.KIT,new Kit ());
		else
			kitLocation.mkdirs ();
	}

	public static void loadAllAutoRanks () {
		File autoRankLocation = new File (saveLocation + File.separator + Keys.AUTO_RANK.name ());
		if (autoRankLocation.exists ())
			for (File file : autoRankLocation.listFiles ())
				DataHelper2.load (file,Keys.AUTO_RANK,new AutoRank ());
		else
			autoRankLocation.mkdirs ();
	}

	public static void loadAllTeams () {
		File teamLoction = new File (saveLocation + File.separator + Keys.TEAM.name ());
		if (teamLoction.exists ()) {
			for (File file : teamLoction.listFiles ()) {
				Team team = DataHelper2.load (file,Keys.TEAM,new Team ());
				TeamManager.register (team);
			}
		} else
			teamLoction.mkdirs ();
	}

	public static void loadRanks () {
		File rankLoction = new File (saveLocation + File.separator + Keys.RANK.name ());
		if (rankLoction.exists ()) {
			for (File file : rankLoction.listFiles ()) {
				Rank rank = DataHelper2.load (file,Keys.TEAM,new Rank ());
				RankManager.registerRank (rank);
			}
		} else
			rankLoction.mkdirs ();
	}

	public static void createDefaultAutoRank () {
		File autoRankLocation = new File (saveLocation + File.separator + Keys.AUTO_RANK.name ());
		if (!autoRankLocation.exists () || autoRankLocation.listFiles ().length <= 0) {
			AutoRank defaultToMember = new AutoRank (30,0,10,"Default","Member");
			DataHelper2.createIfNonExist (Keys.AUTO_RANK,defaultToMember);
			loadRanks ();
		}
	}

	public static void loadAllChannels () {
		File channelLocation = new File (saveLocation + File.separator + Keys.CHANNEL.name ());
		if (channelLocation.exists ()) {
			for (File file : channelLocation.listFiles ()) {
				Channel ch = DataHelper2.load (file,Keys.CHANNEL,new Channel ());
				ChannelManager.addChannel (ch);
			}
		} else
			channelLocation.mkdirs ();
	}

	public static void loadGlobal () {
		Gson gson = new GsonBuilder ().setPrettyPrinting ().create ();
		File globalLocation = new File (saveLocation + File.separator + "Global.json");
		if (globalLocation.exists ()) {
			ArrayList <String> lines = new ArrayList <> ();
			try {
				BufferedReader reader = new BufferedReader (new FileReader (globalLocation));
				String line;
				while ((line = reader.readLine ()) != null)
					lines.add (line);
				reader.close ();
			} catch (FileNotFoundException e) {
				e.printStackTrace ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
			String temp = "";
			for (int s = 0; s <= lines.size () - 1; s++)
				temp = temp + lines.get (s);
			DataHelper2.globalSettings = gson.fromJson (temp,wurmcraft.serveressentials.common.api.storage.Global.class);
		} else {
			DataHelper2.forceSave (saveLocation,new wurmcraft.serveressentials.common.api.storage.Global (null,new String[] {},new String[] {},"https://github.com/Wurmcraft/Server-Essentials"));
			loadGlobal ();
		}
	}

	public static void loadWarps () {
		File warpLocation = new File (saveLocation + File.separator + Keys.WARP.name ());
		if (warpLocation.exists ())
			for (File file : warpLocation.listFiles ())
				DataHelper2.load (file,Keys.WARP,new Warp ());
		else
			warpLocation.mkdirs ();
	}

}
