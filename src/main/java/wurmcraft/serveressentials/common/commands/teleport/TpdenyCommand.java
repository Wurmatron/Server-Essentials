package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ChatManager;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class TpdenyCommand extends EssentialsCommand {

	public TpdenyCommand (String perm) {
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
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (DataHelper.activeRequests.size () > 0) {
				for (long time : DataHelper.activeRequests.keySet ()) {
					EntityPlayer[] otherPlayer = DataHelper.activeRequests.get (time);
					if (otherPlayer[1].getGameProfile ().getId ().equals (player.getGameProfile ().getId ())) {
						DataHelper.activeRequests.remove (time);
						ChatManager.sendMessage (player,Local.TPA_DENY);
					}
				}
			} else
				ChatManager.sendMessage (player,Local.TPA_NONE);
		}  else
			ChatManager.sendMessage (sender,Local.PLAYER_ONLY);
	}
}
