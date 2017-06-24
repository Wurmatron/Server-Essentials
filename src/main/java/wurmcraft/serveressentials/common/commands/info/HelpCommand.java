package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends EssentialsCommand {

	public static final int chatWidth = 54;

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
				String nPage = " Page # ".replaceAll("#", "" + start / 8);
				StringBuilder b = new StringBuilder();
				int startPos = (int)Math.floor((chatWidth-nPage.length())/2);
				b.append(Local.SPACER.substring(0, startPos-1) + nPage);
				b.append(Local.SPACER.substring(0, chatWidth-b.length()));
				if (start/8==0) ChatHelper.sendMessageTo(sender, TextFormatting.RED + b.toString());
				else ChatHelper.sendMessageTo(sender, TextFormatting.RED + b.toString(), clickEvent((start/8)-1));
				for (int index = start; index < (start + 8); index++)
					if (index < server.commandManager.getCommands ().size ()) {
						TextComponentTranslation temp = new TextComponentTranslation (formatCommand (sender,(ICommand) server.commandManager.getCommands ().values ().toArray ()[index]));
						temp.setStyle (new Style ().setColor (TextFormatting.DARK_AQUA));
						sender.addChatMessage (temp);
					}
				ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER,clickEvent ((start / 8) + 1));
			}
		} catch (NumberFormatException e) {
			ChatHelper.sendMessageTo (sender,Local.INVALID_NUMBER.replaceAll ("#",args[0]));
		}
	}

	private String formatCommand (ICommandSender sender,ICommand command) {
		if (command instanceof EssentialsCommand)
			return TextFormatting.AQUA + "/" + command.getCommandName () + " | " + TextFormatting.DARK_AQUA + ((EssentialsCommand) command).getDescription ();
		else
			return command.getCommandUsage (sender);
	}


	private ClickEvent clickEvent (int index) {
		return new ClickEvent (ClickEvent.Action.RUN_COMMAND,"/help #".replaceAll ("#","" + index));
	}

	@Override
	public String getDescription () {
		return "List of available commands";
	}
}
