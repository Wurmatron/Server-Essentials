package wurmcraft.serveressentials.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.permissions.Rank;
import wurmcraft.serveressentials.common.api.storage.*;
import wurmcraft.serveressentials.common.api.team.ITeam;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.config.Defaults;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DataHelper {

	public static final File saveLocation = new File (FMLCommonHandler.instance ().getMinecraftServerInstance ().getDataDirectory () + File.separator + wurmcraft.serveressentials.common.reference.Global.NAME);
	public static final File playerDataLocation = new File (saveLocation + File.separator + "Player-Data" + File.separator);
	public static final File warpLocation = new File (saveLocation + File.separator + "Warp" + File.separator);
	public static final File groupLocation = new File (saveLocation + File.separator + "Group" + File.separator);
	public static final File teamLoction = new File (saveLocation + File.separator + "Teams" + File.separator);
	public static final File channelLocation = new File (saveLocation + File.separator + "Channels" + File.separator);
	public static final File vaultLocation = new File (saveLocation + File.separator + "Vaults" + File.separator);
	public static final File marketLocation = new File (saveLocation + File.separator + "Markets" + File.separator);
	public static final File kitLocation = new File (saveLocation + File.separator + "Kits" + File.separator);
	private static final Gson gson = new GsonBuilder ().setPrettyPrinting ().create ();
	public static HashMap <UUID, PlayerData> loadedPlayers = new HashMap <> ();
	public static ArrayList <Warp> loadedWarps = new ArrayList <> ();
	public static HashMap <Long, EntityPlayer[]> activeRequests = new HashMap <> ();
	public static ArrayList <UUID> afkPlayers = new ArrayList <> ();
	public static Global globalSettings;
	public static HashMap <UUID, Vault[]> playerVaults = new HashMap <> ();
	public static HashMap <UUID, ShopData> playerShops = new HashMap <> ();
	public static ArrayList <Kit> loadedKits = new ArrayList <> ();
	public static HashMap <UUID, UUID> lastMessage = new HashMap <> ();
	public static ArrayList <UUID> spys = new ArrayList <> ();

	public static void registerPlayer (EntityPlayer player) {
		if (!loadedPlayers.containsKey (player.getGameProfile ().getId ())) {
			PlayerData data = loadPlayerData (player.getGameProfile ().getId ());
			if (data != null)
				loadedPlayers.put (player.getGameProfile ().getId (),data);
			else {
				PlayerData newData = new PlayerData (RankManager.getDefaultRank ());
				createPlayerFile (player.getGameProfile ().getId (),newData);
				loadedPlayers.put (player.getGameProfile ().getId (),newData);
			}
		}
	}

	private static void createPlayerFile (UUID name,PlayerData data) {
		if (!playerDataLocation.exists ())
			playerDataLocation.mkdirs ();
		File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
		try {
			playerFileLocation.createNewFile ();
			Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}

	public static PlayerData getPlayerData (UUID name) {
		return loadedPlayers.get (name);
	}

	public static PlayerData loadPlayerData (UUID name) {
		File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
		if (playerFileLocation.exists ()) {
			ArrayList <String> lines = new ArrayList <> ();
			try {
				BufferedReader reader = new BufferedReader (new FileReader (playerFileLocation));
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
			return gson.fromJson (temp,PlayerData.class);
		}
		return null;
	}

	public static void unloadPlayerData (UUID name) {
		if (loadedPlayers.containsKey (name))
			loadedPlayers.remove (name);
	}

	public static void reloadPlayerData (UUID name) {
		unloadPlayerData (name);
		PlayerData data = loadPlayerData (name);
		loadedPlayers.put (name,data);
	}

	public static String addPlayerHome (UUID name,Home home) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			String msg = data.addHome (home);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
			return msg;
		}
		return Local.HOME_FAILED;
	}

	public static String deleteHome (UUID name,String home) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			String msg = data.delHome (home);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
			return msg;
		}
		return Local.HOME_ERROR_DELETION.replaceAll ("#",home);
	}

	public static void updateTeleportTimer (UUID name) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setTeleport_timer (System.currentTimeMillis ());
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void updateLastseen (UUID name) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setLastseen (System.currentTimeMillis ());
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void setTeam (UUID name,ITeam team) {
		PlayerData data = getPlayerData (name);
		File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			data.setTeam (team);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void addMail (Mail mail) {
		PlayerData data = getPlayerData (mail.getReciver ());
		boolean wasLoaded = true;
		if (data == null) {
			data = loadPlayerData (mail.getReciver ());
			wasLoaded = false;
		}
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + mail.getReciver ().toString () + ".json");
			data.addMail (mail);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				if (wasLoaded)
					reloadPlayerData (mail.getReciver ());
				else
					unloadPlayerData (mail.getReciver ());
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void removeMail (UUID uuid,int index) {
		PlayerData data = getPlayerData (uuid);
		boolean wasLoaded = true;
		if (data == null) {
			data = loadPlayerData (uuid);
			wasLoaded = false;
		}
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + uuid.toString () + ".json");
			data.removeMail (index);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				if (wasLoaded)
					reloadPlayerData (uuid);
				else
					unloadPlayerData (uuid);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static ITextComponent displayLocation (Home home) {
		TextComponentString text = new TextComponentString ("X = " + home.getPos ().getX () + " Y = " + home.getPos ().getY () + " Z = " + home.getPos ().getZ ());
		text.getStyle ().setColor (TextFormatting.GREEN);
		return text;
	}

	public static ITextComponent displayLocation (Warp warp) {
		TextComponentString text = new TextComponentString ("X = " + warp.getPos ().getX () + " Y = " + warp.getPos ().getY () + " Z = " + warp.getPos ().getZ ());
		text.getStyle ().setColor (TextFormatting.GREEN);
		return text;
	}

	public static ITextComponent displayLocation (SpawnPoint spawn) {
		TextComponentString text = new TextComponentString ("X = " + spawn.location.getX () + " Y = " + spawn.location.getY () + " Z = " + spawn.location.getZ ());
		text.getStyle ().setColor (TextFormatting.GREEN);
		return text;
	}

	public static String createWarp (Warp warp) {
		if (loadedWarps.size () <= 0)
			loadWarps ();
		if (!warpLocation.exists ())
			warpLocation.mkdirs ();
		File warpFileLocation = new File (warpLocation + File.separator + warp.getName () + ".json");
		try {
			warpFileLocation.createNewFile ();
			Files.write (Paths.get (warpFileLocation.getAbsolutePath ()),gson.toJson (warp).getBytes ());
			loadedWarps.add (warp);
		} catch (IOException e) {
			e.printStackTrace ();
		}
		return Local.WARP_CREATED.replaceAll ("#",warp.getName ());
	}

	public static Warp[] getWarps () {
		if (loadedWarps.size () > 0)
			return loadedWarps.toArray (new Warp[0]);
		return new Warp[0];
	}

	public static void loadWarps () {
		if (warpLocation.exists ()) {
			for (File file : warpLocation.listFiles ())
				if (file.isFile () && file.getName ().endsWith (".json")) {
					ArrayList <String> lines = new ArrayList <> ();
					try {
						BufferedReader reader = new BufferedReader (new FileReader (file));
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
					Warp warp = gson.fromJson (temp,Warp.class);
					if (warp != null)
						loadedWarps.add (warp);
				}
		}
	}

	public static Warp getWarp (String name) {
		if (loadedWarps.size () <= 0)
			loadWarps ();
		if (loadedWarps.size () > 0 && name != null && name.length () > 0) {
			for (Warp warp : loadedWarps)
				if (warp.getName ().equalsIgnoreCase (name))
					return warp;
		}
		return null;
	}

	public static void deleteWarp (Warp warp) {
		loadedWarps.remove (warp);
		for (File file : warpLocation.listFiles ())
			if (file.isFile () && file.getName ().equalsIgnoreCase (warp.getName () + ".json"))
				file.delete ();
	}

	public static void createGlobal (Global global) {
		if (!saveLocation.exists ())
			saveLocation.mkdirs ();
		File globalFile = new File (saveLocation + File.separator + "Global.json");
		try {
			globalFile.createNewFile ();
			Files.write (Paths.get (globalFile.getAbsolutePath ()),gson.toJson (global).getBytes ());
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}

	public static void loadGlobal () {
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
			globalSettings = gson.fromJson (temp,Global.class);
		} else {
			createGlobal (new Global (null,new String[] {},new String[] {},"https://github.com/Wurmcraft/Server-Essentials"));
			loadGlobal ();
		}
	}

	public static void overrideGlobal (Global global) {
		createGlobal (global);
		loadGlobal ();
	}

	public static void loadRanks () {
		if (groupLocation.exists ()) {
			if (groupLocation.listFiles ().length <= 0)
				createDefaultRank ();
			for (File file : groupLocation.listFiles ())
				if (file.isFile () && file.getName ().endsWith (".json")) {
					ArrayList <String> lines = new ArrayList <> ();
					try {
						BufferedReader reader = new BufferedReader (new FileReader (file));
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
					Rank group = gson.fromJson (temp,Rank.class);
					if (group != null)
						RankManager.registerRank (group);
				}
		}
	}

	public static void createGroup (IRank group) {
		if (RankManager.getRanks ().size () <= 0)
			loadRanks ();
		if (!groupLocation.exists ())
			groupLocation.mkdirs ();
		File groupFileLocation = new File (groupLocation + File.separator + group.getName () + ".json");
		if (!groupFileLocation.exists ()) {
			try {
				groupFileLocation.createNewFile ();
				Files.write (Paths.get (groupFileLocation.getAbsolutePath ()),gson.toJson (group).getBytes ());
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void reloadRanks () {
		RankManager.clearAllRanks ();
		loadRanks ();
	}

	public static void createDefaultRank () {
		if (!groupLocation.exists () || groupLocation.listFiles ().length <= 0) {
			Rank defaultGroup = new Rank ("Default",true,"[Default]","",null,new String[] {"common.*","teleport.*"});
			Rank adminGroup = new Rank ("Admin",false,"[Admin]","",new String[] {defaultGroup.getName ()},new String[] {"*"});
			createGroup (defaultGroup);
			createGroup (adminGroup);
			loadRanks ();
		}
	}

	public static void createTeam (ITeam team,boolean forceNew) {
		if (TeamManager.getTeams ().size () <= 0)
			loadAllTeams ();
		if (!teamLoction.exists ())
			teamLoction.mkdirs ();
		File teamFileLocation = new File (teamLoction + File.separator + team.getName () + ".json");
		if (!teamFileLocation.exists () || forceNew) {
			try {
				teamFileLocation.createNewFile ();
				Files.write (Paths.get (teamFileLocation.getAbsolutePath ()),gson.toJson (team).getBytes ());
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void loadAllTeams () {
		if (teamLoction.exists ()) {
			for (File team : teamLoction.listFiles ()) {
				ArrayList <String> lines = new ArrayList <> ();
				try {
					BufferedReader reader = new BufferedReader (new FileReader (team));
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
				TeamManager.register (gson.fromJson (temp,Team.class));
			}
		} else
			teamLoction.mkdirs ();
	}

	public static void saveTeam (ITeam team) {
		File teamFileLoction = new File (teamLoction + File.separator + team.getName () + ".json");
		if (teamFileLoction.exists ())
			createTeam (team,true);
	}

	public static void deleteTeam (Team team) {
		File teamFileLoction = new File (teamLoction + File.separator + team.getName () + ".json");
		if (teamFileLoction.exists ())
			teamFileLoction.delete ();
	}

	public static void addAfkPlayer (EntityPlayer player) {
		if (!afkPlayers.contains (player.getGameProfile ().getId ()))
			afkPlayers.add (player.getGameProfile ().getId ());
	}

	public static void removeAfkPlayer (EntityPlayer player) {
		if (afkPlayers.contains (player.getGameProfile ().getId ()))
			afkPlayers.remove (player.getGameProfile ().getId ());
	}

	public static boolean isAfk (UUID uuid) {
		return afkPlayers.contains (uuid);
	}

	public static void setLastLocation (UUID name,BlockPos location) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setLastLocation (location);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void setMoney (UUID name,int money) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setMoney (money);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void saveChannel (Channel channel) {
		if (channel != null) {
			File channelFile = new File (channelLocation + File.separator + channel.getName () + ".json");
			try {
				channelFile.createNewFile ();
				Files.write (Paths.get (channelFile.getAbsolutePath ()),gson.toJson (channel).getBytes ());
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void loadAllChannels () {
		if (!channelLocation.exists ())
			channelLocation.mkdirs ();
		for (File channel : channelLocation.listFiles ()) {
			ArrayList <String> lines = new ArrayList <> ();
			try {
				BufferedReader reader = new BufferedReader (new FileReader (channel));
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
			ChannelManager.addChannel (gson.fromJson (temp,Channel.class));
		}
	}

	public static void createDefaultChannels () {
		Channel globalChannel = new Channel (Defaults.DEFAULT_CHANNEL,"&9[G]",true,false,Channel.Type.PUBLIC,"");
		Channel staffChannel = new Channel ("Staff","&4[S]",false,false,Channel.Type.RANK,"Admin");
		saveChannel (globalChannel);
		saveChannel (staffChannel);
	}

	public static void setChannel (UUID uuid,Channel channel) {
		PlayerData data = getPlayerData (uuid);
		if (data != null) {
			ChannelManager.removePlayerChannel (uuid,ChannelManager.getFromName (data.getCurrentChannel ()));
			data.setCurrentChannel (channel);
			ChannelManager.setPlayerChannel (uuid,channel);
		}
	}

	public static Channel getChannel (UUID uuid) {
		Channel channel = ChannelManager.getPlayerChannel (uuid);
		if (channel != null)
			return channel;
		else {
			PlayerData data = getPlayerData (uuid);
			if (data != null) {
				if (data.getCurrentChannel () != null) {
					ChannelManager.setPlayerChannel (uuid,ChannelManager.getFromName (data.getCurrentChannel ()));
					return ChannelManager.getPlayerChannel (uuid);
				} else {
					setChannel (uuid,ChannelManager.getFromName (Settings.default_channel));
					ChannelManager.setPlayerChannel (uuid,ChannelManager.getFromName (Settings.default_channel));
				}
			}
		}
		return null;
	}

	public static boolean isMuted (UUID uuid) {
		PlayerData data = getPlayerData (uuid);
		if (data != null) {
			return data.isMuted ();
		}
		return false;
	}

	public static void setMute (UUID name,boolean mute) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setMuted (mute);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static Vault[] loadVault (UUID uuid) {
		if (playerVaults.containsKey (uuid))
			playerVaults.remove (uuid);
		File vaultFileLocation = new File (vaultLocation + File.separator + uuid.toString () + ".json");
		if (vaultFileLocation.exists ()) {
			ArrayList <String> lines = new ArrayList <> ();
			try {
				BufferedReader reader = new BufferedReader (new FileReader (vaultFileLocation));
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
			Vault[] vaults = gson.fromJson (temp,Vault[].class);
			playerVaults.put (uuid,vaults);
			return vaults;
		}
		return null;
	}

	public static void saveVault (UUID uuid,Vault[] vaults) {
		if (!vaultLocation.exists ())
			vaultLocation.mkdirs ();
		File vaultFile = new File (vaultLocation + File.separator + uuid.toString () + ".json");
		if (vaultLocation.exists ()) {
			try {
				vaultFile.createNewFile ();
				Files.write (Paths.get (vaultFile.getAbsolutePath ()),gson.toJson (vaults).getBytes ());
				loadVault (uuid);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void saveVault (UUID uuid,Vault vault) {
		Vault[] uuidVaults = playerVaults.get (uuid);
		if (uuidVaults != null && uuidVaults.length > 0) {
			boolean exists = false;
			for (int index = 0; index < uuidVaults.length; index++)
				if (uuidVaults[index].getName ().equalsIgnoreCase (vault.getName ())) {
					exists = true;
					uuidVaults[index] = vault;
				}
			if (!exists) {
				Vault[] newVaults = new Vault[uuidVaults.length + 1];
				for (int index = 0; index < uuidVaults.length; index++)
					newVaults[index] = uuidVaults[index];
				newVaults[uuidVaults.length] = vault;
				saveVault (uuid,newVaults);
			} else
				saveVault (uuid,uuidVaults);
		} else
			saveVault (uuid,new Vault[] {vault});
	}

	public static void createMarket (UUID name,ShopData data) {
		if (!marketLocation.exists ())
			marketLocation.mkdirs ();
		File marketFile = new File (marketLocation + File.separator + name.toString () + ".json");
		try {
			marketFile.createNewFile ();
			Files.write (Paths.get (marketFile.getAbsolutePath ()),gson.toJson (data).getBytes ());
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}

	public static ShopData loadMarket (UUID name) {
		if (!playerShops.containsKey (name)) {
			File marketFile = new File (marketLocation + File.separator + name.toString () + ".json");
			if (marketFile.exists ()) {
				ArrayList <String> lines = new ArrayList <> ();
				try {
					BufferedReader reader = new BufferedReader (new FileReader (marketFile));
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
				ShopData data = gson.fromJson (temp,ShopData.class);
				playerShops.put (name,data);
				return data;
			}
			return null;
		} else
			return playerShops.get (name);
	}

	public static void saveKit (Kit kit) {
		if (!kitLocation.exists ())
			kitLocation.mkdirs ();
		File kitFile = new File (kitLocation + File.separator + kit.getName () + ".json");
		try {
			kitFile.createNewFile ();
			Files.write (Paths.get (kitFile.getAbsolutePath ()),gson.toJson (kit).getBytes ());
			loadAllKits ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}

	public static void loadAllKits () {
		if (kitLocation.exists ())
			for (File file : kitLocation.listFiles ()) {
				ArrayList <String> lines = new ArrayList <> ();
				try {
					BufferedReader reader = new BufferedReader (new FileReader (file));
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
				Kit kit = gson.fromJson (temp,Kit.class);
				if (kit != null && !loadedKits.contains (kit))
					loadedKits.add (kit);
			}
	}

	public static void setSpy (UUID name,boolean spy) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setSpy (spy);
			if (spy)
				spys.add (name);
			else
				spys.remove (name);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}

	public static void setNickname (UUID name,String nick) {
		PlayerData data = getPlayerData (name);
		if (data == null)
			data = loadPlayerData (name);
		if (data != null) {
			File playerFileLocation = new File (playerDataLocation + File.separator + name.toString () + ".json");
			data.setNickname (nick);
			try {
				Files.write (Paths.get (playerFileLocation.getAbsolutePath ()),gson.toJson (data).getBytes ());
				reloadPlayerData (name);
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
	}
}
