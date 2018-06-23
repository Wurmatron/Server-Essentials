package wurmcraft.serveressentials.common.commands.player;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.api.storage.LocationWrapper;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.TeleportUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class WarpCommand extends SECommand {

  public WarpCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "warp";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/warp <name>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      list(sender, args);
      return;
    }
    if (!args[0].equalsIgnoreCase("list") && DataHelper2.get(Keys.WARP, args[0]) != null) {
      Warp warp = (Warp) DataHelper2.get(Keys.WARP, args[0]);
      EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
      if (TeleportUtils.canTeleport(player.getGameProfile().getId())) {
        player.setPositionAndRotation(warp.getPos().getX(), warp.getPos().getY(),
            warp.getPos().getZ(), warp.getYaw(), warp.getPitch());
        PlayerData data = UsernameResolver
            .getPlayerData(player.getGameProfile().getId().toString());
        data.setLastLocation(
            new LocationWrapper((int) player.posX, (int) player.posY, (int) player.posZ,
                player.dimension));
        DataHelper2.forceSave(Keys.PLAYER_DATA, data);
        TeleportUtils.teleportTo(player, warp.getPos(), warp.getDimension(), true);
        ChatHelper.sendMessageTo(player, Local.WARP_TELEPORT.replaceAll("#", warp.getName()));
      } else if (!TeleportUtils.canTeleport(player.getGameProfile().getId())) {
        ChatHelper.sendMessageTo(player, Local.TELEPORT_COOLDOWN
            .replaceAll("#", TeleportUtils.getRemainingCooldown(player.getGameProfile().getId())));
      }
    } else {
      ChatHelper.sendMessageTo(sender, Local.WARP_NONE.replaceAll("#", args[0]));
    }
  }

  @SubCommand
  public void list(ICommandSender sender, String[] args) {
    List<String> warps = new ArrayList<>();
    for (IDataType warp : DataHelper2.getData(Keys.WARP)) {
      warps.add(((Warp) warp).getName());
    }
    if (warps.size() > 0) {
      ChatHelper.sendMessageTo(sender,
          TextFormatting.DARK_AQUA + "Warps: " + TextFormatting.AQUA + Strings.join(warps, ", "));
    } else {
      ChatHelper.sendMessageTo(sender, Local.WARPS_NONE);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Teleport to a specified \"Warp\" location";
  }

  //		private HoverEvent hoverEvent (Warp warp) {
  //					return new HoverEvent (HoverEvent.Action.SHOW_TEXT,CommandUtils.di (warp));
  //			return null;
  //		}

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    return autoComplete(args, DataHelper2.getData(Keys.WARP));
  }
}
