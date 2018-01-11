package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

import javax.annotation.Nullable;
import java.util.List;

public class SeenCommand extends SECommand {

	public SeenCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "seen";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/seen <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		if (args.length > 0) {
		//			String resolvedUsername = UsernameResolver.usernameFromNickname (args[0]);
		//			if (resolvedUsername == null) {
		//				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("\"#\"",args[0]));
		//				return;
		//			}
		//			if (args.length > 0) {
		//				PlayerList players = server.getServer ().getPlayerList ();
		//				boolean displayed = false;
		//				if (players.getPlayers ().size () > 0)
		//					for (EntityPlayerMP user : players.getPlayers ())
		//						if (user.getGameProfile ().getId ().equals (UsernameResolver.getPlayerUUID (resolvedUsername))) {
		//							ChatHelper.sendMessageTo (sender,(TextFormatting.GREEN + "Player: '" + UsernameCache.getLastKnownUsername (user.getGameProfile ().getId ()) + "' is currently online!"));
		//							displayed = true;
		//						}
		//				if (!displayed) {
		//					for (UUID username : UsernameCache.getMap ().keySet ())
		//						if (UsernameCache.getLastKnownUsername (username).equals (args[0])) {
		//							PlayerData data = DataHelper.loadPlayerData (username);
		//							if (data != null) {
		//								ChatHelper.sendMessageTo (sender,Local.LAST_SEEN.replaceAll ("#",TextFormatting.DARK_AQUA + new Date (data.getLastseen ()).toString ()));
		//								displayed = true;
		//							}
		//							DataHelper.unloadPlayerData (username);
		//						}
		//				} else if (!displayed)
		//					ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		//			}
		//		} else
		//			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Finds when a player was last on the server";
	}
}
