package com.wurmcraft.serveressentials.common.utils.command;

import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.LOGGER;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.security.SecurityModule;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class SECommand extends CommandBase {

  private String module;
  private Command command;
  // Calculated Values
  boolean hasSubCommands;
  Map<String, Method> subCommands;

  public SECommand(String module, Command command) {
    this.module = module;
    this.command = command;
    hasSubCommands = CommandUtils.hasSubCommand(command);
    subCommands = CommandUtils.getSubCommands(command);
  }

  @Override
  public String getName() {
    return command.getName();
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return command.getUsage(LanguageModule.getDefaultLang());
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (hasSubCommands && args.length > 0 && subCommands.containsKey(args[0])) {
      try {
        subCommands
            .get(args[0])
            .invoke(
                command,
                server,
                sender,
                Arrays.copyOfRange(args, 1, args.length),
                LanguageModule.getUserLanguage(sender));
      } catch (IllegalAccessException | InvocationTargetException e) {
        LOGGER.error(e.getLocalizedMessage());
      }
    } else {
      command.execute(server, sender, args, LanguageModule.getUserLanguage(sender));
    }
  }

  @Override
  public List<String> getAliases() {
    return CommandUtils.getCommandAliases(command);
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    if (command.canConsoleRun() && sender.getCommandSenderEntity() == null
        || sender.getCommandSenderEntity() instanceof EntityPlayer
            && SecurityModule.isTrusted((EntityPlayer) sender.getCommandSenderEntity())) {
      return true;
    }
    if (SecurityModule.trustedUsers != null
        && !SecurityModule.trustedUsers.isEmpty()
        && command.getClass().getAnnotation(ModuleCommand.class).trustedRequired()
        && sender instanceof EntityPlayer) {
      return SecurityModule.isTrusted((EntityPlayer) sender.getCommandSenderEntity());
    }
    if (CommandUtils.hasPerm("*", ((EntityPlayer) sender).getGameProfile().getId())) {
      return true;
    }
    return CommandUtils.hasPerm(
        getCommandPerm(command), ((EntityPlayer) sender).getGameProfile().getId());
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return command.getAutoCompletion(server, sender, args, targetPos);
  }

  // TODO Sub Command Perms
  private String getCommandPerm(Command command) {
    if (command.getClass().getAnnotation(ModuleCommand.class).perm().isEmpty()) {
      return command.getClass().getAnnotation(ModuleCommand.class).moduleName()
          + "."
          + command.getName().toLowerCase();
    } else {
      return command.getClass().getAnnotation(ModuleCommand.class).perm();
    }
  }
}
