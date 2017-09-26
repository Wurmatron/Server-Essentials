package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.TeamManager;

import javax.annotation.Nullable;
import java.util.List;

public class TeamAdminCommand extends SECommand {

	public TeamAdminCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "teamAdmin";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/teamadmin disband";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteTeam (args,TeamManager.getTeams ());
	}

	public void disband (ICommandSender sender,String[] args) {
		if (args.length >= 1) {
			Team team = TeamManager.getTeamFromName (args[0]);
			if (team != null) {
				TeamManager.removeTeam (team);
				ChatHelper.sendMessageTo (sender,Local.TEAMADMIN_DISBAND.replaceAll ("#",team.getName ()));
			} else
				ChatHelper.sendMessageTo (sender,Local.TEAM_INVALID.replaceAll ("#",args[1]));
		} else
			ChatHelper.sendMessageTo (sender,Local.TEAM_CREATE_MISSING_NAME);
	}
}
