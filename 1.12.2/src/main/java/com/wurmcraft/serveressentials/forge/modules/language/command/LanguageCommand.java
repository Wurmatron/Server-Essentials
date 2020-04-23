package com.wurmcraft.serveressentials.forge.modules.language.command;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import net.minecraft.command.ICommandSender;

@ModuleCommand(moduleName = "Language", name = "Lang")
public class LanguageCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Language Key"})
  public void changeLanguage(ICommandSender sender, String lang) {

  }
}
