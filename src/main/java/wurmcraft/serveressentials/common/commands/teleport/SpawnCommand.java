package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.storage.SpawnPoint;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.TeleportUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class SpawnCommand extends SECommand {

  public SpawnCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "spawn";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/spawn";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender;
    PlayerData data = UsernameResolver.getPlayerData(player.getGameProfile().getId());
    long teleport_timer = data.getTeleportTimer();
    if (DataHelper2.globalSettings.getSpawn() != null
        && (teleport_timer + (ConfigHandler.teleportCooldown * 1000)) <= System
        .currentTimeMillis()) {
      SpawnPoint spawn = DataHelper2.globalSettings.getSpawn();
      TeleportUtils.teleportTo(player, spawn.location, spawn.dimension, true);
      ChatHelper.sendMessageTo(player, Local.SPAWN_TELEPORTED);
      DataHelper2.forceSave(Keys.PLAYER_DATA, data);
    } else if ((teleport_timer + (ConfigHandler.teleportCooldown * 1000)) > System
        .currentTimeMillis()) {
      ChatHelper.sendMessageTo(player,
          TeleportUtils.getRemainingCooldown(player.getGameProfile().getId()));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Teleport to the server's spawn";
  }
}
