package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.SpawnPoint;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand extends EssentialsCommand {

	public SpawnCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "spawn";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Spawn");
		return aliases;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/spawn";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			long teleport_timer = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeleport_timer ();
			if (DataHelper.globalSettings.getSpawn () != null && (teleport_timer + (Settings.teleport_cooldown * 1000)) <= System.currentTimeMillis ()) {
				SpawnPoint spawn = DataHelper.globalSettings.getSpawn ();
				DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
				player.setLocationAndAngles (spawn.location.getX (),spawn.location.getY (),spawn.location.getZ (),spawn.yaw,spawn.pitch);
				TeleportUtils.teleportTo (player,spawn.location,true);
				player.dimension = spawn.dimension;
				player.addChatComponentMessage (new TextComponentString (Local.SPAWN_TELEPORTED));
			} else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis ())
				sender.addChatMessage (new TextComponentString (Local.TELEPORT_COOLDOWN.replace ("#",Integer.toString (Math.round ((System.currentTimeMillis () - teleport_timer))))));
		} else
			sender.addChatMessage (new TextComponentString ("Command can only be run by players!"));
	}
}
