package com.wurmcraft.serveressentials.common.utils;

import static com.wurmcraft.serveressentials.common.security.SecurityModule.*;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.rest.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class SECommand implements ICommand, ModuleCommand {

  protected static final TextFormatting DEFAULT_COLOR = TextFormatting.LIGHT_PURPLE;
  protected static final TextFormatting DEFAULT_USAGE_COLOR = TextFormatting.GOLD;
  protected static final String FORMATTING_CODE = "\u00A7";

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

  protected static GlobalTeam forceTeamFromName(String name) {
    GlobalTeam team = null;
    if (UserManager.TEAM_CACHE.get(name) != null && UserManager.TEAM_CACHE.get(name).length > 0) {
      team = (GlobalTeam) UserManager.getTeam(name)[0];
    }
    if (team == null) {
      team = RequestHelper.TeamResponses.getTeam(name);
    }
    return team;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "";
  }

  public List<String> getAltNames() {
    return new ArrayList<>();
  }

  @Override
  public List<String> getAliases() {
    List<String> aliases = new ArrayList<>();
    aliases.add(getName().toLowerCase());
    aliases.add(getName().toUpperCase());
    for (String alt : getAltNames()) {
      aliases.add(alt);
      aliases.add(alt.toLowerCase());
      aliases.add(alt.toUpperCase());
    }
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
            ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
          }
        }
      }
    } else if (!hasSubCommand()) {
      // Run Non Sub Command
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    if (canConsoleRun() && sender.getCommandSenderEntity() == null) {
      return true;
    }
    if (trusted.isEmpty()
        && getClass().getAnnotation(Command.class).trustedRequired()
        && sender instanceof EntityPlayer) {
      return isTrustedMember((EntityPlayer) sender.getCommandSenderEntity());
    }
    return CommandUtils.hasPerm(getCommandPerm(), sender);
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return new ArrayList<>();
  }

  public String getCommandPerm() {
    if (!getClass().getAnnotation(Command.class).perm().isEmpty()) {
      return getClass().getAnnotation(Command.class).perm();
    }
    return getClass().getAnnotation(Command.class).moduleName() + "." + getName().toLowerCase();
  }

  public boolean canConsoleRun() {
    return false;
  }

  public boolean hasSubCommand() {
    return false;
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return false;
  }

  @Override
  public int compareTo(ICommand o) {
    return 0;
  }

  public String getDescription(ICommandSender sender) {
    return "";
  }

  protected static List<String> autoCompleteUsername(String[] args, int index) {
    List<String> possibleUsernames =
        Arrays.asList(
            FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames());
    if (args.length > index && args[index] != null) {
      return predictName(args[index], possibleUsernames);
    } else {
      return possibleUsernames;
    }
  }

  protected static List<String> predictName(String current, List<String> possibleNames) {
    List<String> predictedNames = new ArrayList<>();
    for (String name : possibleNames) {
      if (name.toLowerCase().startsWith(current.toLowerCase())
          || name.toLowerCase().endsWith(current.toLowerCase())) {
        predictedNames.add(name);
      }
    }
    return predictedNames;
  }
}