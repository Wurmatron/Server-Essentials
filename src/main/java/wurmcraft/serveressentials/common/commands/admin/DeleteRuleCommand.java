package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeleteRuleCommand extends EssentialsCommand {

	public DeleteRuleCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "deleteRule";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/remRule <rule No.>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("RemoveRule");
		aliases.add ("REMOVERULE");
		aliases.add ("remrule");
		aliases.add ("Remrule");
		aliases.add ("RemRule");
		aliases.add ("REMRULE");
		aliases.add ("DeleteRule");
		aliases.add ("deleterule");
		aliases.add ("deleteRule");
		aliases.add ("Deleterule");
		aliases.add ("DELETERULE");
		aliases.add ("DelRule");
		aliases.add ("delrule");
		aliases.add ("Delrule");
		aliases.add ("DELRULE");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			Integer ruleIndex = Integer.parseInt (args[0]);
			if (ruleIndex >= 0) {
				DataHelper.globalSettings.removeRule (ruleIndex);
				ChatHelper.sendMessageTo (sender,Local.RULE_REMOVED.replaceAll ("#",args[0]));
			} else
				ChatHelper.sendMessageTo (sender,Local.RULE_INVALID_INDEX.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			for (int i = 0; i > DataHelper.globalSettings.getRules ().length; i++)
				list.add (Integer.toString (i)); return list;
	}
}
