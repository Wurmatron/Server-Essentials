package com.wurmcraft.serveressentials.common.chat;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.utils.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
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

  private ChatHelper() {}

  public static String format(
      String username, Rank rank, Channel channel, int dimension, ITeam team, String message) {
    String format = "";
    if (team != null && team.getName() != null && team.getName().length() > 0) {
      if (rank.getSuffix() != null && !rank.getSuffix().equals("")) {
        if (channel.getFilter() != null) {
          for (String replacement : channel.getFilter()) {
            String[] split = replacement.split(" ");
            message = message.replaceAll(split[0], split[1]);
          }
        }
        format =
            StringUtils.replaceEach(
                ConfigHandler.chatFormat,
                new String[] {
                  USERNAME_KEY,
                  CHANNEL_KEY,
                  MESSAGE_KEY,
                  DIMENSION_KEY,
                  RANK_PREFIX_KEY,
                  RANK_SUFFIX_KEY,
                  TEAM_KEY
                },
                new String[] {
                  username,
                  channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                  message.replaceAll("&", "\u00A7"),
                  Integer.toString(dimension),
                  rank.getPrefix().replaceAll("&", "\u00A7"),
                  rank.getSuffix().replaceAll("&", "\u00A7"),
                  team.getName().length() > 0
                      ? TextFormatting.GRAY
                          + team.getName().substring(0, 1).toUpperCase()
                          + team.getName().substring(1).toLowerCase()
                          + TextFormatting.RESET
                      : ""
                });
      } else {
        if (channel.getFilter() != null) {
          for (String replacement : channel.getFilter()) {
            String[] split = replacement.split(" ");
            message = message.replaceAll(split[0], split[1]);
          }
        }
        StringUtils.replaceEach(
            ConfigHandler.chatFormat.replaceAll(" " + RANK_SUFFIX_KEY, ""),
            new String[] {
              USERNAME_KEY, CHANNEL_KEY, MESSAGE_KEY, DIMENSION_KEY, RANK_PREFIX_KEY, TEAM_KEY
            },
            new String[] {
              username,
              channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
              message,
              Integer.toString(dimension),
              rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
              team.getName().length() > 0
                  ? TextFormatting.GRAY
                      + team.getName().substring(0, 1).toUpperCase()
                      + team.getName().substring(1, team.getName().length()).toLowerCase()
                      + TextFormatting.RESET
                  : ""
            });
      }
    } else {
      if (rank.getSuffix() != null && !rank.getSuffix().equals("")) {
        if (channel.getFilter() != null) {
          for (String replacement : channel.getFilter()) {
            String[] split = replacement.split(" ");
            message = message.replaceAll(split[0], split[1]);
          }
        }
        format =
            StringUtils.replaceEach(
                ConfigHandler.chatFormat.replaceAll(TEAM_KEY, ""),
                new String[] {
                  USERNAME_KEY,
                  CHANNEL_KEY,
                  MESSAGE_KEY,
                  DIMENSION_KEY,
                  RANK_PREFIX_KEY,
                  RANK_SUFFIX_KEY
                },
                new String[] {
                  username,
                  channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                  message,
                  Integer.toString(dimension),
                  rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                  rank.getSuffix().replaceAll("&", "\u00A7")
                });
      } else {
        if (channel.getFilter() != null) {
          for (String replacement : channel.getFilter()) {
            String[] split = replacement.split(" ");
            message = message.replaceAll(split[0], split[1]);
          }
        }
        if (channel.getFilter() != null) {
          for (String replacement : channel.getFilter()) {
            String[] split = replacement.split(" ");
            message = message.replaceAll(split[0], split[1]);
          }
        }
        format =
            StringUtils.replaceEach(
                ConfigHandler.chatFormat
                    .replaceAll(TEAM_KEY, "")
                    .replaceAll(" " + RANK_SUFFIX_KEY, ""),
                new String[] {
                  USERNAME_KEY, CHANNEL_KEY, MESSAGE_KEY, DIMENSION_KEY, RANK_PREFIX_KEY
                },
                new String[] {
                  username,
                  channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                  message,
                  Integer.toString(dimension),
                  rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET
                });
      }
    }
    return format;
  }

  public static void sendMessage(ICommandSender sender, String msg) {
    ITextComponent message = ForgeHooks.newChatWithLinks(msg.replaceAll("&", "\u00A7"), true);
    sender.sendMessage(message);
  }

  public static void notifyStaff(String msg) {
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      if (isStaff(player)) {
        sendMessage(player, msg);
      }
    }
    ServerEssentialsServer.LOGGER.info("[Notify] " + msg);
  }

  private static boolean isStaff(EntityPlayerMP player) {
    if (ConfigHandler.storageType.equalsIgnoreCase("rest")) {
      return CommandUtils.hasPerm(
          "staff.notify", UserManager.getPlayerRank(player.getGameProfile().getId()));
    }
    return false;
  }
}
