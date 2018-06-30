package com.wurmcraft.serveressentials.api.command;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public abstract class SECommand implements ICommand {

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return null;
  }

  @Override
  public List<String> getAliases() {
    return null;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return false;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return null;
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return false;
  }

  @Override
  public int compareTo(ICommand o) {
    return 0;
  }

  public String getPermission() {
    return "";
  }
}
