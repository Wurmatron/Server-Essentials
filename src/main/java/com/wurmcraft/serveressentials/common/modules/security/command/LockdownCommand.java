package com.wurmcraft.serveressentials.common.modules.security.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.security.SecurityModule;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "Security", trustedRequired = true)
public class LockdownCommand extends Command {

  @Override
  public String getName() {
    return "LockDown";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/lockdown";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_LOCKDOWN;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (SecurityModule.lockdown) {
      SecurityModule.lockdown = false;
      for (EntityPlayer player :
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.SECURITY_LOCKDOWN_DISABLED);
      }
    } else {
      SecurityModule.lockdown = true;
      for (EntityPlayer player :
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.SECURITY_LOCKDOWN_ENABLED);
      }
    }
  }
}
