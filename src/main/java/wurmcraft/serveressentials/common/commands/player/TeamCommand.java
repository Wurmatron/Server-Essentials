package wurmcraft.serveressentials.common.commands.player;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeamManager;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamCommand extends SECommand {

	public TeamCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "team";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"t"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/team join | leave | create <name> | invite | kick <name> | info | set <public | color>";
	}

	@Override
	public boolean hasSubCommand () {
		return true;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
	}

	@Override
	public String getDescription () {
		return "Handles anything to do with joining, creating,leaving and showing info about teams";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		if (args.length == 2 && args[0].equalsIgnoreCase ("invite")) {
			List <String> players = new ArrayList <> ();
			for (EntityPlayerMP player : server.getPlayerList ().getPlayers ())
				players.add (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()));
			return players;
		}
		return null;
	}

	@SubCommand
	public void create (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length > 0) {
			Team team = new Team (Strings.join (Arrays.copyOfRange (args,0,args.length)," "),player.getGameProfile ().getId (),false);
			if (TeamManager.register (team)) {
				DataHelper.setTeam (player.getGameProfile ().getId (),team);
				ChatHelper.sendMessageTo (player,Local.TEAM_CREATED.replaceAll ("#",Strings.join (Arrays.copyOfRange (args,0,args.length)," ")));
			} else
				ChatHelper.sendMessageTo (player,Local.TEAM_NONE);
		} else
			ChatHelper.sendMessageTo (player,Local.TEAM_CREATE_MISSING_NAME);
	}

	@SubCommand
	public void join (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Team team = TeamManager.getTeamFromName (Strings.join (Arrays.copyOfRange (args,0,args.length)," "));
		if (team != null) {
			if (team.canJoin (player.getGameProfile ().getId ())) {
				team.addMember (player.getGameProfile ().getId ());
				DataHelper.saveTeam (team);
				DataHelper.setTeam (player.getGameProfile ().getId (),team);
				ChatHelper.sendMessageTo (player,Local.TEAM_JOINED.replaceAll ("#",team.getName ()));
			} else
				ChatHelper.sendMessageTo (player,Local.TEAM_INVALID.replaceAll ("#",Strings.join (Arrays.copyOfRange (args,0,args.length)," ")));
		} else
			ChatHelper.sendMessageTo (player,Local.TEAM_INVALID.replaceAll ("#",Strings.join (Arrays.copyOfRange (args,0,args.length)," ")));
	}

	@SubCommand
	public void leave (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
		if (team != null) {
			ChatHelper.sendMessageTo (player,Local.TEAM_LEFT.replaceAll ("#",team.getName ()));
			DataHelper.setTeam (player.getGameProfile ().getId (),null);
			team.removeMember (player.getGameProfile ().getId ());
			DataHelper.saveTeam (team);
		}
	}

	@SubCommand
	public void invite (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
		if (team != null && team.getLeader ().equals (player.getGameProfile ().getId ())) {
			if (args.length == 1) {
				EntityPlayer user = UsernameResolver.getPlayer (args[0]);
				if (user != null) {
					ChatHelper.sendMessageTo (player,Local.TEAM_INVITED.replaceAll ("#",user.getDisplayName ().getUnformattedText ()));
					ChatHelper.sendMessageTo (user,Local.TEAM_INVITED_OTHER.replaceAll ("#",team.getName ()));
					team.addPossibleMember (user.getGameProfile ().getId ());
					DataHelper.saveTeam (team);
				}
			} else
				ChatHelper.sendMessageTo (player,Local.TEAM_MISSING_NAME);
		} else if (team != null)
			ChatHelper.sendMessageTo (player,Local.TEAM_LEADER_PERM);
	}

	@SubCommand
	public void kick (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
		if (team != null && team.getLeader ().equals (player.getGameProfile ().getId ())) {
			if (args.length == 1 && args[0] != null) {
				for (UUID key : UsernameCache.getMap ().keySet ())
					if (UsernameCache.getLastKnownUsername (key).equalsIgnoreCase (args[0])) {
						PlayerData data = DataHelper.getPlayerData (key);
						if (data == null)
							DataHelper.loadPlayerData (key);
						DataHelper.setTeam (key,null);
						team.removeMember (key);
						DataHelper.saveTeam (team);
						EntityPlayer user = UsernameResolver.getPlayer (args[0]);
						ChatHelper.sendMessageTo (user,Local.TEAM_KICKED);
						ChatHelper.sendMessageTo (player,Local.TEAM_KICKED_OTHER.replaceAll ("#",UsernameCache.getLastKnownUsername (key)));
					}
			}
		} else if (team != null)
			player.sendMessage (new TextComponentString (Local.TEAM_LEADER_PERM));
	}

	@SubCommand
	public void info (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
		if (team != null) {
			ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.SPACER);
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Name: " + team.getName ());
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Owner: " + UsernameCache.getLastKnownUsername (team.getLeader ()));
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Open: " + team.isPublic ());
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Color: " + team.getTeamColor ().getFriendlyName ());
			if (team.getMembers ().size () > 0) {
				List <String> members = new ArrayList <> ();
				for (UUID mem : team.getMembers ().keySet ())
					members.add (UsernameCache.getLastKnownUsername (mem));
				ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Members: " + Strings.join (members,", "));
			}
			ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.SPACER);
		} else if (team == null) {
			ChatHelper.sendMessageTo (player,Local.TEAM_NONE);
		} else if (args.length == 1 && TeamManager.getTeamFromName (args[0]) != null) {
			team = TeamManager.getTeamFromName (args[0]);
			ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.SPACER);
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Name: " + team.getName ());
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Owner: " + UsernameCache.getLastKnownUsername (team.getLeader ()));
			ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Open: " + team.isPublic ());
			if (team.getMembers ().size () > 0) {
				List <String> members = new ArrayList <> ();
				for (UUID mem : team.getMembers ().keySet ())
					members.add (UsernameCache.getLastKnownUsername (mem));
				ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Members: " + Strings.join (members,", "));
			}
			ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.SPACER);
		} else
			ChatHelper.sendMessageTo (player,getUsage (sender));
	}

	@SubCommand
	public void set (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
		if (team != null && team.getLeader ().equals (player.getGameProfile ().getId ())) {
			if (args.length == 0)
				ChatHelper.sendMessageTo (player,getUsage (sender));
			else if (args.length > 1 && args[0].equalsIgnoreCase ("public")) {
				if (args.length > 1 && args[1].equalsIgnoreCase ("true") || args[1].equalsIgnoreCase ("yes")) {
					team.setPublic (true);
					DataHelper.saveTeam (team);
				} else if (args.length > 1 && args[1].equalsIgnoreCase ("false") || args[1].equalsIgnoreCase ("no")) {
					team.setPublic (false);
					DataHelper.saveTeam (team);
					ChatHelper.sendMessageTo (player,Local.TEAM_SET_VALUE.replaceAll ("#","public").replaceAll ("$",args[1]));
				} else
					ChatHelper.sendMessageTo (player,Local.TEAM_INVAID_VALUE.replaceAll ("#",args[0]).replaceAll ("$",args[1]));
			} else if (args.length > 1 && args[0].equalsIgnoreCase ("color")) {
				TextFormatting color = TextFormatting.getValueByName (args[1]);
				if (color != null) {
					team.setColor (color);
					DataHelper.saveTeam (team);
					ChatHelper.sendMessageTo (player,Local.TEAM_SET_VALUE.replaceAll ("#","color").replaceAll ("$",color.getFriendlyName ()));
				} else {
					ChatHelper.sendMessageTo (player,getUsage (sender));
					List <String> colors = new ArrayList <> ();
					for (TextFormatting te : TextFormatting.values ())
						colors.add (te.getFriendlyName ());
					ChatHelper.sendMessageTo (player,Strings.join (colors.toArray (new String[0])," "));
				}
			} else
				ChatHelper.sendMessageTo (player,getUsage (sender));
		} else if (team != null)
			ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.TEAM_LEADER_PERM);
		else
			ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.TEAM_NONE);
	}
}
