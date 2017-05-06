package wurmcraft.serveressentials.common.commands.player;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeamManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamCommand extends EssentialsCommand {

	public TeamCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "team";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("t");
		aliases.add ("Team");
		aliases.add ("T");
		aliases.add ("TEAM");
		return aliases;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/team join | leave | create | invite | kick";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase ("create")) {
					if (args.length >= 1) {
						Team team = new Team (Strings.join (Arrays.copyOfRange (args,1,args.length)," "),player.getGameProfile ().getId (),false);
						if (TeamManager.register (team)) {
							DataHelper.setTeam (player.getGameProfile ().getId (),team);
							ChatHelper.sendMessageTo (player,Local.TEAM_CREATED.replaceAll ("#",Strings.join (Arrays.copyOfRange (args,1,args.length)," ")));
						}
					} else
						ChatHelper.sendMessageTo (player,Local.TEAM_CREATE_MISSING_NAME);
				} else if (args[0].equalsIgnoreCase ("join")) {
					if (args.length >= 1) {
						Team team = TeamManager.getTeamFromName (Strings.join (Arrays.copyOfRange (args,1,args.length)," "));
						if (team != null) {
							if (team.canJoin (player.getGameProfile ().getId ())) {
								team.addMember (player.getGameProfile ().getId ());
								DataHelper.saveTeam (team);
								DataHelper.setTeam (player.getGameProfile ().getId (),team);
								ChatHelper.sendMessageTo (player,Local.TEAM_JOINED.replaceAll ("#",team.getName ()));
							}
						} else
							ChatHelper.sendMessageTo (player,Local.TEAM_INVALID.replaceAll ("#",Strings.join (Arrays.copyOfRange (args,1,args.length)," ")));
					}
				} else if (args[0].equalsIgnoreCase ("leave")) {
					Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
					if (team != null) {
						ChatHelper.sendMessageTo (player,Local.TEAM_LEFT.replaceAll ("#",team.getName ()));
						DataHelper.setTeam (player.getGameProfile ().getId (),null);
						team.removeMember (player.getGameProfile ().getId ());
						DataHelper.saveTeam (team);
					}
				} else if (args[0].equalsIgnoreCase ("invite")) {
					Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
					if (team != null && team.getLeader ().equals (player.getGameProfile ().getId ())) {
						if (args.length == 2) {
							PlayerList players = server.getServer ().getPlayerList ();
							if (players.getPlayerList ().size () > 0)
								for (EntityPlayerMP user : players.getPlayerList ())
									if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[1]).getId ())) {
										ChatHelper.sendMessageTo (player,Local.TEAM_INVITED.replaceAll ("#",user.getDisplayName ().getUnformattedText ()));
										ChatHelper.sendMessageTo (user,Local.TEAM_INVITED_OTHER.replaceAll ("#",team.getName ()));
										team.addPossibleMember (user.getGameProfile ().getId ());
										DataHelper.saveTeam (team);
									}
						} else
							ChatHelper.sendMessageTo (player,Local.TEAM_MISSING_NAME);
					} else if (team != null)
						ChatHelper.sendMessageTo (player,Local.TEAM_LEADER_PERM);
				} else if (args[0].equalsIgnoreCase ("kick")) {
					Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
					if (team != null && team.getLeader ().equals (player.getGameProfile ().getId ())) {
						if (args.length == 1 && args[1] != null) {
							for (UUID key : UsernameCache.getMap ().keySet ())
								if (UsernameCache.getLastKnownUsername (key).equalsIgnoreCase (args[1])) {
									PlayerData data = DataHelper.getPlayerData (key);
									if (data == null)
										DataHelper.loadPlayerData (key);
									DataHelper.setTeam (key,null);
									team.removeMember (key);
									DataHelper.saveTeam (team);
									PlayerList players = server.getServer ().getPlayerList ();
									if (players.getPlayerList ().size () > 0)
										for (EntityPlayerMP user : players.getPlayerList ())
											if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[1]).getId ()))
												ChatHelper.sendMessageTo (user,Local.TEAM_KICKED);
									ChatHelper.sendMessageTo (player,Local.TEAM_KICKED_OTHER.replaceAll ("#",UsernameCache.getLastKnownUsername (key)));
								}
						}
					} else if (team != null)
						player.addChatComponentMessage (new TextComponentString (Local.TEAM_LEADER_PERM));
				} else if (args[0].equalsIgnoreCase ("info")) {
					Team team = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeam ();
					if (team != null) {
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
					}
				}
			} else
				ChatHelper.sendMessageTo (player,getCommandUsage (player));
		}
	}
}