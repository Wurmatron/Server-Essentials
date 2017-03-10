package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class DeleteRuleCommand extends EssentialsCommand {

		public DeleteRuleCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "deleteRule";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/removeRule <rule No.>";
		}

		@Override
		public List<String> getCommandAliases() {
				List<String> aliases = new ArrayList<>(); aliases.add("remrule"); return aliases;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length > 0) {
						Integer ruleIndex = Integer.parseInt(args[0]); if (ruleIndex >= 0) {
								DataHelper.globalSettings.removeRule(ruleIndex);
								sender.addChatMessage(new TextComponentString(Local.RULE_REMOVED.replaceAll("#", args[0])));
						} else sender.addChatMessage(new TextComponentString(Local.RULE_INVALID_INDEX.replaceAll("#", args[0])));
				} else sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
		}
}
