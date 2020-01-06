package com.wurmcraft.serveressentials.common.modules.discord.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.Token;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Discord")
public class VerifyCommand extends Command {

  @Override
  public String getName() {
    return "verify";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/verify <code>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_VERIFY;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      Token[] tokens = RequestGenerator.Discord.getDiscordCodes();
      boolean found = false;
      if (tokens != null && tokens.length > 0) {
        for (Token token : tokens) {
          if (token.token.equals(args[0])) {
            found = true;
            GlobalRestUser user =
                (GlobalRestUser)
                    UserManager.getUserData((EntityPlayer) sender.getCommandSenderEntity())[0];
            user.setDiscord(token.id);
            RequestGenerator.User.overridePlayer(user, Type.STANDARD);
            ChatHelper.sendMessage(sender, senderLang.local.DISCORD_SYNCED);
            UserManager.addReward(
                (EntityPlayer) sender.getCommandSenderEntity(), ConfigHandler.discordReward);
          }
        }
      }
      if (!found) {
        ChatHelper.sendMessage(sender, senderLang.local.DISCORD_INVALID_TOKEN);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }
}
