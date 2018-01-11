package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class SpyCommand extends SECommand {

	public SpyCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "spy";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/spy | /spy <name>";
	}

	@Override
	public String getDescription () {
		return "Enables and Disables Spy";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 0 && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
			data.setSpy (!data.isSpy ());
			DataHelper2.forceSave (Keys.PLAYER_DATA,data);
			ChatHelper.sendMessageTo (player,Local.SPY.replaceAll ("#","" + data.isSpy ()));
		} else if (args.length == 1) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
			if (player != null) {
				data.setSpy (!data.isSpy ());
				DataHelper2.forceSave (Keys.PLAYER_DATA,data);
				ChatHelper.sendMessageTo (player,Local.SPY.replaceAll ("#","" + data.isSpy ()));
				ChatHelper.sendMessageTo (sender,Local.SPY_OTHER.replaceAll ("#","" + data.isSpy ()).replaceAll ("&",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())));
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
