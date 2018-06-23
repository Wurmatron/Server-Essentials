package wurmcraft.serveressentials.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraftforge.common.config.Config;
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
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.TeamManager;

@Config(modid = Global.MODID)
public class ConfigHandler {

  @Config.Ignore
  public static final File saveLocation = new File(
      "." + File.separator + Global.NAME.replaceAll(" ", "-"));

  @Config.Comment("Enable debug mode")
  public static boolean debug = false;

  @Config.Comment("Default Home Name")
  public static String homeName = "default";

  @Config.Comment("Time between teleportation (seconds)")
  public static int teleportCooldown = 5;

  @Config.Comment("Respawn Point (home, spawn, default")
  public static String respawnPoint = "home";

  @Config.Comment("Timeout for Tpa request")
  public static int tpaTimeout = 120;

  @Config.Comment("Default Channel to join")
  public static String defaultChannel = "Global";

  @Config.Comment("Force default channel on join")
  public static boolean forceDefaultChannelOnJoin = true;

  @Config.Comment("How Chat is formatted")
  public static String chatFormat =
      "%channel% " + "[%team%] " + "%rankPrefix% " + "%username% " + "%rankSuffix% " + "%message%";

  @Config.Comment("String used for currency")
  public static String currencySymbol = "$";

  @Config.Comment("How the message command is displayed")
  public static String msgFormat = "%username% - > %message%";

  @Config.Comment("Used by Security module for certain command if its enabled")
  public static String trustedStaff = "";

  @Config.Comment("Makes it harder to change up the ranks along with a few abuse checks")
  public static boolean securityModule = false;

  @Config.Comment("Used by OnlineTime Ranking System and simular commands")
  public static int topMaxDisplay = 20;

  @Config.Comment("Log Chat to a file")
  public static boolean logChat = true;

  @Config.Comment("How long before saving chat")
  public static int logInterval = 30;

  @Config.Comment("Max of same message a user is allowed to type before it is blocked")
  public static int spamLimit = 3;

  @Config.Comment("How the mail command is displated")
  public static String mailFormat = "%username% -> %message%";

  @Config.Comment("Allow the creation of buy signs")
  public static boolean buySign = true;

  @Config.Comment("Allow the creation of sell signs")
  public static boolean sellSign = true;

  @Config.Comment("Allow the creation of color signs")
  public static boolean colorSign = true;

  @Config.Comment("Allow the creation of command signs")
  public static boolean commandSign = true;

  @Config.Comment("Default Server Lang")
  public static String defaultLang = "en_us";

  @Config.Comment("Claiming is enabled?")
  public static boolean claimingEnabled = true;

  public static void createDefaultChannels() {
    Channel globalChannel = new Channel(defaultChannel, "&9[G]", true, false, Channel.Type.PUBLIC,
        "", new String[]{"Wurmatron Wurm",
        "\"Demi San\" \"Demi God\""});
    Channel staffChannel = new Channel("Staff", "&4[S]", false, false, Channel.Type.RANK, "Admin",
        null);
    Channel teamChannel = new Channel("Team", "&a[T]", true, false, Channel.Type.TEAM, "", null);
    DataHelper2.createIfNonExist(Keys.CHANNEL, globalChannel);
    DataHelper2.createIfNonExist(Keys.CHANNEL, staffChannel);
    DataHelper2.createIfNonExist(Keys.CHANNEL, teamChannel);
  }

  public static void createDefaultRank() {
    File groupLocation = new File(saveLocation + File.separator + Keys.RANK.name());
    if (!groupLocation.exists() || groupLocation.listFiles().length <= 0) {
      Rank defaultGroup = new Rank("Default", true, "[Default]", "", null,
          new String[]{"common.*", "teleport.*", "claim.*"});
      Rank memberGroup = new Rank("Member", false, "[Member]", "", new String[]{"Default"},
          new String[]{"perk.*"});
      Rank adminGroup = new Rank("Admin", false, "[Admin]", "",
          new String[]{defaultGroup.getName()}, new String[]{"*"});
      DataHelper2.createIfNonExist(Keys.RANK, defaultGroup);
      DataHelper2.createIfNonExist(Keys.RANK, adminGroup);
      DataHelper2.createIfNonExist(Keys.RANK, memberGroup);
      loadRanks();
    } else {
      groupLocation.mkdirs();
    }
  }

  public static void loadAllKits() {
    File kitLocation = new File(saveLocation + File.separator + Keys.KIT.name());
    if (kitLocation.exists()) {
      for (File file : kitLocation.listFiles()) {
        DataHelper2.load(file, Keys.KIT, new Kit());
      }
    } else {
      kitLocation.mkdirs();
    }
  }

  public static void loadAllAutoRanks() {
    File autoRankLocation = new File(saveLocation + File.separator + Keys.AUTO_RANK.name());
    if (autoRankLocation.exists()) {
      for (File file : autoRankLocation.listFiles()) {
        DataHelper2.load(file, Keys.AUTO_RANK, new AutoRank());
      }
    } else {
      autoRankLocation.mkdirs();
    }
  }

  public static void loadAllTeams() {
    File teamLoction = new File(saveLocation + File.separator + Keys.TEAM.name());
    if (teamLoction.exists()) {
      for (File file : teamLoction.listFiles()) {
        Team team = DataHelper2.load(file, Keys.TEAM, new Team());
        TeamManager.register(team);
      }
    } else {
      teamLoction.mkdirs();
    }
  }

  public static void loadRanks() {
    File rankLoction = new File(saveLocation + File.separator + Keys.RANK.name());
    if (rankLoction.exists()) {
      for (File file : rankLoction.listFiles()) {
        Rank rank = DataHelper2.load(file, Keys.TEAM, new Rank());
        RankManager.registerRank(rank);
      }
    } else {
      rankLoction.mkdirs();
    }
  }

  public static void createDefaultAutoRank() {
    File autoRankLocation = new File(saveLocation + File.separator + Keys.AUTO_RANK.name());
    if (!autoRankLocation.exists() || autoRankLocation.listFiles().length <= 0) {
      AutoRank defaultToMember = new AutoRank(30, 0, 10, "Default", "Member");
      DataHelper2.createIfNonExist(Keys.AUTO_RANK, defaultToMember);
      loadRanks();
    }
  }

  public static void loadAllChannels() {
    File channelLocation = new File(saveLocation + File.separator + Keys.CHANNEL.name());
    if (channelLocation.exists()) {
      for (File file : channelLocation.listFiles()) {
        Channel ch = DataHelper2.load(file, Keys.CHANNEL, new Channel());
        ChannelManager.addChannel(ch);
      }
    } else {
      channelLocation.mkdirs();
    }
  }

  public static void loadGlobal() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File globalLocation = new File(saveLocation + File.separator + "Global.json");
    if (globalLocation.exists()) {
      ArrayList<String> lines = new ArrayList<>();
      try {
        BufferedReader reader = new BufferedReader(new FileReader(globalLocation));
        String line;
        while ((line = reader.readLine()) != null) {
          lines.add(line);
        }
        reader.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      String temp = "";
      for (int s = 0; s <= lines.size() - 1; s++) {
        temp = temp + lines.get(s);
      }
      DataHelper2.globalSettings = gson
          .fromJson(temp, wurmcraft.serveressentials.common.api.storage.Global.class);
    } else {
      wurmcraft.serveressentials.common.api.storage.Global global = new wurmcraft.serveressentials.common.api.storage.Global(
          null, new String[]{}, new String[]{}, "https://github.com/Wurmcraft/Server-Essentials");
      DataHelper2.forceSave(saveLocation, global);
      loadGlobal();
    }
  }

  public static void loadWarps() {
    File warpLocation = new File(saveLocation + File.separator + Keys.WARP.name());
    if (warpLocation.exists()) {
      for (File file : warpLocation.listFiles()) {
        DataHelper2.load(file, Keys.WARP, new Warp());
      }
    } else {
      warpLocation.mkdirs();
    }
  }

}
