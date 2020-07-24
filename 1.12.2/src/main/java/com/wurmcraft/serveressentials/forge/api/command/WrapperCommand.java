package com.wurmcraft.serveressentials.forge.api.command;

import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class WrapperCommand extends CommandBase {

  private ICommand command;

  public WrapperCommand(ICommand command) {
    this.command = command;
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
    command.execute(server, sender, args);
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
