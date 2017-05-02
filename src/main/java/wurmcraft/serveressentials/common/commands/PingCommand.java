package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class PingCommand extends EssentialsCommand {

	public PingCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "ping";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/ping";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		sender.addChatMessage (new TextComponentString ("pongy!"));
	}
}
