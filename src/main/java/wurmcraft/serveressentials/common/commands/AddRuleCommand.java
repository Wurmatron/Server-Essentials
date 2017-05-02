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

public class AddRuleCommand extends EssentialsCommand {


	public AddRuleCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "addRule";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/addRule <rule>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("AddRule");
		aliases.add ("addrule");
		aliases.add ("AddRule");
		aliases.add ("ADDRULE");
		return aliases;
	}


	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			String rule = Strings.join (args," ");
			DataHelper.globalSettings.addRule (rule);
			ChatManager.sendMessage (sender,Local.RULE_CREATED.replaceAll ("#",rule));
		} else
			ChatManager.sendMessage (sender,getCommandUsage (sender));
	}
}
