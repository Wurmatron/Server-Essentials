package wurmcraft.serveressentials.common.commands.item;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;

public class RenameCommand extends SECommand {

	public RenameCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "rename";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"name"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/rename <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length > 0) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player.getHeldItemMainhand () != null) {
				player.getHeldItemMainhand ().setStackDisplayName (Strings.join (args," ").replaceAll ("&","\u00A7"));
				ChatHelper.sendMessageTo (player,Local.NAME_CHANGED.replaceAll ("#",Strings.join (args," ").replaceAll ("&","\u00A7")));
			} else
				ChatHelper.sendMessageTo (player,Local.NO_ITEM);
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Rename's the item you are currently holding";
	}
}
