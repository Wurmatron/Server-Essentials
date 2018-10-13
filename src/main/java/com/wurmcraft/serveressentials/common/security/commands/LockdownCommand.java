package com.wurmcraft.serveressentials.common.security.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.security.SecurityModule;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Security", trustedRequired = true)
public class LockdownCommand extends SECommand {

  @Override
  public String getName() {
    return "lockdown";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    ChatHelper.sendMessage(
        sender,
        SecurityModule.lockdown
            ? getCurrentLanguage(sender).LOCKDOWN_DISABLED
            : getCurrentLanguage(sender).LOCKDOWN_ENABLED);
    SecurityModule.lockdown = !SecurityModule.lockdown;
  }
}
