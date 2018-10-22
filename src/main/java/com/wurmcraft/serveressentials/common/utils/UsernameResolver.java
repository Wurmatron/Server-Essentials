package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class UsernameResolver {

  private UsernameResolver() {}

  public static EntityPlayer getPlayer(String name) {
    UUID uuid = getUUIDFromName(name);
    return FMLCommonHandler.instance()
        .getMinecraftServerInstance()
        .getPlayerList()
        .getPlayerByUUID(uuid);
  }

  public static String getNick(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")
        && UserManager.getPlayerData(uuid).length > 0) {
      return ((GlobalUser) UserManager.getPlayerData(uuid)[0]).getNick();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")
        && UserManager.getPlayerData(uuid) != null
        && UserManager.getPlayerData(uuid).length > 0
        && ((PlayerData) UserManager.getPlayerData(uuid)[0]).getNickname().length() > 0) {
      return ((PlayerData) UserManager.getPlayerData(uuid)[0]).getNickname();
    }
    return getUsername(uuid);
  }

  public static UUID getUUIDFromName(String username) {
    String nick = usernameFromNickname(username);
    if (!nick.equalsIgnoreCase(username)) {
      username = usernameFromNickname(username);
    }
    Set<UUID> uuids = UsernameCache.getMap().keySet();
    for (UUID uuid : uuids) {
      if (UsernameCache.getMap().get(uuid).equalsIgnoreCase(username)) {
        return uuid;
      }
    }
    return null;
  }

  public static String getUsername(UUID uniqueID) {
    return UsernameCache.getMap().get(uniqueID);
  }

  public static String usernameFromNickname(String username) {
    String unFormattedName =
        TextFormatting.getTextWithoutFormattingCodes(username).replaceAll("\\*", "");
    if (new AbstractUsernameCollection(UsernameCache.getMap().values()).contains(unFormattedName)) {
      return unFormattedName;
    } else {
      for (UUID uid : UsernameCache.getMap().keySet()) {
        String nick = getNick(uid);
        if (nick != null && nick.equalsIgnoreCase(unFormattedName)) {
          return getUsername(uid);
        }
      }
      return "";
    }
  }

  public static final class AbstractUsernameCollection<T extends String>
      extends AbstractCollection<T> {

    List<T> names = new LinkedList<>();

    public AbstractUsernameCollection(Collection<T> values) {
      for (T value : values) {
        add(value);
      }
    }

    @Override
    public boolean contains(Object s) {
      Iterator<T> itr = this.iterator();
      if (s == null) {
        return false;
      } else {
        while (itr.hasNext()) {
          if (((String) s).equalsIgnoreCase(itr.next())) {
            return true;
          }
        }
      }
      return false;
    }

    @Override
    public Iterator<T> iterator() {
      return names.iterator();
    }

    @Override
    public boolean add(T s) {
      if (!contains(s)) {
        names.add(s);
        return true;
      } else {
        return false;
      }
    }

    @Override
    public int size() {
      return names.size();
    }
  }
}
