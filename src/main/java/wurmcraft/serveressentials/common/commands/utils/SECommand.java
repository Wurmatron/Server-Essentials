package wurmcraft.serveressentials.common.commands.utils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.CommandUtils;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class SECommand extends CommandBase {

	protected Perm perm;

	public SECommand (Perm perm) {
		this.perm = perm;
	}

	public static List <String> autoComplete (String[] args,List <IDataType> data) {
		List <String> dataList = new ArrayList <> ();
		if (args.length == 0) {
			for (IDataType d : data)
				if (d != null)
					dataList.add (d.getID ());
			return dataList;
		} else if (args.length == 1) {
			for (IDataType d : data)
				if (d != null && d.getID ().toLowerCase ().startsWith (args[0]))
					dataList.add (d.getID ());
			return dataList;
		}
		return null;
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

	public static List <String> autoComplete (String[] args,List <IDataType> data,int index) {
		List <String> dataList = new ArrayList <> ();
		if (args.length >= index) {
			for (IDataType d : data)
				if (d != null && d.getID ().toLowerCase ().startsWith (args[index]))
					dataList.add (d.getID ());
			return dataList;
		}
		return null;
	}

	//		@Override
	//	public List <String> getCommandAliases () {
	//		if (getAliases ().size () > 0) {
	//			String[] aliases = getAliases ();
	//			List <String> allAliases = new ArrayList <> ();
	//			String command = getName ();
	//			// TODO Generate List of all Aliases
	//			Collections.addAll (allAliases,CommandUtils.permute (command));
	//			for(String str : aliases) {
	//				Collections.addAll (allAliases,CommandUtils.permute (str));
	//			}
	////			LogHandler.info ("C: " + allAliases.toString ());
	//			return allAliases;
	//		}
	//		return super.getAliases ();
	//	}

	public static List <String> autoCompleteUsername (String[] args,int index) {
		List <String> possibleUsernames = getOnlinePlayerNames ();
		if (args.length > index && args[index] != null)
			return predictName (args[index],possibleUsernames);
		else
			return possibleUsernames;
	}

	public static List <String> predictName (String current,List <String> possibleNames) {
		List <String> predictedNames = new ArrayList <> ();
		for(String name : possibleNames)
			if(name.toLowerCase ().startsWith (current.toLowerCase ()))
				predictedNames.add (name);
			else if(name.toLowerCase ().endsWith (current.toLowerCase ()))
				predictedNames.add (name);
		return predictedNames;
	}

	private static List <String> getOnlinePlayerNames () {
		return Arrays.asList (FMLCommonHandler.instance ().getMinecraftServerInstance ().getOnlinePlayerNames ());
	}

	@Override
	public String getName () {
		return null;
	}

	@Override
	public String getUsage (ICommandSender sender) {
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
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	public List <String> getAliases () {
		if (getCommandAliases ().length > 0) {
			List <String> aliases = new ArrayList ();
			Collections.addAll (aliases,getCommandAliases ());
			return aliases;
		}
		return new ArrayList <> ();
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

	public String[] getCommandAliases () {
		return new String[0];
	}

	public String getDescription () {
		return "";
	}

	@Override
	public boolean checkPermission (MinecraftServer server,ICommandSender sender) {
		if (ConfigHandler.securityModule && requiresTrusted ()) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer)
				return SecurityUtils.isTrustedMember ((EntityPlayer) sender.getCommandSenderEntity ()) && RankManager.hasPermission (((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ().toString ())).getRank (),perm.toString ());
		} else if (sender.getCommandSenderEntity () instanceof EntityPlayer)
			return RankManager.hasPermission (((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ().toString ())).getRank (),perm.toString ());
		else
			return canConsoleRun ();
		return false;
	}

	protected boolean hasPerm (ICommandSender sender,String thePerm) {
		if (ConfigHandler.securityModule && requiresTrusted ()) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer)
				return SecurityUtils.isTrustedMember ((EntityPlayer) sender.getCommandSenderEntity ()) && RankManager.hasPermission (((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ().toString ())).getRank (),thePerm);
		} else if (sender.getCommandSenderEntity () instanceof EntityPlayer)
			return RankManager.hasPermission (((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ().toString ())).getRank (),thePerm);
		else
			return canConsoleRun ();
		return false;
	}
}
