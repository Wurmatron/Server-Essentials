package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class TpHereCommand extends SECommand {

	public TpHereCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "tphere";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/tphere <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		super.execute (server,sender,args);
		//		EntityPlayer player = (EntityPlayer) sender;
		//		if (args.length == 1) {
		//			EntityPlayer here = UsernameResolver.getPlayer (args[0]);
		//			if (here != null) {
		//				here.setPositionAndUpdate (player.posX,player.posY,player.posZ);
		//				PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
		//				String playerName = data.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + data.getNickname ().replaceAll ("&","\u00A7") : here.getDisplayNameString ();
		//				ChatHelper.sendMessageTo (here,Local.TELEPORT_TO.replaceAll ("#",playerName));
		//				PlayerData hereData = DataHelper.getPlayerData (here.getGameProfile ().getId ());
		//				String hereName = hereData.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + hereData.getNickname ().replaceAll ("&","\u00A7") : here.getDisplayNameString ();
		//				ChatHelper.sendMessageTo (player,Local.TELEPORTED_FROM.replaceAll ("#",hereName).replaceAll ("%","You"));
		//			} else
		//				ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		//		} else
		//			ChatHelper.sendMessageTo (player,getUsage (sender));
	}

	@Override
	public String getDescription () {
		return "Deny a teleport request";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> args0 = autoCompleteUsername (args,0);
		if (UsernameResolver.getPlayer (args[0]) != null)
			return autoCompleteUsername (args,1);
		return args0;
	}
}
