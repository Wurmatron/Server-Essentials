package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

public class BackCommand extends SECommand {

	public BackCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "back";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/back";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		super.execute (server,sender,args);
		//		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		//		PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
		//		if (data != null && data.getLastLocation () != null) {
		//			BlockPos lastLocation = data.getLastLocation ();
		//			player.setPositionAndUpdate (lastLocation.getX (),lastLocation.getY (),lastLocation.getZ ());
		//			DataHelper.updateTeleportTimer (player.getGameProfile ().getId ());
		//			ChatHelper.sendMessageTo (player,Local.TELEPORT_BACK);
		//		} else
		//			ChatHelper.sendMessageTo (player,Local.INVALID_LASTLOCATION);
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Teleport to last known location";
	}
}
