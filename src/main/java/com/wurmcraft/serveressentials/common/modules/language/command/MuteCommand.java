package com.wurmcraft.serveressentials.common.modules.language.command;

import static com.wurmcraft.serveressentials.common.utils.command.CommandUtils.predictUsernames;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Language")
public class MuteCommand extends Command {

  @Override
  public String getName() {
    return "mute";
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("m");
    aliases.add("Silent");
    return aliases;
  }

  @Override
  public String getUsage(Lang lang) {
    return "/mute <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_MUTE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (toggleMuteStatus(player)) {
        ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.LANG_MUTE);
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_MUTE_SENDER.replaceAll(
                Replacment.PLAYER, player.getDisplayNameString()));
        ChatHelper.notifyStaff(
            senderLang.local.LANG_MUTE_NOTIFY.replaceAll(
                Replacment.PLAYER, player.getDisplayNameString()));
      } else {
        ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.LANG_UNMUTE);
        ChatHelper.sendMessage(
            sender,
            senderLang.local.LANG_UNMUTE_SENDER.replaceAll(
                Replacment.PLAYER, player.getDisplayNameString()));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(LanguageModule.getUserLanguage(sender)));
    }
  }

  private static boolean toggleMuteStatus(EntityPlayer player) {
    boolean currentStatus = UserManager.isUserMuted(player.getGameProfile().getId());
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(player)[0];
      currentStatus = !currentStatus;
      user.setMuted(currentStatus);
      UserManager.setUserData(
          player.getGameProfile().getId(), new Object[] {user, UserManager.getUserData(player)[1]});
      return currentStatus;
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(player)[0];
      currentStatus = !currentStatus;
      user.setMuted(!currentStatus);
    }
    return currentStatus;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0 || args.length == 1) {
      return predictUsernames(args, 1);
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }
}
