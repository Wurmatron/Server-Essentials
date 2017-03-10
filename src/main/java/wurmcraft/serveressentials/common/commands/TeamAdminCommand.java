package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Team;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.TeamManager;

import java.util.ArrayList;
import java.util.List;

public class TeamAdminCommand extends EssentialsCommand {

		public TeamAdminCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "teamadmin";
		}

		@Override
		public List<String> getCommandAliases() {
				List<String> aliases = new ArrayList<>(); aliases.add("tadmin"); return aliases;
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/teamadmin disband";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length >= 1) {
						if (args[0].equalsIgnoreCase("disband")) {
								if (args.length >= 2) {
										Team team = TeamManager.getTeamFromName(args[1]); if (team != null) {
												TeamManager.removeTeam(team);
												sender.addChatMessage(new TextComponentString(Local.TEAMADMIN_DISBAND.replaceAll("#", team.getName())));
										} else sender.addChatMessage(new TextComponentString(Local.TEAM_INVALID.replaceAll("#", args[1])));
								} else sender.addChatMessage(new TextComponentString(Local.TEAM_CREATE_MISSING_NAME));
						}
				}
		}
}
