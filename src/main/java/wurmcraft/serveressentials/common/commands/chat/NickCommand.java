package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class NickCommand extends SECommand {

	public NickCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "nick";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/nick <username> <name> /nick <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
			if (player != null && data.getNickname () != null) {
				ChatHelper.sendMessageTo (sender,(data.getNickname ().replaceAll ("&","\u00A7")));
			} else if (player != null && data.getNickname () == null)
				ChatHelper.sendMessageTo (sender,Local.NICK_NONE.replaceAll ("#",player.getDisplayNameString ()));
			else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else if (args.length == 2) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
			if (player != null) {
				if (!args[1].equals (player.getDisplayNameString ())) {
					data.setNickname (args[1] + "&r");
					DataHelper2.forceSave (Keys.PLAYER_DATA,data);
				} else
					data.setNickname (null);
				DataHelper2.forceSave (Keys.PLAYER_DATA,data);
				ChatHelper.sendMessageTo (player,Local.NICKNAME_SET.replaceAll ("#",args[1].replaceAll ("&","\u00A7")));
				if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
					EntityPlayer senderPlayer = (EntityPlayer) sender.getCommandSenderEntity ();
					if (!senderPlayer.getGameProfile ().getId ().equals (player.getGameProfile ().getId ()))
						ChatHelper.sendMessageTo (senderPlayer,Local.NICKNAME_OTHER.replaceAll ("#",args[1].replaceAll ("&","\u00A7")));
				}
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	//	@Override
	//	public String[] getAliases () {
	//		return new String[] {"n"};
	//	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Changes a player's display name";
	}
}
