package com.wurmcraft.serveressentials.common.chat.events;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.team.Team;

public class PlayerChat {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onChat(ServerChatEvent e) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      if (proccessRest(e)) {
        e.setCanceled(true);
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      if (proccessFile(e)) {
        e.setCanceled(true);
      }
    }
  }

  public static boolean proccessRest(ServerChatEvent e) {
    GlobalUser global =
        (GlobalUser) UserManager.getPlayerData(e.getPlayer().getGameProfile().getId())[0];
    LocalUser local =
        (LocalUser) UserManager.getPlayerData(e.getPlayer().getGameProfile().getId())[1];
    Channel currentChannel = (Channel) DataHelper.get(Keys.CHANNEL, local.getCurrentChannel());
    if (DataHelper.get(Keys.CHANNEL, local.getCurrentChannel()) == null) {
      local.setCurrentChannel(ConfigHandler.defaultChannel);
      DataHelper.forceSave(Keys.LOCAL_USER, local);
      currentChannel = (Channel) DataHelper.get(Keys.CHANNEL, local.getCurrentChannel());
    }
    String username =
        global.getNick().isEmpty()
            ? e.getUsername()
            : TextFormatting.GREEN + "*" + TextFormatting.GRAY + global.getNick();
    e.setComponent(
        new TextComponentString(
            ChatHelper.format(
                username,
                UserManager.getRank(global.rank),
                currentChannel,
                e.getPlayer().dimension,
                UserManager.getTeam(global.getTeam()).length > 0
                    ? (ITeam) UserManager.getTeam(global.getTeam())[0]
                    : new Team(),
                e.getMessage())));
    if (currentChannel.getName().equalsIgnoreCase(ConfigHandler.defaultChannel)) {
      return false;
    }
    return true;
  }

  public static boolean proccessFile(ServerChatEvent e) {
    return false;
  }
}
