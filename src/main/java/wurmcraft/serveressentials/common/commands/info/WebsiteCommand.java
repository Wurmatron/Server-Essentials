package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class WebsiteCommand extends EssentialsCommand {

	public WebsiteCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "website";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/website";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Website");
		aliases.add ("WEBSITE");
		aliases.add ("web");
		aliases.add ("Web");
		aliases.add ("WEB");
		return aliases;
	}

	@Override
	public String getDescription () {
		return "Displays the server's website";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		ChatHelper.sendMessageTo (sender,TextFormatting.RED + "Website: " + TextFormatting.GOLD + DataHelper.globalSettings.getWebsite ());
	}
}
