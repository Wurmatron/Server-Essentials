package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;

import java.util.*;

/**
 * Created by matthew on 6/24/17.
 */
public class UsernameResolver {
    public static final class AbstractUsernameCollection<T extends String> extends AbstractCollection<T> {
        List<T> names;
        {
            names = new LinkedList<>();
        }
        public AbstractUsernameCollection(Collection<T> values) {
            for (T value : values) add(value);
        }
        @Override
        public boolean contains(Object s) {
            Iterator<T> itr = this.iterator();
            if(s == null) {
                while(itr.hasNext()) {
                    if(itr.next() == null) {
                        return true;
                    }
                }
            } else {
                while(itr.hasNext()) {
                    if(((String)s).equalsIgnoreCase(itr.next())) {
                        return true;
                    }
                }
            }
            return false;
        }
        @Override
        public Iterator<T> iterator() {return names.iterator();}
        @Override
        public boolean add(T s) {
            if (!contains(s)) {
                names.add(s);
                return true;
            } else return false;
        }
        @Override
        public int size() {
            return 0;
        }
    }

    public static boolean isValidPlayer(String username) {
        return new AbstractUsernameCollection<String>(UsernameCache.getMap().values()).contains(username);
    }

    public static boolean isNickname(String username) {
        return username.equalsIgnoreCase(getNickname(getPlayerUUID(username)));
    }

    public static String getNickname(UUID uuid) {
        return DataHelper.loadPlayerData(uuid).getNickname();
    }

    public static PlayerData getPlayerData(UUID uniqueID) {
        return DataHelper.loadedPlayers.get(uniqueID);
    }

    public static PlayerData getPlayerData(String username) {
        Set<UUID> uuids = UsernameCache.getMap().keySet();
        for (UUID uuid : uuids) if (UsernameCache.getMap().get(uuid).equalsIgnoreCase(username))
            return DataHelper.loadedPlayers.get(uuid);
        return null;
    }

    public static UUID getPlayerUUID(String username) {
        Set<UUID> uuids = UsernameCache.getMap().keySet();
        for (UUID uuid : uuids) if (UsernameCache.getMap().get(uuid).equalsIgnoreCase(username)) return uuid;
        return null;
    }

    public static String getUsername(UUID uniqueID) {
        return UsernameCache.getMap().get(uniqueID);
    }

    public static EntityPlayer getPlayer(MinecraftServer server, String username) {
        return isValidPlayer(username) ? server.getPlayerList().getPlayerByUsername(username) : null;
    }

    public static EntityPlayer getPlayer(MinecraftServer server, UUID uuid){
        return server.getPlayerList().getPlayerByUUID(uuid);
    }
}
