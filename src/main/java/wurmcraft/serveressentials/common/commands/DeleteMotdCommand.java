package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class DeleteMotdCommand extends EssentialsCommand {

		public DeleteMotdCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "deleteMotd";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/removeMotd <motd No.>";
		}

		@Override
		public List<String> getCommandAliases() {
				List<String> aliases = new ArrayList<>(); aliases.add("remmotd"); return aliases;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length > 0) {
						Integer motdIndex = Integer.parseInt(args[0]); if (motdIndex >= 0) {
								DataHelper.globalSettings.removeMotd(motdIndex);
								sender.addChatMessage(new TextComponentString(Local.MOTD_REMOVED.replaceAll("#", args[0])));
						} else sender.addChatMessage(new TextComponentString(Local.MOTD_INVALID_INDEX.replaceAll("#", args[0])));
				} else sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
		}
}
