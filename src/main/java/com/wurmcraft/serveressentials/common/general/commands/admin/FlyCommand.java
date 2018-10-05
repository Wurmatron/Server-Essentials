package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Command(moduleName = "General")
public class FlyCommand extends SECommand {

  @Override
  public String getName() {
    return "fly";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length > 0) {
      EntityPlayer user = UsernameResolver.getPlayer(args[0]);
      if (user != null) {
        if (!user.capabilities.allowFlying) {
          user.capabilities.allowFlying = true;
          user.capabilities.isFlying = true;
          ChatHelper.sendMessage(
              user, LanguageModule.getLangfromUUID(user.getGameProfile().getId()).FLY_ENABLED);
          ChatHelper.sendMessage(
              player,
              LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                  .FLY_ENABLED_OTHER
                  .replaceAll("%PLAYER%", user.getDisplayNameString()));
          user.sendPlayerAbilities();
        } else {
          user.capabilities.allowFlying = false;
          ChatHelper.sendMessage(
              user, LanguageModule.getLangfromUUID(user.getGameProfile().getId()).FLY_DISABLED);
          ChatHelper.sendMessage(
              player,
              LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                  .FLY_DISABLED_OTHER
                  .replaceAll("%PLAYER%", user.getDisplayNameString()));
          user.sendPlayerAbilities();
        }
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    } else {
      if (!player.capabilities.isCreativeMode) {
        if (!player.capabilities.allowFlying) {
          player.capabilities.allowFlying = true;
          ChatHelper.sendMessage(player, getCurrentLanguage(sender).FLY_ENABLED);
          player.sendPlayerAbilities();
        } else {
          player.capabilities.allowFlying = false;
          ChatHelper.sendMessage(player, getCurrentLanguage(sender).FLY_DISABLED);
          player.sendPlayerAbilities();
        }
      } else {
        ChatHelper.sendMessage(player, getCurrentLanguage(sender).FLY_ENABLED);
      }
    }
  }
}
