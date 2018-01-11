package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

import javax.annotation.Nullable;
import java.util.List;

public class DPFCommand extends SECommand {

	public DPFCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "deletePlayerFile";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"delPlayerFile","dPlayerFile","dpf"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/DeletePlayerFile <Username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		if (args.length == 1) {
		//			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
		//			if (player != null) {
		//				DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
		//				player.onKillCommand ();
		//				((EntityPlayerMP) player).connection.disconnect (new TextComponentString (Local.PLAYER_FILE_DELETE));
		//				File playerFile = new File (server.getDataDirectory (),File.separator + server.getFolderName () + File.separator + "playerdata" + File.separator + player.getGameProfile ().getId ().toString () + ".dat");
		//				LogHandler.info ("Deleting " + playerFile.getName ());
		//				ChatHelper.sendMessageTo (sender,Local.PLAYER_FILE_DELETE_OTHER.replaceAll ("#",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())));
		//			} else
		//				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		//		} else
		//			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Deletes a certain players player file";
	}
}
