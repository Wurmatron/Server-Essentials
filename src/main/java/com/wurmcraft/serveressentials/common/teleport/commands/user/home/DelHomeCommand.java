package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "Teleportation")
public class DelHomeCommand extends SECommand {

  @Override
  public String getName() {
    return "delHome";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1) {
      String homeName = args[0];
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      user.delHome(homeName);
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).HOME_DELETED.replaceAll("%HOME%", homeName)));
      DataHelper.forceSave(Keys.LOCAL_USER, user);
      UserManager.playerData.put(
          player.getGameProfile().getId(),
          new Object[] {forceUserFromUUID(player.getGameProfile().getId()), user});
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
