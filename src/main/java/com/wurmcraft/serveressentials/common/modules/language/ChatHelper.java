package com.wurmcraft.serveressentials.common.modules.language;

import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;

public class ChatHelper {

  public static String format(
      String username, Rank rank, Channel channel, int dimension, String message) {
    String format = "";
    if (!isFormatted(message)) {
      if (rank == null) {
        rank =
            new Rank("", "&5[Error]", "", new String[] {ConfigHandler.defaultRank}, new String[0]);
      }
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
                },
                new String[] {
                  username.replaceAll("&", "\u00A7"),
                  channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                  message.replaceAll("&", "\u00A7"),
                  Integer.toString(dimension),
                  rank.getPrefix().replaceAll("&", "\u00A7"),
                  rank.getSuffix().replaceAll("&", "\u00A7")
                });
        return format.replaceAll(" {2}", " ");
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
                },
                new String[] {
                  username.replaceAll("&", "\u00A7"),
                  channel.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                  message,
                  Integer.toString(dimension),
                  rank.getPrefix().replaceAll("&", "\u00A7") + TextFormatting.RESET,
                });
        return format.replaceAll("\\s\\s+/g", " ");
      }
    } else {

    }
    return format.replaceAll("\\s\\s+/g", " ");
  }

  private static String applyFilter(Channel channel, String message) {
    if (channel != null && channel.getFilter() != null) {
      for (String replacement : channel.getFilter()) {
        String[] split = replacement.split(" ");
        message = message.replaceAll(split[0], split[1]);
      }
    }
    return message.replaceAll("\\s\\s+/g", " ");
  }

  public static void sendMessage(ICommandSender sender, String msg) {
    ITextComponent message = ForgeHooks.newChatWithLinks(msg.replaceAll("&", "\u00A7"), true);
    sender.sendMessage(message);
  }

  public static void sendMessageToAll(String msg) {
    ITextComponent message = ForgeHooks.newChatWithLinks(msg.replaceAll("&", "\u00A7"), true);
    for (EntityPlayer player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      player.sendMessage(message);
    }
    ServerEssentialsServer.LOGGER.info(message.getFormattedText());
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

  public static void notifySpy(String msg) {
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      if (isSpy(player)) {
        sendMessage(player, "&4[&cSpy&c]:&5 " + msg);
      }
    }
  }

  private static boolean isStaff(EntityPlayerMP player) {
    return CommandUtils.hasPerm("staff.notify", player);
  }

  private static boolean isSpy(EntityPlayerMP player) {
    return CommandUtils.hasPerm("staff.spy", player);
  }

  public static boolean isFormatted(String message) {
    return false;
  }

  public static String reformatMessage(Channel ch, String message) {
    return message;
  }
}
