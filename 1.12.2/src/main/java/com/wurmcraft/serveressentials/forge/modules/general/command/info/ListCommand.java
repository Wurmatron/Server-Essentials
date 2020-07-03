package com.wurmcraft.serveressentials.forge.modules.general.command.info;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.track.TrackingStatus;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General", name = "List", aliases = "Players")
public class ListCommand {

  @Command(inputArguments = {})
  public void listPlayers(ICommandSender sender) {
    if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      TrackingStatus[] status = RestRequestGenerator.Track.getStatus();
      for (TrackingStatus s : status) {
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + s.serverID + (" v" + s.modpackVersion)));
        for (String player : s.players) {
          if (player.contains("(") && player.contains(")")) {
            sender.sendMessage(
                new TextComponentString(
                    "    " + COMMAND_INFO_COLOR + player.substring(0,player.indexOf("(")-1)));
          } else {
            sender.sendMessage(
                new TextComponentString("    " + COMMAND_INFO_COLOR + player));
          }
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + SERegistry.globalConfig.serverID + (" v"
              + SERegistry.globalConfig.modpackVersion)));
      for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance()
          .getPlayerList().getPlayers()) {
        sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
            "    " + COMMAND_INFO_COLOR + player.getDisplayNameString()), player));
      }
    }
  }

}
