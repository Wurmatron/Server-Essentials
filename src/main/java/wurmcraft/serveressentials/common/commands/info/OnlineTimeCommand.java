package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OnlineTimeCommand extends EssentialsCommand {

	public OnlineTimeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "onlineTime";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/onlineTime | /onlineTime <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 0) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				ChatHelper.sendMessageTo (player,TeleportUtils.convertToHumanReadable (DataHelper.getPlayerData (player.getGameProfile ().getId ()).getOnlineTime () * 60000));
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
		} else if (args.length == 1) {
			for (EntityPlayerMP player : server.getPlayerList ().getPlayerList ())
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0]))
					ChatHelper.sendMessageTo (sender,TeleportUtils.convertToHumanReadable (DataHelper.getPlayerData (player.getGameProfile ().getId ()).getOnlineTime () * 60000));
		} else
			ChatHelper.sendMessageTo (sender ,getCommandUsage (sender));
	}

	@Override
	public Boolean isPlayerOnly () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Find out how long you have played on the server.";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("onlinetime");
		aliases.add ("OnlineTime");
		aliases.add ("ONLINETIME");
		aliases.add ("OT");
		aliases.add ("ot");
		return aliases;
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> usernames = new ArrayList <> ();
		Collections.addAll (usernames,server.getAllUsernames ());
		return usernames;
	}
}
