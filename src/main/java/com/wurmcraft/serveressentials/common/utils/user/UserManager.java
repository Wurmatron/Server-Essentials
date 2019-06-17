package com.wurmcraft.serveressentials.common.utils.user;

import static com.wurmcraft.serveressentials.common.storage.rest.RestWorldEvents.rankChangeCache;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.user.eco.Bank;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.ServerTime;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
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
    return null;
  }

  public static Rank getUserRank(EntityPlayer player) {
    return getUserRank(player.getGameProfile().getId().toString());
  }

  private static Rank getFileRank(String uuid) {
    return ((FileUser) getUserData(uuid)[0]).getRank();
  }

  private static Rank getRestRank(String uuid) {
    return getUserData(uuid).length > 0
        ? ServerEssentialsAPI.rankManager.getRank(((GlobalRestUser) getUserData(uuid)[0]).rank)
        : null;
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
    // TODO Announce User Rank-Up
  }

  public static Channel getUserChannel(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) UserManager.getUserData(uuid)[1];
      return (Channel) DataHelper.get(Storage.CHANNEL, user.getCurrentChannel());
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
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      return ((LocalRestUser) getUserData(user)[1]).getIgnored();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      return ((FileUser) getUserData(user)[0]).getIgnored();
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
}
