package wurmcraft.serveressentials.common.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.config.ConfigHandler;

public class ChannelManager {

  private static List<Channel> channels = new ArrayList<>();
  private static HashMap<String, Channel> nameChannel = new HashMap<>();
  private static HashMap<UUID, String> playerChannels = new HashMap<>();
  private static HashMap<String, List<UUID>> channelPlayers = new HashMap<>();

  public static void addChannel(Channel channel) {
    if (channel != null && channel.getName() != null) {
      channels.add(channel);
      nameChannel.put(channel.getName(), channel);
    }
  }

  public static List<IDataType> getChannels() {
    return Collections.unmodifiableList(channels);
  }

  public static Channel getFromName(String name) {
    return nameChannel.get(name);
  }

  public static Channel getDefaultChannel() {
    Channel defaultChannel = getFromName(ConfigHandler.defaultChannel);
		if (defaultChannel != null) {
			return defaultChannel;
		} else if (channels.size() > 0) {
			return channels.get(0);
		} else {
			ConfigHandler.createDefaultChannels();
			return getDefaultChannel();
		}
  }

  public static void setPlayerChannel(UUID uuid, Channel channel) {
		if (playerChannels.containsKey(uuid)) {
			playerChannels.replace(uuid, channel.getName());
		} else {
			playerChannels.put(uuid, channel.getName());
		}
    List<UUID> players = channelPlayers.get(channel.getName());
    if (players != null && players.size() > 0) {
      boolean found = false;
			for (UUID id : players) {
				if (id.equals(uuid)) {
					found = true;
				}
			}
			if (!found) {
				players.add(uuid);
			}
      channelPlayers.replace(channel.getName(), players);
    } else {
      List<UUID> playerList = new ArrayList<>();
      playerList.add(uuid);
      channelPlayers.put(channel.getName(), playerList);
    }
  }

  public static Channel getPlayerChannel(UUID uuid) {
    Channel channel = getFromName(playerChannels.get(uuid));
		if (channel != null) {
			return channel;
		} else {
			setPlayerChannel(uuid, getDefaultChannel());
			return getFromName(playerChannels.get(uuid));
		}
  }

  public static void removePlayerChannel(UUID uuid, Channel channel) {
    playerChannels.remove(uuid);
		if (channel != null && channelPlayers.get(channel.getName()) != null) {
			channelPlayers.get(channel.getName()).remove(uuid);
		}
  }

  public static List<UUID> getPlayersInChannel(Channel channel) {
    return channelPlayers.get(channel.getName());
  }
}
