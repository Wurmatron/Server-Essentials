package com.wurmcraft.serveressentials.forge.api.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.module.config.CommandCost;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class WrapperCommand extends CommandBase {

  private ICommand command;
  private CommandCost cost;

  public WrapperCommand(ICommand command) {
    this.command = command;
    if (SERegistry.isModuleLoaded("Economy")) {
      EconomyConfig config = (EconomyConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Economy");
      if (config.commandCost != null && config.commandCost.length > 0) {
        for (CommandCost cmd : config.commandCost) {
          if (cmd.commandName.equalsIgnoreCase(getName())) {
            this.cost = cmd;
          }
        }
      }
    }
  }

  @Override
  public String getName() {
    return command.getName();
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return command.getUsage(sender);
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (EcoUtils.handleCommandCost(sender, cost)) {
      command.execute(server, sender, args);
      if (SERegistry.globalConfig.logCommandToCMD) {
        ServerEssentialsServer.logger.info(
            sender.getDisplayName().getUnformattedText() + " has run command `/"
                + getName() + " " + String.join(" ", args) + "'");
      }
    } else {
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_INSUFFICENT_FUNDS
              .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + cost.cost + COMMAND_COLOR)));
    }
  }

  @Override
  public int getRequiredPermissionLevel() {
    return super.getRequiredPermissionLevel();
  }

  @Override
  public List<String> getAliases() {
    return command.getAliases();
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank")) {
      return RankUtils.hasPermission(RankUtils.getRank(sender),
          "command" + "." + command.getName());
    } else {
      return true;
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos targetPos) {
    return command.getTabCompletions(server, sender, args, targetPos);
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return command.isUsernameIndex(args, index);
  }
}
