package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (!homeName.equalsIgnoreCase("list") && setHome(player, homeName)) {
      sender.sendMessage(new TextComponentString("T"));
    } else {
      sender.sendMessage(new TextComponentString("F"));
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
        UserManager.playerData.put(player.getGameProfile().getId(), new Object[]{global, data});
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
}
