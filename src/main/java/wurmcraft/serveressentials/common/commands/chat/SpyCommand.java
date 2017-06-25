package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// TODO Username lookup
public class SpyCommand extends EssentialsCommand {

	public SpyCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "spy";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/spy | /spy <name>";
	}

	@Override
	public String getDescription () {
		return "Enables and Disables Spy";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Spy");
		aliases.add ("SPY");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 0 && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			DataHelper.setSpy (player.getGameProfile ().getId (),!DataHelper.getPlayerData (player.getGameProfile ().getId ()).isSpy ());
			ChatHelper.sendMessageTo (player,Local.SPY.replaceAll ("#","" + DataHelper.getPlayerData (player.getGameProfile ().getId ()).isSpy ()));
		} else if (args.length == 1) {
			boolean found = false;
			for (EntityPlayer player : server.getPlayerList ().getPlayerList ())
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
					found = true;
					DataHelper.setSpy (player.getGameProfile ().getId (),!DataHelper.getPlayerData (player.getGameProfile ().getId ()).isSpy ());
					ChatHelper.sendMessageTo (player,Local.SPY.replaceAll ("#","" + DataHelper.getPlayerData (player.getGameProfile ().getId ()).isSpy ()));
					ChatHelper.sendMessageTo (sender,Local.SPY_OTHER.replaceAll ("#","" + DataHelper.getPlayerData (player.getGameProfile ().getId ()).isSpy ()).replaceAll ("&",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())));
				}
			if (!found)
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
