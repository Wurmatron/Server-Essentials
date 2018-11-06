package com.wurmcraft.serveressentials.common.chat.events;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.global.Channel.Type;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.api.json.user.team.file.Team;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.chat.commands.MuteCommand;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerChat {

  protected static Map<UUID, String[]> lastChat = new HashMap<>();

  public static boolean processRest(ServerChatEvent e) {
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
        ForgeHooks.newChatWithLinks(
            ChatHelper.format(
                username,
                UserManager.getRank(global.rank),
                currentChannel,
                e.getPlayer().dimension,
                UserManager.getTeam(global.getTeam()).length > 0
                    ? (ITeam) UserManager.getTeam(global.getTeam())[0]
                    : new Team(),
                e.getMessage()),
            true));
    return currentChannel.getName().equalsIgnoreCase(ConfigHandler.globalChannel);
  }

  private static boolean processFile(ServerChatEvent e) {
    return false;
  }

  private static List<EntityPlayerMP> getPlayersInChannel(Channel channel) {
    List<EntityPlayerMP> players = new ArrayList<>();
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      Channel ch = getUserChannel(player.getGameProfile().getId());
      if (channel.getName().equalsIgnoreCase(ch.getName())) {
        players.add(player);
      }
    }
    return players;
  }

  private static Channel getUserChannel(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(uuid)[1];
      return (Channel) DataHelper.get(Keys.CHANNEL, user.getCurrentChannel());
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return (Channel) DataHelper.get(Keys.CHANNEL, data.getCurrentChannel());
    }
    return (Channel) DataHelper.get(Keys.CHANNEL, ConfigHandler.defaultChannel);
  }

  private static boolean handleMessage(UUID name, String message) {
    if (lastChat.containsKey(name)) {
      String[] chat = lastChat.get(name);
      if (chat[0].equalsIgnoreCase(message)) {
        int count = 0;
        for (int index = 0; index < chat.length; index++) {
          if (message.equalsIgnoreCase(chat[index])) {
            count++;
          } else if (chat[index] == null) {
            chat[index] = message;
            count++;
            break;
          }
        }
        return count < ConfigHandler.spamLimit;
      } else {
        lastChat.remove(name);
        return true;
      }
    } else {
      String[] chat = new String[ConfigHandler.spamLimit];
      chat[0] = message;
      lastChat.put(name, chat);
    }
    return !isIgnored(name, message);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onChat(ServerChatEvent e) {
    if (MuteCommand.getMuted(e.getPlayer().getGameProfile().getId())) {
      e.setCanceled(true);
      e.getPlayer()
          .sendMessage(
              ForgeHooks.newChatWithLinks(
                  LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId()).MUTED));
      return;
    }
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      if (handleMessage(e.getPlayer().getGameProfile().getId(), e.getMessage())) {
        if (processRest(e)) {
          e.setCanceled(true);
          Channel ch = getUserChannel(e.getPlayer().getGameProfile().getId());
          for (EntityPlayerMP player : getPlayersInChannel(ch)) {
            if (ch.getType().name().equals(Type.PUBLIC.name())) {
              player.sendMessage(e.getComponent());
            } else if (canSeeChannelMessage(ch, e.getPlayer(), player)) {
              player.sendMessage(e.getComponent());
            }
          }
        }
      } else {
        e.getPlayer()
            .sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                        .CHAT_SPAM));
        e.setCanceled(true);
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      if (handleMessage(e.getPlayer().getGameProfile().getId(), e.getMessage())) {
        if (processFile(e)) {
          e.setCanceled(true);
        }
      } else {
        e.getPlayer()
            .sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                        .CHAT_SPAM));
        e.setCanceled(true);
      }
    }
  }

  public boolean canSeeChannelMessage(Channel channel, EntityPlayer sender, EntityPlayerMP player) {
    GlobalUser senderUser = (GlobalUser) UserManager.getPlayerData(sender)[0];
    GlobalUser user = (GlobalUser) UserManager.getPlayerData(player)[0];
    if (channel.getType() == Type.TEAM) {
      return senderUser.getTeam().equals(user.getTeam());
    }
    return false;
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void nameDisplay(PlayerEvent.NameFormat e) {
    EntityPlayer player = getPlayer(e.getUsername());
    if (player != null) {
      StringBuilder builder = new StringBuilder();
      Rank rank = UserManager.getPlayerRank(player.getGameProfile().getId());
      if (rank != null) {
        builder.append(rank.getPrefix().replaceAll("&", "\u00A7"));
      }
      builder.append(" ");
      builder.append(getName(player));
      if (rank != null && !rank.getSuffix().isEmpty()) {
        builder.append(rank.getSuffix().replaceAll("&", "\u00A7"));
      }
      e.setDisplayname(builder.toString());
    }
  }

  private String getName(EntityPlayer player) {
    String nick = UsernameResolver.getNick(player.getGameProfile().getId());
    if (nick.isEmpty()) {
      return UsernameResolver.getUsername(player.getGameProfile().getId());
    }
    return nick.replaceAll("&", "\u00A7");
  }

  private EntityPlayer getPlayer(String username) {
    for (EntityPlayer player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      if (player.getGameProfile().getName().equalsIgnoreCase(username)) {
        return player;
      }
    }
    return null;
  }

  public static boolean isIgnored(UUID user, String message) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser localUser = (LocalUser) UserManager.getPlayerData(user)[1];
      if (localUser != null
          && localUser.getIgnored() != null
          && localUser.getIgnored().length > 0) {
        for (String ignored : localUser.getIgnored()) {
          if (ignored.startsWith("[") && ignored.endsWith("]")) {
            if (message.toLowerCase().contains(ignored)) {
              return true;
            }
          } else if (message.substring(1).toLowerCase().startsWith(ignored)) {
            return true;
          }
        }
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData userData = (PlayerData) UserManager.getPlayerData(user)[0];
      return userData != null;
    }
    return false;
  }
}
