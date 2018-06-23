package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.TeamManager;

public class TeamAdminCommand extends SECommand {

  public TeamAdminCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "teamAdmin";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/teamadmin disband";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length <= 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
    super.execute(server, sender, args);
  }

  @SubCommand
  public void disband(ICommandSender sender, String[] args) {
    if (args.length >= 1) {
      Team team = TeamManager.getTeamFromName(args[0]);
      if (team != null) {
        TeamManager.removeTeam(team);
        ChatHelper.sendMessageTo(sender, Local.TEAMADMIN_DISBAND.replaceAll("#", team.getName()));
      } else {
        ChatHelper.sendMessageTo(sender, Local.TEAM_INVALID.replaceAll("#", args[1]));
      }
    } else {
      ChatHelper.sendMessageTo(sender, Local.TEAM_CREATE_MISSING_NAME);
    }
  }
}
