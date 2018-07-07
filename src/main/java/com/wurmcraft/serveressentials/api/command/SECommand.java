package com.wurmcraft.serveressentials.api.command;

import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public abstract class SECommand implements ICommand {

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "";
  }

  @Override
  public List<String> getAliases() {
    List<String> aliases = new ArrayList<>();
    aliases.add(getName().toLowerCase());
    aliases.add(getName().toUpperCase());
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (!canConsoleRun() && sender.getCommandSenderEntity() == null) {
      sender.sendMessage(
          new TextComponentString(
              LanguageModule.getLangFromKey(ConfigHandler.defaultLanguage).PLAYER_ONLY));
      return;
    }
    if (hasSubCommand() && args.length > 0) {
      Method[] methods = getClass().getMethods();
      for (Method method : methods) {
        if (method.getAnnotation(SubCommand.class) != null
            && method.getName().equalsIgnoreCase(args[0])) {
          try {
            method.invoke(this, sender, CommandUtils.getArgsAfterCommand(1, args));
            return;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    } else if (!hasSubCommand()) {
      // Run Non Sub Command
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
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

  public boolean canConsoleRun() {
    return false;
  }

  public boolean hasSubCommand() {
    return false;
  }

  protected static Local getCurrentLanguage(ICommandSender sender) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      return LanguageModule.getLangfromUUID(player.getGameProfile().getId());
    }
    return LanguageModule.getLangFromKey(ConfigHandler.defaultLanguage);
  }

  protected static GlobalUser forceUserFromUUID(UUID uuid) {
    GlobalUser user = null;
    if (UserManager.getPlayerData(uuid) != null && UserManager.getPlayerData(uuid).length > 0) {
      user = (GlobalUser) UserManager.getPlayerData(uuid)[0];
    }
    if (user == null) {
      user = RequestHelper.UserResponses.getPlayerData(uuid);
    }
    return user;
  }
}
