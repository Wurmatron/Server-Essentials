package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General", name = "Sudo", aliases = {"forceUser", "Fu"})
public class SudoCommand {

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.STRING_ARR}, inputNames = {"Player", "Command Args"})
  public void sudoCommand(ICommandSender sender, EntityPlayer player, String[] commands) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.sudo") || !SERegistry
        .isModuleLoaded("Rank")) {
      String command = Strings.join(commands, " ");
      FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()
          .executeCommand(player, command);
      sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SUDO_OTHER.
                  replaceAll("%PLAYER%",
                      COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR).
                  replaceAll("%COMMAND%", COMMAND_INFO_COLOR + command + COMMAND_COLOR)),
          player));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
