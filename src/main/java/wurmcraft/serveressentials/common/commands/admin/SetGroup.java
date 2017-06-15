package wurmcraft.serveressentials.common.commands.admin;


import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.permissions.Rank;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;
import wurmcraft.serveressentials.common.utils.RankManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SetGroup extends EssentialsCommand {

	public SetGroup (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "setGroup";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/setGroup <username> <group>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length > 1) {
			List <EntityPlayerMP> onlinePlayers = server.getPlayerList ().getPlayerList ();
			boolean found = false;
			for (EntityPlayerMP player : onlinePlayers)
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equals (args[0]))
					if (RankManager.getRankFromName (args[1]) != null) {
						found = true;
						DataHelper.setRank (player.getGameProfile ().getId (),RankManager.getRankFromName (args[1]));
						PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
						String name = data.getNickname () != null ? "*" + TextFormatting.RESET + data.getNickname ().replaceAll ("&","\u00A7") : player.getDisplayNameString ();
						ChatHelper.sendMessageTo (sender,Local.RANK_CHANGED.replaceAll ("Your",name).replaceAll ("#",RankManager.getRankFromName (args[1]).getName ()));
					}
			if (!found)
				for (UUID id : UsernameCache.getMap ().keySet ())
					if (UsernameCache.getMap ().get (id).equalsIgnoreCase (args[0])) {
						if (RankManager.getRankFromName (args[1]) != null) {
							found = true;
							DataHelper.setRank (id,RankManager.getRankFromName (args[1]));
							PlayerData data = DataHelper.getPlayerData (id);
							String name = data.getNickname () != null ? "*" + TextFormatting.RESET + data.getNickname ().replaceAll ("&","\u00A7") : UsernameCache.getLastKnownUsername (id);
							ChatHelper.sendMessageTo (sender,Local.RANK_CHANGED.replaceAll ("Your",name).replaceAll ("#",RankManager.getRankFromName (args[1]).getName ()));
						}
					}
			if (!found)
				ChatHelper.sendMessageTo (sender,Local.RANK_NOT_FOUND.replaceAll ("#",args[1]));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public Boolean isPlayerOnly () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Set a player's group";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("setgroup");
		aliases.add ("SETGROUP");
		aliases.add ("Setgroup");
		aliases.add ("SetRank");
		aliases.add ("Setrank");
		aliases.add ("setrank");
		aliases.add ("setRank");
		aliases.add ("SETRANK");
		return aliases;
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		if (args.length == 1) {
			if (args[0] == null || args[0].length () == 0) {
				List <String> usernames = new ArrayList <> ();
				Collections.addAll (usernames,server.getAllUsernames ());
				return usernames;
			} else {
				List <String> usernames = new ArrayList <> ();
				for (String name : server.getAllUsernames ())
					if (name.startsWith (args[0]))
						usernames.add (name);
			}
		}
		return null;
	}

	@Override
	public boolean checkPermission (MinecraftServer server,ICommandSender sender) {
		if (Settings.securityModule) {
			LogHandler.info ("SM");
			if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
				LogHandler.info ("T: " + super.checkPermission (server,sender));
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				LogHandler.info ("S: " + SecurityUtils.isTrustedMember (player));
				return super.checkPermission (server,sender) && SecurityUtils.isTrustedMember (player);
			} else
				return false;
		} else
			return super.checkPermission (server,sender);
	}
}
