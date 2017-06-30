package wurmcraft.serveressentials.common.commands.utils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.*;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.CommandUtils;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.RankManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SECommand extends CommandBase {

	protected Perm perm;

	public SECommand (Perm perm) {
		this.perm = perm;
	}

	@Override
	public String getCommandName () {
		return null;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return null;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (!canConsoleRun () && sender.getCommandSenderEntity () == null) {
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
			return;
		}
		if (hasSubCommand () && args.length > 0) {
			Method[] methods = getClass ().getMethods ();
			for (Method method : methods)
				if (method.getAnnotation (SubCommand.class) != null && method.getName ().equalsIgnoreCase (args[0])) {
					try {
						method.invoke (this,sender,CommandUtils.getArgsAfterCommand (1,args));
					} catch (Exception e) {
					}
				}
		} else if (!hasSubCommand ()) {
			// Run Non Sub Command
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getCommandAliases () {
		if (getAliases ().length > 0) {
			String[] aliases = getAliases ();
			List <String> allAliases = new ArrayList <> ();
			String command = getCommandName ();
			// TODO Generate List of all Aliases
			Collections.addAll (allAliases,aliases);
			return allAliases;
		}
		return super.getCommandAliases ();
	}

	public String[] getAliases () {
		return new String[0];
	}

	public boolean canConsoleRun () {
		return true;
	}

	public boolean hasSubCommand () {
		return false;
	}

	public boolean requiresTrusted () {
		return false;
	}

	@Override
	public boolean checkPermission (MinecraftServer server,ICommandSender sender) {
		if (Settings.securityModule && requiresTrusted ()) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer)
				return SecurityUtils.isTrustedMember ((EntityPlayer) sender.getCommandSenderEntity ()) && RankManager.hasPermission (DataHelper.getPlayerData (((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ()).getRank (),perm.toString ());
		} else if (sender.getCommandSenderEntity () instanceof EntityPlayer)
			return RankManager.hasPermission (DataHelper.getPlayerData (((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ()).getRank (),perm.toString ());
		else
			return canConsoleRun ();
		return false;
	}

	public String getDescription () {
		return "";
	}

	public static List <String> autoCompleteHomes (String[] args,Home[] homes) {
		List <String> homeList = new ArrayList <> ();
		if (args.length == 0) {
			for (Home home : homes)
				if (home != null)
					homeList.add (home.getName ());
			return homeList;
		} else if (args.length == 1) {
			for (Home home : homes)
				if (home != null && home.getName ().toLowerCase ().startsWith (args[0]))
					homeList.add (home.getName ());
			return homeList;
		}
		return null;
	}

	public static List <String> autoCompleteWarps (String[] args,Warp[] warps) {
		List <String> warpList = new ArrayList <> ();
		if (args.length == 0) {
			for (Warp warp : warps)
				if (warp != null)
					warpList.add (warp.getName ());
			return warpList;
		} else if (args.length == 1) {
			for (Warp warp : warps)
				if (warp != null && warp.getName ().toLowerCase ().startsWith (args[0]))
					warpList.add (warp.getName ());
			return warpList;
		}
		return null;
	}

	public static List <String> autoCompleteVaults (String[] args,Vault[] vaults) {
		if (vaults.length > 0) {
			List <String> vaultList = new ArrayList <> ();
			if (args.length == 0) {
				for (Vault vault : vaults)
					if (vault != null)
						vaultList.add (vault.getName ());
				return vaultList;
			} else if (args.length == 1) {
				for (Vault vault : vaults)
					if (vault != null && vault.getName ().toLowerCase ().startsWith (args[0]))
						vaultList.add (vault.getName ());
				return vaultList;
			}
		}
		return null;
	}

	public static List <String> autoCompleteKits (String[] args,List <Kit> kits,int index) {
		if (kits.size () > 0) {
			List <String> kitList = new ArrayList <> ();
			if (args.length == (((index - 1) >= 0) ? (index - 1) : 0)) {
				for (Kit kit : kits)
					if (kit != null)
						kitList.add (kit.getName ());
				return kitList;
			} else if (args.length == 1) {
				for (Kit kit : kits)
					if (kit != null && kit.getName ().toLowerCase ().startsWith (args[index]))
						kitList.add (kit.getName ());
				return kitList;
			}
		}
		return null;
	}

	public static List <String> autoCompleteChannel (String[] args,List <Channel> channels) {
		if (channels.size () > 0) {
			List <String> channelList = new ArrayList <> ();
			if (args.length == 0) {
				for (Channel channel : channels)
					if (channel != null)
						channelList.add (channel.getName ());
				return channelList;
			} else if (args.length == 1) {
				for (Channel channel : channels)
					if (channel != null && channel.getName ().toLowerCase ().startsWith (args[0]))
						channelList.add (channel.getName ());
				return channelList;
			}
		}
		return null;
	}

	public static List <String> autoCompleteTeam (String[] args,List <Team> teams) {
		if (teams.size () > 0) {
			List <String> teamList = new ArrayList <> ();
			if (args.length == 1) {
				for (Team team : teams)
					if (team != null)
						teamList.add (team.getName ());
				return teamList;
			} else if (args.length == 2) {
				for (Team team : teams)
					if (team != null && team.getName ().toLowerCase ().startsWith (args[1]))
						teamList.add (team.getName ());
				return teamList;
			}
		}
		return null;
	}

	public static List <String> autoCompleteUsername (String[] args,int playerIndex) {
		List <String> usernames = new ArrayList <> ();
		if (args.length >= playerIndex && args[playerIndex] != null) {
			for (EntityPlayerMP player : FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().getPlayerList ()) {
				if (player.getDisplayNameString ().toLowerCase ().startsWith (args[playerIndex].toLowerCase ()))
					usernames.add (player.getDisplayNameString ());
				else {
					PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
					if (data.getNickname () != null && !data.getNickname ().equals ("") && args[playerIndex].toLowerCase ().startsWith (data.getNickname ().toLowerCase ()))
						usernames.add (data.getNickname ());
				}
			}
			return usernames;
		} else {
			for (EntityPlayerMP player : FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().getPlayerList ()) {
				PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
				String name = data.getNickname () != null ? data.getNickname () : player.getDisplayNameString ();
				usernames.add (name);
			}
			return usernames;
		}
	}

	protected boolean hasPerm (ICommandSender sender,String thePerm) {
		if (Settings.securityModule && requiresTrusted ()) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer)
				return SecurityUtils.isTrustedMember ((EntityPlayer) sender.getCommandSenderEntity ()) && RankManager.hasPermission (DataHelper.getPlayerData (((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ()).getRank (),thePerm);
		} else if (sender.getCommandSenderEntity () instanceof EntityPlayer)
			return RankManager.hasPermission (DataHelper.getPlayerData (((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ()).getRank (),thePerm);
		else
			return canConsoleRun ();
		return false;
	}
}
