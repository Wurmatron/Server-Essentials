package wurmcraft.serveressentials.common.commands.admin;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class AddMotdCommand extends SECommand {

	public AddMotdCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "addMotd";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"aMotd"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/addMotd <motd>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			String rule = Strings.join (args," ");
			DataHelper.globalSettings.addMotd (rule);
			ChatHelper.sendMessageTo (sender,Local.MOTD_CREATED.replaceAll ("#",rule));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public String getDescription () {
		return "Adds a line to the MOTD";
	}


	@Override
	public boolean canConsoleRun () {
		return true;
	}
}
