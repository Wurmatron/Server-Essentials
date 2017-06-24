package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

public class TpacceptCommand extends EssentialsCommand {

	public TpacceptCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "tpaccept";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/tpaccept";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender;
		if (DataHelper.activeRequests.size () > 0) {
			for (long time : DataHelper.activeRequests.keySet ()) {
				EntityPlayer[] otherPlayer = DataHelper.activeRequests.get (time);
				if (otherPlayer[1].getGameProfile ().getId ().equals (player.getGameProfile ().getId ())) {
					DataHelper.setLastLocation (otherPlayer[0].getGameProfile ().getId (),otherPlayer[0].getPosition ());
					TeleportUtils.teleportTo (otherPlayer[0],player.getPosition (),true);
					ChatHelper.sendMessageTo (otherPlayer[1],Local.TPA_ACCEPED_OTHER.replaceAll ("#",otherPlayer[0].getDisplayName ().getUnformattedText ()));
					ChatHelper.sendMessageTo (otherPlayer[0],Local.TPA_ACCEPTED.replaceAll ("#",otherPlayer[1].getDisplayName ().getUnformattedText ()));
					DataHelper.activeRequests.remove (time);
				}
			}
		}
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Accept a teleport request";
	}
}
