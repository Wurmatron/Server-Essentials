package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Teleportation")
public class SetHome extends SECommand {

  private static int getMaxHomes(GlobalUser user) {
    for (String perk : user.getPerks()) {
      if (perk.startsWith("home.amount.")) {
        return Integer.parseInt(perk.substring(perk.lastIndexOf(".") + 1, perk.length()));
      }
    }
    return 1;
  }

  @Override
  public String getName() {
    return "setHome";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    String homeName = args.length > 0 ? args[0] : ConfigHandler.defaultHome;
    if (homeName.isEmpty()) homeName = ConfigHandler.defaultHome;
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (!homeName.equalsIgnoreCase("list") && setHome(player, homeName)) {
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .HOME_CREATED
              .replaceAll("%HOME%", homeName)
              .replaceAll("&", "\u00A7"));
    } else {
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .HOME_FAILED
              .replaceAll("%HOME%", homeName)
              .replaceAll("&", "\u00A7"));
    }
  }

  public boolean setHome(EntityPlayer player, String name) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player)[0];
      if (data.getMaxHomes() > data.getHomes().length) {
        data.addHome(new Home(name, new LocationWrapper(player.getPosition(), player.dimension)));
        return true;
      } else {
        return false;
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser data = (LocalUser) UserManager.getPlayerData(player)[1];
      GlobalUser global = (GlobalUser) UserManager.getPlayerData(player)[0];
      if (getMaxHomes(global) > data.getHomes().length) {
        data.addHome(new Home(name, new LocationWrapper(player.getPosition(), player.dimension)));
        DataHelper.forceSave(Keys.LOCAL_USER, data);
        UserManager.playerData.put(player.getGameProfile().getId(), new Object[] {global, data});
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/setHome \u00A7b<name>";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("setH");
    alts.add("sHome");
    return alts;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_SETHOME.replaceAll("&", "\u00A7");
  }
}
