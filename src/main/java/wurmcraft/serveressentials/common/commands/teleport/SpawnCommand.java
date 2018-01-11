package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

public class SpawnCommand extends SECommand {

	public SpawnCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "spawn";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/spawn";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		super.execute (server,sender,args);
		//		EntityPlayer player = (EntityPlayer) sender;
		//		long teleport_timer = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeleportTimer ();
		//		if (DataHelper.globalSettings.getSpawn () != null && (teleport_timer + (Settings.teleport_cooldown * 1000)) <= System.currentTimeMillis ()) {
		//			SpawnPoint spawn = DataHelper.globalSettings.getSpawn ();
		//			DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
		//			player.setLocationAndAngles (spawn.location.getX (),spawn.location.getY (),spawn.location.getZ (),spawn.yaw,spawn.pitch);
		//			TeleportUtils.teleportTo (player,spawn.location,spawn.dimension,true);
		//			ChatHelper.sendMessageTo (player,Local.SPAWN_TELEPORTED);
		//		} else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis ())
		//			ChatHelper.sendMessageTo (player,TeleportUtils.getRemainingCooldown (player.getGameProfile ().getId ()));
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Teleport to the server's spawn";
	}
}
