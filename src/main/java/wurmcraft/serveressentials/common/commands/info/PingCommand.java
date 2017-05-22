package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import java.util.ArrayList;
import java.util.List;

public class PingCommand extends EssentialsCommand {

	public PingCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "ping";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Ping");
		aliases.add ("PING");
		return aliases;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/ping";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		ChatHelper.sendMessageTo (sender,Local.PING_REPLY);
	}

	@Override
	public String getDescription () {
		return "Displays a message from the server";
	}
}
