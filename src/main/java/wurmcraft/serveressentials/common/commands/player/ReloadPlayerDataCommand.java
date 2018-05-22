package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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

public class ReloadPlayerDataCommand extends SECommand {

	public ReloadPlayerDataCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "reloadPlayerData";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"relPlayerData","rpd"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/reloadPlayerData <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && args[0] != null) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (player != null) {
				DataHelper2.remove (Keys.PLAYER_DATA,(PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ()));
				DataHelper2.load (Keys.PLAYER_DATA,new PlayerData (player.getGameProfile ().getId (),null));
				ChatHelper.sendMessageTo (player,Local.DATA_RELOADED);
				ChatHelper.sendMessageTo (sender,Local.DATA_RELOADED_OTHER.replaceAll ("#",player.getDisplayNameString ()));

			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Reloads a certain player's PlayerData";
	}
}
