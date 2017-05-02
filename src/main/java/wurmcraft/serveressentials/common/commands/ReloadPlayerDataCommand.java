package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class ReloadPlayerDataCommand extends EssentialsCommand {

	public ReloadPlayerDataCommand (final String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "reloadPlayerData";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/reloadPlayerData <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && args[0] != null) {
			PlayerList players = server.getPlayerList ();
			if (players.getCurrentPlayerCount () > 0) {
				for (EntityPlayerMP player : players.getPlayerList ())
					if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()) != null && UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
						DataHelper.unloadPlayerData (player.getGameProfile ().getId ());
						DataHelper.loadPlayerData (player.getGameProfile ().getId ());
						player.addChatComponentMessage (new TextComponentString ("Your PlayerData has been reloaded!"));
						sender.addChatMessage (new TextComponentString ("#'s player data has been reloaded".replaceAll ("#",player.getDisplayNameString ())));
					}
			}
		}
	}
}
