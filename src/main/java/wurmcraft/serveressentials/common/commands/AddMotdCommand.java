package wurmcraft.serveressentials.common.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ChatManager;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class AddMotdCommand extends EssentialsCommand {

	public AddMotdCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "addMotd";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/addMotd <motd>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("AddMotd");
		aliases.add ("ADDMOTD");
		aliases.add ("addmotd");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			String rule = Strings.join (args," ");
			DataHelper.globalSettings.addMotd (rule);
			ChatManager.sendMessage (sender,Local.MOTD_CREATED.replaceAll ("#",rule));
		} else
			ChatManager.sendMessage (sender,getCommandUsage (sender));
	}
}
