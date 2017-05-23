package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class NickCommand extends EssentialsCommand {

	public NickCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "nick";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/nick <nick> | /nick <name> <nick>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Nick");
		aliases.add ("NICK");
		return aliases;
	}

	@Override
	public String getDescription () {
		return "Changes a players name";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			DataHelper.setNickname (player.getGameProfile ().getId (),args[0]);
			ChatHelper.sendMessageTo (player,Local.NICKNAME.replaceAll ("#",args[0]));
		} else if (args.length == 2) {
			for (EntityPlayer player : server.getPlayerList ().getPlayerList ())
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
					DataHelper.setNickname (player.getGameProfile ().getId (),args[1]);
					ChatHelper.sendMessageTo (player,Local.NICKNAME.replaceAll ("#",args[1]));
					ChatHelper.sendMessageTo (sender,Local.NICKNAME_OTHER.replaceAll ("#",args[0]).replaceAll ("&",args[1]));
				}
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}
}
