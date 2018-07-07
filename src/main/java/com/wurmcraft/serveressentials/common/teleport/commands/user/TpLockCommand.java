package com.wurmcraft.serveressentials.common.teleport.commands.user;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "Teleportation")
public class TpLockCommand extends SECommand {

  @Override
  public String getName() {
    return "tpLock";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
    if (user.isTpLock()) {
      user.setTpLock(false);
      sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).TPLOCK_DISABLED));
    } else {
      user.setTpLock(true);
      sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).TPLOCK_ENABLED));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
