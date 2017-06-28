package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.test.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class TpdenyCommand extends SECommand {

	public TpdenyCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "tpdeny";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/tpdeny";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender;
		if (DataHelper.activeRequests.size () > 0) {
			for (long time : DataHelper.activeRequests.keySet ()) {
				EntityPlayer[] otherPlayer = DataHelper.activeRequests.get (time);
				if (otherPlayer[1].getGameProfile ().getId ().equals (player.getGameProfile ().getId ())) {
					DataHelper.activeRequests.remove (time);
					ChatHelper.sendMessageTo (player,Local.TPA_DENY);
				}
			}
		} else
			ChatHelper.sendMessageTo (player,Local.TPA_NONE);
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Deny a teleport request";
	}
}
