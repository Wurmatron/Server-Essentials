package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.utils.LogHandler;

import java.io.File;

public class DPFCommand extends EssentialsCommand {

	public DPFCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "deleteplayerfile";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/DeletePlayerFile <Username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1) {
			PlayerList players = FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ();
			if (players.getCurrentPlayerCount () > 0) {
				for (EntityPlayerMP player : players.getPlayerList ()) {
					if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
						player.onKillCommand ();
						player.connection.kickPlayerFromServer ("You have been killed along with your player file deleted!");
						File playerFile = new File (server.getDataDirectory (),File.separator + server.getFolderName () + File.separator + "playerdata" + File.separator + player.getGameProfile ().getId ().toString () + ".dat");
						LogHandler.info ("Deleting " + playerFile.getName ());

					}
				}
			}
		}
	}
}
