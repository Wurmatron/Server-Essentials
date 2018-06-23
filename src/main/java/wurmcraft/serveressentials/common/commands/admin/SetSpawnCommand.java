package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.SpawnPoint;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class SetSpawnCommand extends SECommand {

  public SetSpawnCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "setSpawn";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/setSpawn";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    EntityPlayer player = (EntityPlayer) sender;
    DataHelper2.globalSettings
        .setSpawn(new SpawnPoint(player.getPosition(), player.rotationYaw, player.rotationPitch));
    player.world.setSpawnPoint(player.getPosition());
    ChatHelper.sendMessageTo(player,
        Local.SPAWN_SET.replaceAll("@", "" + DataHelper2.globalSettings.getSpawn().dimension),
        hoverEvent(DataHelper2.globalSettings.getSpawn()));
  }

  public HoverEvent hoverEvent(SpawnPoint home) {
    return new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatHelper.displayLocation(home));
  }

  @Override
  public String getDescription() {
    return "Sets the worlds spawn point";
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
