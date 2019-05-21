package com.wurmcraft.serveressentials.common.modules.language;

import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.team.Team;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;

public class ChatHelper {

  public static String format(
      String username, Rank rank, Channel channel, int dimension, Team team, String message) {
    if (!isFormatted(message)) {
      String format = "";
      if (team != null && team.getName() != null && team.getName().length() > 0) {
        if (rank.getSuffix() != null && !rank.getSuffix().equals("")) {
          message = applyFilter(channel, message);
          format =
              StringUtils.replaceEach(
                  ConfigHandler.chatFormat,
                  new String[] {
                    Replacment.USERNAME,
                    Replacment.CHANNEL,
                    Replacment.MESSAGE,
                    Replacment.DIMENSION,
                    Replacment.RANK_PREFIX,
                    Replacment.RANK_SUFFIX,
                    Replacment.TEAM
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
          return format;
        } else {
          message = applyFilter(channel, message);
          format =
              StringUtils.replaceEach(
                  ConfigHandler.chatFormat.replaceAll(" " + Replacment.RANK_SUFFIX, ""),
                  new String[] {
                    Replacment.USERNAME,
                    Replacment.CHANNEL,
                    Replacment.MESSAGE,
                    Replacment.DIMENSION,
                    Replacment.RANK_PREFIX,
                    Replacment.TEAM
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
                            + team.getName().substring(1).toLowerCase()
                            + TextFormatting.RESET
                        : ""
                  });
          return format;
        }
      } else {
        if (rank.getSuffix() != null && !rank.getSuffix().equals("")) {
          message = applyFilter(channel, message);
          format =
              StringUtils.replaceEach(
                  ConfigHandler.chatFormat.replaceAll(Replacment.TEAM, ""),
                  new String[] {
                    Replacment.USERNAME,
                    Replacment.CHANNEL,
                    Replacment.MESSAGE,
                    Replacment.DIMENSION,
                    Replacment.RANK_PREFIX,
                    Replacment.RANK_SUFFIX
                  },
                  new String[] {
                    username,
                    channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                    message,
                    Integer.toString(dimension),
                    rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                    rank.getSuffix().replaceAll("&", "\u00A7")
                  });
          return format;
        } else {
          message = applyFilter(channel, message);
          format =
              StringUtils.replaceEach(
                  ConfigHandler.chatFormat
                      .replaceAll(Replacment.TEAM, "")
                      .replaceAll(" " + Replacment.RANK_SUFFIX, ""),
                  new String[] {
                    Replacment.USERNAME,
                    Replacment.CHANNEL,
                    Replacment.MESSAGE,
                    Replacment.DIMENSION,
                    Replacment.RANK_PREFIX
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
    } else {
      return message;
    }
  }

  private static String applyFilter(Channel channel, String message) {
    if (channel != null && channel.getFilter() != null) {
      for (String replacement : channel.getFilter()) {
        String[] split = replacement.split(" ");
        message = message.replaceAll(split[0], split[1]);
      }
    }
    return message;
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
    return CommandUtils.hasPerm("staff.notify", player);
  }

  public static boolean isFormatted(String message) {
    return false;
  }

  public static String reformatMessage(Channel ch, String message) {
    return message;
  }
}
