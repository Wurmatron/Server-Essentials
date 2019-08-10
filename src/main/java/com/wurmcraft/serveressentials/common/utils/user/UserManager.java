package com.wurmcraft.serveressentials.common.utils.user;

import static com.wurmcraft.serveressentials.common.storage.rest.RestWorldEvents.rankChangeCache;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.storage.json.Kit;
import com.wurmcraft.serveressentials.api.user.eco.Bank;
import com.wurmcraft.serveressentials.api.user.eco.Coin;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.ServerTime;
import com.wurmcraft.serveressentials.api.user.storage.Home;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.matterbridge.api.json.MBMessage;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator.MatterBridge;
import java.util.*;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class UserManager {

  private static NonBlockingHashMap<String, Object[]> loadedUserData = new NonBlockingHashMap<>();

  public static Object[] getUserData(String uuid) {
    return loadedUserData.getOrDefault(uuid, new Object[0]);
  }

  public static Object[] getUserData(UUID uuid) {
    return getUserData(uuid.toString());
  }

  public static Object[] getUserData(EntityPlayer player) {
    return getUserData(player.getGameProfile().getId().toString());
  }

  public static boolean isUserLoaded(String uuid) {
    return getUserData(uuid) != null && getUserData(uuid).length > 0;
  }

  public static void setUserData(String uuid, Object[] data) {
    loadedUserData.put(uuid, data);
  }

  public static void setUserData(UUID uuid, Object[] data) {
    setUserData(uuid.toString(), data);
  }

  public static Rank getUserRank(String uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      return getRestRank(uuid);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      return getFileRank(uuid);
    }
    return getDefaultRank();
  }

  public static Rank getUserRank(EntityPlayer player) {
    return getUserRank(player.getGameProfile().getId().toString());
  }

  private static Rank getFileRank(String uuid) {
    return ((FileUser) getUserData(uuid)[0]).getRank();
  }

  private static Rank getRestRank(String uuid) {
    Rank rank =
        getUserData(uuid).length > 0
            ? ServerEssentialsAPI.rankManager.getRank(((GlobalRestUser) getUserData(uuid)[0]).rank)
            : null;
    if (rank == null) {
      ServerEssentialsServer.LOGGER.error(
          "Unable to load '" + uuid + "s' rank defaulting to " + ConfigHandler.defaultRank);
      rank = getDefaultRank();
    }
    return rank;
  }

  public static void deleteUser(UUID uuid) {
    loadedUserData.remove(uuid);
  }

  public static Rank getDefaultRank() {
    return ServerEssentialsAPI.rankManager.getRank(ConfigHandler.defaultRank);
  }

  public static double getServerCurrency(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(uuid)[0];
      return user.getBank().getCurrency(ConfigHandler.serverCurrency);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(uuid)[0];
      return user.getMoney();
    }
    return -1;
  }

  public static void rankupUser(EntityPlayer player, Rank rank) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      rankChangeCache.put(player.getGameProfile().getId().toString(), rank.getID());
      GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(player)[0];
      user.setRank(rank.getID());
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(player)[0];
      user.setRank(rank);
      DataHelper.save(Storage.USER, user);
    }
    for (EntityPlayerMP randomPlayer :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      ChatHelper.sendMessage(
          randomPlayer,
          LanguageModule.getUserLanguage(randomPlayer)
              .local
              .CHAT_RANKUP
              .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
              .replaceAll(Replacment.RANK, rank.getPrefix()));
    }
    if (ServerEssentialsAPI.isModuleLoaded("MatterBridge")) {
      MatterBridge.sendMessage(
          new MBMessage(
              "",
              LanguageModule.getDefaultLang()
                  .local
                  .CHAT_RANKUP
                  .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                  .replaceAll(Replacment.RANK, rank.getPrefix()),
              ConfigHandler.serverName,
              ConfigHandler.serverName,
              "",
              ""));
    }
  }

  public static Channel getUserChannel(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) UserManager.getUserData(uuid)[1];
      if (user != null && user.getCurrentChannel() != null) {
        return (Channel) DataHelper.get(Storage.CHANNEL, user.getCurrentChannel());
      } else {
        ServerEssentialsServer.LOGGER.error("Unable to load '" + uuid + "'s Channel!");
        return DataHelper.get(Storage.CHANNEL, ConfigHandler.defaultChannel, new Channel());
      }
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(uuid)[0];
      return (Channel) DataHelper.get(Storage.CHANNEL, user.getCurrentChannel());
    }
    return null;
  }

  public static Channel getUserChannel(EntityPlayer player) {
    return getUserChannel(player.getGameProfile().getId());
  }

  public static boolean isUserMuted(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      return ((GlobalRestUser) UserManager.getUserData(uuid)[0]).isMuted();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      return ((FileUser) UserManager.getUserData(uuid)[0]).isMuted();
    }
    return false;
  }

  public static boolean isIgnored(UUID user, String msg) {
    for (String ignore : getIgnored(user)) {
      if (msg.contains(ignore)) {
        return true;
      }
    }
    return false;
  }

  private static String[] getIgnored(UUID user) {
    if (getUserData(user) != null && getUserData(user).length > 0) {
      if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")
          && getUserData(user).length >= 2
          && getUserData(user)[1] != null) {
        return ((LocalRestUser) getUserData(user)[1]).getIgnored();
      } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
        return ((FileUser) getUserData(user)[0]).getIgnored();
      }
    }
    return new String[0];
  }

  public static String getUserTeam(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(uuid)[0];
      return user.getTeam();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(uuid)[0];
      return user.getTeam();
    }
    return "";
  }

  public static String getUserTeam(EntityPlayer player) {
    return getUserTeam(player.getGameProfile().getId());
  }

  public static String getNickname(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      if (UserManager.getUserData(uuid).length > 0) {
        GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(uuid)[0];
        if (user != null) {
          return user.getNick();
        }
      }
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      if (UserManager.getUserData(uuid).length > 0) {
        FileUser user = (FileUser) UserManager.getUserData(uuid)[0];
        if (user != null) {
          return user.getNickname();
        }
      }
    }
    return "";
  }

  public static String getNickname(EntityPlayer player) {
    return getNickname(player.getGameProfile().getId());
  }

  public static void setUserCurrency(EntityPlayer player, int amount) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player.getGameProfile().getId())[0];
      Bank bank = user.getBank();
      bank.earn(ConfigHandler.serverCurrency, amount);
      user.setBank(bank);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player.getGameProfile().getId())[0];
      user.setMoney(amount);
      DataHelper.save(Storage.USER, user);
    }
  }

  public static void addServerTime(EntityPlayer player, int amount) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player.getGameProfile().getId())[0];
      ServerTime time = user.getServerData(ConfigHandler.serverName);
      time.setOnlineTime(time.getOnlineTime() + amount);
      user.addServerData(time);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player.getGameProfile().getId())[0];
      user.setOnlineTime(user.getOnlineTime() + amount);
      DataHelper.save(Storage.USER, user);
    }
  }

  public static void setUserChannel(EntityPlayer player, Channel channel) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player.getGameProfile().getId())[1];
      user.setCurrentChannel(channel.getID());
      DataHelper.save(Storage.LOCAL_USER, user);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player.getGameProfile().getId())[0];
      user.setCurrentChannel(channel);
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static void setNickName(EntityPlayer player, String nick) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.setNick(nick);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      user.setNickname(nick);
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static void setLastLocation(EntityPlayer player, LocationWrapper pos) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      user.setLastLocation(pos);
      DataHelper.save(Storage.LOCAL_USER, user);
      DataHelper.addData(Storage.LOCAL_USER, user);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      user.setLastLocation(pos);
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static void setLanguage(EntityPlayer player, Lang lang) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.setLang(lang.getID());
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      user.setLang(lang.getID());
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static void setRank(EntityPlayer player, Rank rank) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.setRank(rank.getID());
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      user.setRank(rank);
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static String[] getPerms(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      return user.getPermission();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      return new String[0];
    }
    return new String[0];
  }

  public static String[] getPerks(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      return user.getPerks();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.getCustomData();
    }
    return new String[0];
  }

  public static void addPerm(EntityPlayer player, String... perm) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.addPermission(perm);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    }
  }

  public static void addPerk(EntityPlayer player, String... perk) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.addPerk(perk);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      for (String p : perk) {
        user.addCustomData(p);
      }
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static void delPerm(EntityPlayer player, String... perm) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      for (String p : perm) {
        user.delPermission(p);
      }
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    }
  }

  public static void delPerk(EntityPlayer player, String... perk) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      for (String p : perk) {
        user.delPerk(p);
      }
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      for (String p : perk) {
        user.delCustomData(p);
      }
      DataHelper.save(Storage.USER, user);
      DataHelper.addData(Storage.USER, user);
    }
  }

  public static long getLastSeen(String type, EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      if (type.equalsIgnoreCase("Global") || type.equalsIgnoreCase("Network")) {
        GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
        long newest = user.getServerData(ConfigHandler.serverName).getLastSeen();
        for (ServerTime time : user.getServerData()) {
          if (newest > time.getLastSeen()) {
            newest = time.getLastSeen();
          }
        }
        return newest;
      } else if (type.equalsIgnoreCase("Local") || type.equalsIgnoreCase("Server")) {
        GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
        return user.getServerData(ConfigHandler.serverName).getLastSeen();
      }
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.getLastseen();
    }
    return -1;
  }

  public static boolean hasPerm(EntityPlayer player, String perm) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      return user.hasPerm(perm);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.hasPerm(perm);
    }
    return false;
  }

  public static long getLastTeleport(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return user.getTeleportTimer();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.getTeleportTimer();
    }
    return Long.MIN_VALUE;
  }

  public static boolean isAcceptingTPARequests(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return !user.isTpLock();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return !user.isTpLock();
    }
    return false;
  }

  public static boolean toggleTPA(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      user.setTpLock(!user.isTpLock());
      return user.isTpLock();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      user.setTpLock(!user.isTpLock());
      return user.isTpLock();
    }
    return false;
  }

  public static int getMaxHomes(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      return getHomesFromPerks(player, user.getPerks());
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return getHomesFromPerks(player, user.getCustomData());
    }
    return 1;
  }

  private static int getHomesFromPerks(EntityPlayer player, String[] perks) {
    for (String perk : perks) {
      if (perk.startsWith("home.amount")) {
        try {
          return Integer.parseInt(perk.substring(perk.lastIndexOf(".") + 1));
        } catch (Exception e) {
          ServerEssentialsServer.LOGGER.warn(
              player.getDisplayNameString() + " has a invalid Max Home Perk, Unable to Read");
        }
      }
    }
    return 1;
  }

  public static boolean setHome(EntityPlayer player, Home home) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser local = (LocalRestUser) getUserData(player)[1];
      int maxHomes = getMaxHomes(player);
      if (local.getHomes().length + 1 < maxHomes) {
        return false;
      }
      boolean overrideHome = false;
      for (Home h : local.getHomes()) {
        if (h.getName().equalsIgnoreCase(home.getName())) {
          overrideHome = true;
        }
      }
      if (overrideHome) {
        List<Home> homes = new ArrayList<>();
        for (Home h : local.getHomes()) {
          if (h.getName().equalsIgnoreCase(home.getName())) {
            homes.add(home);
          } else {
            homes.add(h);
          }
        }
      } else {
        local.addHome(home);
      }
      DataHelper.save(Storage.LOCAL_USER, local);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[1];
      int maxHomes = getMaxHomes(player);
      if (user.getHomes().length + 1 < maxHomes) {
        return false;
      }
      boolean overrideHome = false;
      for (Home h : user.getHomes()) {
        if (h.getName().equalsIgnoreCase(home.getName())) {
          overrideHome = true;
        }
      }
      if (overrideHome) {
        List<Home> homes = new ArrayList<>();
        for (Home h : user.getHomes()) {
          if (h.getName().equalsIgnoreCase(home.getName())) {
            homes.add(home);
          } else {
            homes.add(h);
          }
        }
      } else {
        user.addHome(home);
      }
      DataHelper.save(Storage.USER, user);
    }
    return true;
  }

  public static Home getHome(EntityPlayer player, String name) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return Arrays.stream(user.getHomes())
          .filter(h -> h.getName().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return Arrays.stream(user.getHomes())
          .filter(h -> h.getName().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  public static Home[] getHomes(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return user.getHomes();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.getHomes();
    }
    return new Home[0];
  }

  public static void delHome(EntityPlayer player, Home home) {
    if (home != null) {
      if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
        LocalRestUser user = (LocalRestUser) getUserData(player)[1];
        user.delHome(home.getName());
        DataHelper.save(Storage.LOCAL_USER, user);
      } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
        FileUser user = (FileUser) getUserData(player)[0];
        user.delHome(home.getName());
        DataHelper.save(Storage.USER, user);
      }
    }
  }

  public static LocationWrapper getLastLocation(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return user.getLastLocation();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.getLastLocation();
    }
    return new LocationWrapper(player.getPosition(), player.dimension);
  }

  public static Coin[] getUserCoins(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      return user.getBank().getCoin();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      //      return user.getBank();
    }
    return new Coin[0];
  }

  public static double spendCurrency(EntityPlayer player, String currency, double amount) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.getBank().spend(currency, amount);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
      return amount;
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      //      FileUser user = (FileUser) getUserData([0]);
    }
    return -1;
  }

  public static double earnCurrency(EntityPlayer player, String currency, double amount) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      user.getBank().earn(currency, amount);
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
      return amount;
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      //      FileUser user = (FileUser) getUserData([0]);
    }
    return -1;
  }

  public static boolean canBuy(EntityPlayer player, String currency, double amount) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(player)[0];
      return user.getBank().getCurrency(currency) >= amount;
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      //      FileUser user = (FileUser) getUserData([0]);
    }
    return false;
  }

  public static int getRewards(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return user.getRewardPoints();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      // Invalid for file Type
    }
    return 0;
  }

  public static void consumeReward(EntityPlayer player, int amount) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      user.consumePoint(amount);
      DataHelper.save(Storage.LOCAL_USER, user);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      // Invalid for file Type
    }
  }

  public static boolean canUseKit(EntityPlayer player, Kit kit) {
    return lastKitUse(player, kit) + (kit.timer * 60000) < System.currentTimeMillis();
  }

  public static long lastKitUse(EntityPlayer player, Kit kit) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      return user.getKitUsage().getOrDefault(kit.name, 0L);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      return user.getKitUsage().getOrDefault(kit.name, 0L);
    }
    return 0L;
  }

  public static void useKit(EntityPlayer player, Kit kit) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) getUserData(player)[1];
      user.useKit(kit.name);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(player)[0];
      user.useKit(kit.name);
    }
  }
}
