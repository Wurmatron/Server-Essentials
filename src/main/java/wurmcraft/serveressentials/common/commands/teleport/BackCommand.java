package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.LocationWrapper;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.TeleportUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class BackCommand extends SECommand {

  public BackCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "back";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/back";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    PlayerData data = UsernameResolver.getPlayerData(player);
    if (data != null && data.getLastLocation() != null) {
      if (player instanceof EntityPlayerMP) {
        LocationWrapper last = data.getLastLocation();
        TeleportUtils.doTeleport((EntityPlayerMP) player, last.getX(), last.getY(), last.getZ(),
            last.getDim());
        data.setTeleportTimer(System.currentTimeMillis());
        DataHelper2.forceSave(Keys.PLAYER_DATA, data);
        ChatHelper.sendMessageTo(player, Local.TELEPORT_BACK);
      }
    } else {
      ChatHelper.sendMessageTo(player, Local.INVALID_LASTLOCATION);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Teleport to last known location";
  }
}
