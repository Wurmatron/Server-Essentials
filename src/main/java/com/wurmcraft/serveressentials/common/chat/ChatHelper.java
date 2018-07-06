package com.wurmcraft.serveressentials.common.chat;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;

public class ChatHelper {

  public static final String USERNAME_KEY = "%username%";
  public static final String CHANNEL_KEY = "%channel%";
  public static final String MESSAGE_KEY = "%message%";
  public static final String DIMENSION_KEY = "%dimension%";
  public static final String RANK_PREFIX_KEY = "%rankPrefix%";
  public static final String RANK_SUFFIX_KEY = "%rankSuffix%";
  public static final String TEAM_KEY = "%team%";

  public static HashMap<String, String[]> lastChat = new HashMap<>();

  public static String format(String username, Rank rank, Channel channel, int dimension,
      ITeam team, String message) {
    String format;
    if (team != null) {
      if (rank.getSuffix() != null && !rank.getSuffix().equals("")) {
        format = StringUtils.replaceEach(ConfigHandler.chatFormat,
            new String[]{USERNAME_KEY, CHANNEL_KEY, MESSAGE_KEY, DIMENSION_KEY, RANK_PREFIX_KEY,
                RANK_SUFFIX_KEY, TEAM_KEY}, new String[]{username,
                channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET, message,
                Integer.toString(dimension),
                rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                rank.getSuffix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                TextFormatting.GRAY + team.getName() + TextFormatting.RESET});
      } else {
        format = StringUtils
            .replaceEach(ConfigHandler.chatFormat.replaceAll(" " + RANK_SUFFIX_KEY, ""),
                new String[]{USERNAME_KEY, CHANNEL_KEY, MESSAGE_KEY, DIMENSION_KEY, RANK_PREFIX_KEY,
                    TEAM_KEY}, new String[]{username,
                    channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET, message,
                    Integer.toString(dimension),
                    rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                    TextFormatting.GRAY + team.getName() + TextFormatting.RESET});
      }
    } else {
      if (rank.getSuffix() != null && !rank.getSuffix().equals("")) {
        format = StringUtils.replaceEach(ConfigHandler.chatFormat.replaceAll(TEAM_KEY, ""),
            new String[]{USERNAME_KEY, CHANNEL_KEY, MESSAGE_KEY, DIMENSION_KEY, RANK_PREFIX_KEY,
                RANK_SUFFIX_KEY}, new String[]{username,
                channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET, message,
                Integer.toString(dimension),
                rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                rank.getSuffix().replaceAll("&", "\u00A7") + TextFormatting.RESET});
      } else {
        format = StringUtils.replaceEach(
            ConfigHandler.chatFormat.replaceAll(TEAM_KEY, "").replaceAll(" " + RANK_SUFFIX_KEY, ""),
            new String[]{USERNAME_KEY, CHANNEL_KEY, MESSAGE_KEY, DIMENSION_KEY, RANK_PREFIX_KEY},
            new String[]{username,
                channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET, message,
                Integer.toString(dimension),
                rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET});
      }
    }
    return format;
  }

  public static void sendChannelMessage(Channel channel, String displayName, Rank rank,
      int dimension, ITeam team, String message) {
    PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
    message = applyFilter(channel, message);
    if (!channel.getName().equalsIgnoreCase("Team")) {
      for (EntityPlayerMP player : players.getPlayers()) {
        ITextComponent textComponent = ForgeHooks.newChatWithLinks(
            format(displayName, rank, channel, dimension, team, message));
        player.sendMessage(textComponent);
      }
    }
  }

  private static String applyFilter(Channel ch, String message) {
    if (ch.getFilter() != null && ch.getFilter().length > 0) {
      for (String filter : ch.getFilter()) {
        String[] replace = unpackFilter(filter);
        if (replace != null && replace.length > 0) {
          message = message.replaceAll("(?i)" + replace[0], replace[1]);
        }
      }
    }
    return message;
  }

  private static String[] unpackFilter(String filter) {
    if (!filter.contains("\"")) {
      String[] temp = filter.split(" ");
      return new String[]{temp[0], temp[1]};
    }
    if (filter.contains("\"")) {
      if (filter.length() - filter.replaceAll("\"", "").length() == 2) {
        String[] temp = filter.split("\"");
        return new String[]{temp[1], temp[3]};
      } else if (filter.length() - filter.replaceAll("\"", "").length() == 4) {
        String[] temp = filter.split("\"");
        return new String[]{temp[1], temp[3]};
      }
    }
    return null;
  }
}
