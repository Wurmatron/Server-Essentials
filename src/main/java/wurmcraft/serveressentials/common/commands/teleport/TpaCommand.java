package wurmcraft.serveressentials.common.commands.teleport;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.TeleportUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class TpaCommand extends SECommand {

  public static HashMap<Long, EntityPlayer[]> activeRequests = new HashMap<>();

  public TpaCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "tpa";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/tpa <user>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase(player.getDisplayNameString())) {
        ChatHelper.sendMessageTo(player, Local.TPA_SELF);
        return;
      }
      EntityPlayer otherPlayer = UsernameResolver.getPlayer(args[0]);
      if (otherPlayer != null) {
        if (!activeRequests.values().contains(new EntityPlayer[]{player, otherPlayer})) {
          ChatHelper.sendMessageTo(otherPlayer,
              Local.TPA_REQUEST.replaceAll("#", player.getDisplayName().getUnformattedText()));
          ChatHelper.sendMessageTo(player, Local.TPA_REQUEST_SENT
              .replaceAll("#", otherPlayer.getDisplayName().getUnformattedText()));
          TeleportUtils.addTeleport(player, otherPlayer);
        }
      } else {
        ChatHelper.sendMessageTo(sender, Local.TPA_USER_NOTFOUND);
      }
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Request to teleport to another player";
  }
}
