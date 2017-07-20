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

public class AddRuleCommand extends SECommand {

	public AddRuleCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "addRule";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/addRule <rule>";
	}

//	@Override
//	public String[] getAliases () {
//		return new String[] {"addRule","aRule"};
//	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			String rule = Strings.join (args," ");
			DataHelper.globalSettings.addRule (rule);
			ChatHelper.sendMessageTo (sender,Local.RULE_CREATED.replaceAll ("#",rule));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public String getDescription () {
		return "Add a Rule to the list";
	}


	@Override
	public boolean canConsoleRun () {
		return true;
	}
}
