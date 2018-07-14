package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Command(moduleName = "General")
public class VanishCommand extends SECommand {

  private static List<EntityPlayer> vanished = new ArrayList<>();

  @Override
  public String getName() {
    return "vanish";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("v");
    return alts;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      toggleVanish(
          (EntityPlayer) sender.getCommandSenderEntity(),
          !vanished.contains((EntityPlayer) sender.getCommandSenderEntity()));
    }
  }

  public void toggleVanish(EntityPlayer player, boolean vanish) {
    if (vanish) {
      vanished.add(player);
      SPacketEntityStatus packet = new SPacketEntityStatus(player, (byte) 3);
      for (EntityPlayer otherPlayer :
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
        if (otherPlayer != player) {
          ((EntityPlayerMP) otherPlayer).connection.sendPacket(packet);
        }
      }
    } else {
      // TODO Unvanish
    }
    vanished.remove(player);
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
