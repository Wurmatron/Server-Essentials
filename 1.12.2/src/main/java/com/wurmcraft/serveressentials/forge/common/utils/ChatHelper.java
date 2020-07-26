package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.chat.DefaultFontInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ChatHelper {

  public static void sendMessage(ICommandSender sender, String msg) {
    sendMessage(sender, new TextComponentString(msg.replaceAll("&", "\u00a7")));
  }

  public static void sendHoverMessage(ICommandSender sender, String msg,
      String hoverMsg) {
      ITextComponent msgComponent = new TextComponentString(
          msg.replaceAll("&", "\u00a7"));
      msgComponent.getStyle().setHoverEvent(new HoverEvent(Action.SHOW_TEXT,
          new TextComponentString(hoverMsg.replaceAll("&", "\u00a7"))));
      sendMessage(sender, msgComponent);
  }

  public static void sendClickMessage(ICommandSender sender, String msg, String command) {
    ITextComponent msgComponent = new TextComponentString(msg.replaceAll("&", "\u00a7"));
    msgComponent.getStyle()
        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    sendMessage(sender, msgComponent);
  }

  public static void sendMessage(ICommandSender sender, ITextComponent msg) {
    sender.sendMessage(msg);
  }

  public static void sendSpacerWithMessage(ICommandSender sender, String spacer,
      String msg) {
    int msgLength = calculateLength(msg);
    int leftPerSide =
        ((DefaultFontInfo.getChatLength() - msgLength) / 2) - DefaultFontInfo.SPACE
            .getLength();
    String chatMSG = spacer.substring(0, leftPerSide);
    if (chatMSG.endsWith("&")) {
      chatMSG = chatMSG.substring(0, chatMSG.length() - 1);
    }
    chatMSG = chatMSG + " " + msg + " " + spacer.substring(0, leftPerSide);
    sendMessage(sender, chatMSG);
  }

  public static void sendMessageToAll(ITextComponent msg) {
    for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getPlayerList().getPlayers()) {
      sendMessage(player, msg);
    }
    ServerEssentialsServer.logger.info("[Chat]: " + msg.getUnformattedText());
  }

  public static void sendMessageToAll(String msg) {
    sendMessageToAll(msg.replaceAll("&", "\u00a7"));
  }

  private static int calculateLength(String msg) {
    int total = 0;
    for (int index = 0; index < msg.toCharArray().length; index++) {
      char currentChar = msg.toCharArray()[index];
      if (currentChar == '&') {
        index += 2;
      } else {
        total += DefaultFontInfo.getDefaultFontInfo(currentChar).getLength();
      }
    }
    return total;
  }
}