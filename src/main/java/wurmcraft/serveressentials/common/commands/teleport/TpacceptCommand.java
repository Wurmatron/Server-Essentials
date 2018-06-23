package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.LocationWrapper;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.TeleportUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class TpacceptCommand extends SECommand {

  public TpacceptCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "tpaccept";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/tpaccept";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender;
    if (TpaCommand.activeRequests.size() > 0) {
      for (long time : TpaCommand.activeRequests.keySet()) {
        EntityPlayer[] otherPlayer = TpaCommand.activeRequests.get(time);
        if (otherPlayer[1].getGameProfile().getId().equals(player.getGameProfile().getId())) {
          PlayerData data = UsernameResolver.getPlayerData(otherPlayer[0]);
          data.setLastLocation(
              new LocationWrapper((int) otherPlayer[0].posX, (int) otherPlayer[0].posY,
                  (int) otherPlayer[0].posZ, otherPlayer[0].dimension));
          TeleportUtils.teleportTo(otherPlayer[0], player.getPosition(), true);
          ChatHelper.sendMessageTo(otherPlayer[1], Local.TPA_ACCEPED_OTHER
              .replaceAll("#", otherPlayer[0].getDisplayName().getUnformattedText()));
          ChatHelper.sendMessageTo(otherPlayer[0], Local.TPA_ACCEPTED
              .replaceAll("#", otherPlayer[1].getDisplayName().getUnformattedText()));
          TpaCommand.activeRequests.remove(time);
          return;
        }
      }
    } else {
      ChatHelper.sendMessageTo(sender, Local.NO_TPA);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Accept a teleport request";
  }
}
