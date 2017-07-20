package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeleteRuleCommand extends SECommand {

	public DeleteRuleCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "deleteRule";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/remRule <rule No.>";
	}

//	@Override
//	public String[] getAliases () {
//		return new String[] {"delRule", "removeRule", "remRule"};
//	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			Integer ruleIndex = Integer.parseInt (args[0]);
			if (ruleIndex >= 0 && DataHelper.globalSettings.getRules ().length > ruleIndex) {
				DataHelper.globalSettings.removeRule (ruleIndex);
				ChatHelper.sendMessageTo (sender,Local.RULE_REMOVED.replaceAll ("#",args[0]));
			} else
				ChatHelper.sendMessageTo (sender,Local.RULE_INVALID_INDEX.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			for (int i = 0; i > DataHelper.globalSettings.getRules ().length; i++)
				list.add (Integer.toString (i));
		return list;
	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Removes a line from the Rules";
	}
}
