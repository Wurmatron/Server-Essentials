package com.wurmcraft.serveressentials.common.modules.language.event;

import static com.wurmcraft.serveressentials.common.modules.language.LanguageModule.getPlayersInChannel;
import static com.wurmcraft.serveressentials.common.utils.user.UserManager.getUserChannel;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.storage.json.Channel;
import com.wurmcraft.serveressentials.api.storage.json.Channel.Type;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.team.SETeam;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEvents {

  protected static Map<UUID, String[]> lastChat = new HashMap<>();

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onChat(ServerChatEvent e) {
    if (UserManager.isUserMuted(e.getPlayer().getGameProfile().getId())) {
      ChatHelper.sendMessage(
          e.getPlayer(), LanguageModule.getUserLanguage(e.getPlayer()).local.MUTED);
      e.setCanceled(true);
      return;
    }
    if (canHandleMessage(e.getPlayer().getGameProfile().getId(), e.getMessage())) {
      if (processMessage(e)) {
        e.setCanceled(true);
        Channel ch = getUserChannel(e.getPlayer().getGameProfile().getId());
        getPlayersInChannel(ch)
            .stream()
            .filter(
                player ->
                    ch.getType().name().equals(Type.PUBLIC.name())
                        || canSeeChannelMessage(ch, e.getPlayer(), player))
            .forEach(player -> player.sendMessage(e.getComponent()));
        if (!ConfigHandler.chatNotification.isEmpty()
            && e.getMessage().contains(ConfigHandler.chatNotification)) {
          String name =
              e.getMessage().substring(e.getMessage().indexOf(ConfigHandler.chatNotification));
          name = name.substring(1, name.indexOf(" "));
          EntityPlayerMP player = (EntityPlayerMP) CommandUtils.getPlayerForName(name);
          if (player != null) {
            player.connection.sendPacket(
                new SPacketCustomSound(
                    "block.anvil.land",
                    SoundCategory.HOSTILE,
                    player.getPosition().getX(),
                    player.getPosition().getY(),
                    player.getPosition().getZ(),
                    1000f,
                    1f));
          }
        }
      }
    } else {
      ChatHelper.sendMessage(
          e.getPlayer(), LanguageModule.getUserLanguage(e.getPlayer()).local.SPAM);
      e.setCanceled(true);
    }
  }

  private static boolean canHandleMessage(UUID name, String message) {
    if (lastChat.containsKey(name)) { // Check for message history
      String[] chat = lastChat.get(name);
      if (chat[0].equalsIgnoreCase(message)) { // same as last time
        int count = 0;
        for (int index = 0; index < chat.length; index++) {
          if (message.equalsIgnoreCase(chat[index])) { // count the same messages
            count++;
          } else if (chat[index] == null) { // add msg to list and  break, continue
            chat[index] = message;
            count++;
            break;
          }
        }
        return count <= ConfigHandler.spamLimit;
      } else { // different msg, so we reset
        lastChat.remove(name);
        return true;
      }
    } else {
      String[] chat = new String[ConfigHandler.spamLimit + 1];
      chat[0] = message;
      lastChat.put(name, chat);
      return !UserManager.isIgnored(name, message);
    }
  }

  private static void setUserChannel(EntityPlayerMP player, Channel channel) {
    if (channel == null) {
      channel = (Channel) DataHelper.get(Storage.CHANNEL, ConfigHandler.defaultChannel);
    }
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser user = (LocalRestUser) UserManager.getUserData(player)[1];
      user.setCurrentChannel(channel.getID());
      DataHelper.save(Storage.LOCAL_USER, user);
      UserManager.setUserData(
          player.getGameProfile().getId(),
          new Object[] {UserManager.getUserData(player.getGameProfile().getId()), user});
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(player)[0];
      user.setCurrentChannel(channel);
      DataHelper.save(Storage.USER, user);
      UserManager.setUserData(player.getGameProfile().getId(), new Object[] {user});
    }
  }

  private static boolean processMessage(ServerChatEvent e) {
    Channel ch = UserManager.getUserChannel(e.getPlayer());
    String username =
        UserManager.getNickname(e.getPlayer()).isEmpty()
            ? e.getPlayer().getName()
            : TextFormatting.GREEN
                + "*"
                + TextFormatting.GRAY
                + UserManager.getNickname(e.getPlayer());
    if (ch == null) {
      setUserChannel(
          e.getPlayer(), (Channel) DataHelper.get(Storage.CHANNEL, ConfigHandler.defaultChannel));
    }
    e.setComponent(
        ForgeHooks.newChatWithLinks(
            ChatHelper.format(
                username,
                UserManager.getUserRank(e.getPlayer()),
                ch,
                e.getPlayer().dimension,
                new SETeam(),
                e.getMessage()),
            true));
    return !ch.getName().equalsIgnoreCase(ConfigHandler.globalChannel);
  }

  public boolean canSeeChannelMessage(Channel channel, EntityPlayer sender, EntityPlayerMP player) {
    if (channel.getType() == Type.TEAM) {
      return UserManager.getUserTeam(sender.getGameProfile().getId())
          .equals(UserManager.getUserTeam(player.getGameProfile().getId()));
    }
    return false;
  }

  //  // TODO Find a better way of doing this
  //  @SubscribeEvent(priority = EventPriority.HIGHEST)
  //  public void nameDisplay(PlayerEvent.NameFormat e) {
  //    if (e.getEntityPlayer() != null) {
  //      StringBuilder builder = new StringBuilder();
  //      Rank rank = UserManager.getUserRank(e.getEntityPlayer());
  //      if (rank != null) {
  //        builder.append(rank.getPrefix().replaceAll("&", "\u00A7"));
  //      }
  //      builder.append(" ");
  //      builder.append(getName(e.getEntityPlayer()));
  //      if (rank != null && !rank.getSuffix().isEmpty()) {
  //        builder.append(rank.getSuffix().replaceAll("&", "\u00A7"));
  //      }
  //      e.setDisplayname(builder.toString());
  //    }
  //  }

  private String getName(EntityPlayer player) {
    String nick = UserManager.getNickname(player);
    if (nick.isEmpty()) {
      return player.getName();
    }
    return nick.replaceAll("&", "\u00A7");
  }
}
