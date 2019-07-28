package com.wurmcraft.serveressentials.common.modules.matterbridge.event;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.matterbridge.MatterBridgeModule;
import com.wurmcraft.serveressentials.common.modules.matterbridge.api.json.MBMessage;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.concurrent.TimeUnit;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerTickEvent {

  @SubscribeEvent
  public void onServerTick(TickEvent.ServerTickEvent e) {
    if (e.phase == TickEvent.Phase.END) {
      ServerEssentialsServer.instance.executors.schedule(
          () -> {
            MBMessage[] newMessages = MatterBridgeModule.getMessages();
            if (newMessages != null && newMessages.length > 0) {
              for (MBMessage msg : newMessages) {
                if (msg.id != null && !msg.id.isEmpty()) {
                  String[] formattingData = getFormattingData(msg.username);
                  if (formattingData[1] != null && formattingData[1].length() > 0) {
                    formattingData[1] = formattingData[1].replaceAll(" ", "");
                    Rank rank = ServerEssentialsAPI.rankManager.getRank(formattingData[1]);
                    if (rank != null) {
                      formattingData[1] = rank.getPrefix();
                    }
                    if (rank.getSuffix() != null && !rank.getSuffix().isEmpty()) {
                      formattingData[2] = formattingData[2] + rank.getSuffix();
                    }
                  }
                  ChatHelper.reformatAndSendMessage(
                      DataHelper.get(Storage.CHANNEL, ConfigHandler.globalChannel, new Channel()),
                      formattingData[0] + " " + formattingData[1] + " " + formattingData[2],
                      msg.text);
                } else {
                  ChatHelper.reformatAndSendMessage(
                      DataHelper.get(Storage.CHANNEL, ConfigHandler.globalChannel, new Channel()),
                      msg.username,
                      msg.text);
                }
              }
            }
          },
          0,
          TimeUnit.MILLISECONDS);
    }
  }

  private static String[] getFormattingData(String name) {
    return new String[] {
      name.substring(0, name.indexOf("]") + 1),
      name.substring(name.indexOf("]") + 3, name.lastIndexOf("]")),
      name.substring(name.lastIndexOf("]") + 1)
    };
  }

  @SubscribeEvent(receiveCanceled = false)
  public void onChat(ServerChatEvent e) {
    MBMessage msg =
        new MBMessage(
            "",
            e.getMessage(),
            "["
                + UserManager.getUserRank(e.getPlayer()).getName()
                + "] "
                + (UserManager.getNickname(e.getPlayer()).isEmpty()
                    ? e.getUsername()
                    : UserManager.getNickname(e.getPlayer())),
            UserManager.getUserRank(e.getPlayer()).getID(),
            "",
            e.getPlayer().getGameProfile().getId().toString());
    MatterBridgeModule.sendMessage(msg);
  }
}
