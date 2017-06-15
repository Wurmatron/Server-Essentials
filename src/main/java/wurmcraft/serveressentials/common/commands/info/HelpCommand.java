package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import java.util.ArrayList;
import java.util.List;

/*
 TODO FIX Localization of Vanilla Commands
 */
public class HelpCommand extends EssentialsCommand {

	public HelpCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "help";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/help <#>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Help");
		aliases.add ("HELP");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		int start = 0;
		try {
			if (args.length == 1 && Integer.parseInt (args[0]) != -1)
				start = 8 * Integer.parseInt (args[0]);
			if (start <= server.commandManager.getCommands ().size ()) {
				if (start / 8 == 0)
					ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER.substring (0,19) + " Page # ".replaceAll ("#","" + start / 8) + Local.SPACER.substring (22,49));
				else
					ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER.substring (0,19) + " Page # ".replaceAll ("#","" + start / 8) + Local.SPACER.substring (22,49),clickEvent ((start / 8) - 1),0);
				for (int index = start; index < (start + 8); index++)
					if (index < server.commandManager.getCommands ().size ())
						ChatHelper.sendMessageTo (sender,formatCommand (sender,(ICommand) server.commandManager.getCommands ().values ().toArray ()[index]));
				ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER,clickEvent ((start / 8) + 1),0);
			}
		} catch (NumberFormatException e) {
			ChatHelper.sendMessageTo (sender,Local.INVALID_NUMBER.replaceAll ("#",args[0]));
		}
	}

	private String formatCommand (ICommandSender sender,ICommand command) {
		if (command instanceof EssentialsCommand)
			return TextFormatting.AQUA + "/" + command.getCommandName () + " | " + TextFormatting.DARK_AQUA + ((EssentialsCommand) command).getDescription ();
		else
			return TextFormatting.AQUA + "/" + command.getCommandName () + " | " + TextFormatting.DARK_AQUA + command.getCommandUsage (sender);
	}

	private ClickEvent clickEvent (int index) {
		return new ClickEvent (ClickEvent.Action.RUN_COMMAND,"/help #".replaceAll ("#","" + index));
	}

	@Override
	public String getDescription () {
		return "List of available commands";
	}
}
