package wurmcraft.serveressentials.common.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Team;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeamManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamCommand extends EssentialsCommand {

		public TeamCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "team";
		}

		@Override
		public List<String> getCommandAliases() {
				List<String> aliases = new ArrayList<>(); aliases.add("t"); return aliases;
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/team join | leave | create | invite";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (sender instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) sender; if (args.length > 0) {
								if (args[0].equalsIgnoreCase("create")) {
										if (args.length >= 1) {
												Team team = new Team(Strings.join(Arrays.copyOfRange(args, 1, args.length), " "), player.getGameProfile().getId(), false);
												if (TeamManager.register(team)) {
														DataHelper.setTeam(player.getGameProfile().getId(), team);
														player.addChatComponentMessage(new TextComponentString(Local.TEAM_CREATED.replaceAll("#", Strings.join(Arrays.copyOfRange(args, 1, args.length), " "))));
												}
										} else player.addChatComponentMessage(new TextComponentString(Local.TEAM_CREATE_MISSING_NAME));
								} else if (args[0].equalsIgnoreCase("join")) {
										if (args.length <= 1) {
												Team team = TeamManager.getTeamFromName(Strings.join(Arrays.copyOfRange(args, 1, args.length), " "));
												if (team != null) {
														if (team.canJoin(player.getGameProfile().getId())) {
																team.addMember(player.getGameProfile().getId()); DataHelper.saveTeam(team);
																DataHelper.setTeam(player.getGameProfile().getId(), team);
																player.addChatComponentMessage(new TextComponentString(Local.TEAM_JOINED.replaceAll("#", team.getName())));
														}
												} else
														player.addChatComponentMessage(new TextComponentString(Local.TEAM_INVALID.replaceAll("#", Strings.join(Arrays.copyOfRange(args, 1, args.length), " "))));
										}
								} else if (args[0].equalsIgnoreCase("leave")) {
										Team team = DataHelper.getPlayerData(player.getGameProfile().getId()).getTeam(); if (team != null) {
												player.addChatComponentMessage(new TextComponentString(Local.TEAM_LEFT.replaceAll("#", team.getName())));
												DataHelper.setTeam(player.getGameProfile().getId(), null);
												team.removeMember(player.getGameProfile().getId());
										}
								} else if (args[0].equalsIgnoreCase("invite")) {
										Team team = DataHelper.getPlayerData(player.getGameProfile().getId()).getTeam();
										if (team != null && team.getLeader().equals(player.getGameProfile().getId())) {
												if (args.length == 2) {
														PlayerList players = server.getServer().getPlayerList();
														if (players.getPlayerList().size() > 0) for (EntityPlayerMP user : players.getPlayerList())
																if (user.getGameProfile().getId().equals(server.getServer().getPlayerProfileCache().getGameProfileForUsername(args[1]).getId())) {
																		player.addChatComponentMessage(new TextComponentString(Local.TEAM_INVITED.replaceAll("#", user.getDisplayName().getUnformattedText())));
																		user.addChatComponentMessage(new TextComponentString(Local.TEAM_INVITED_OTHER.replaceAll("#", team.getName())));
																		team.addPossibleMember(user.getGameProfile().getId()); DataHelper.saveTeam(team);
																}
												} else player.addChatComponentMessage(new TextComponentString(Local.TEAM_MISSING_NAME));
										} else if (team != null) player.addChatComponentMessage(new TextComponentString(Local.TEAM_LEADER_PERM));
								}
						} else player.addChatComponentMessage(new TextComponentString(getCommandUsage(sender)));
				}
		}
}
