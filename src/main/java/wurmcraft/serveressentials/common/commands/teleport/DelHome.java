package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class DelHome extends SECommand {

	public DelHome (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "deleteHome";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"delHome","removeHome","remHome"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/delhome <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
		if (data == null)
			DataHelper2.load (Keys.PLAYER_DATA,new PlayerData (player.getGameProfile ().getId (),null));
		if (args.length == 0) {
			data.delHome (ConfigHandler.homeName);
			DataHelper2.forceSave (Keys.PLAYER_DATA,data);
			ChatHelper.sendMessageTo (sender,Local.HOME_DELETED.replaceAll ("#",ConfigHandler.homeName));
		} else if (args.length == 1) {
			data.delHome (args[0]);
			DataHelper2.forceSave (Keys.PLAYER_DATA,data);
			ChatHelper.sendMessageTo (sender,Local.HOME_DELETED.replaceAll ("#",args[0]));
		}
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
				if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
					return autoCompleteHomes (args,UsernameResolver.getPlayerData (player.getGameProfile ().getId ()).getHomes ());
				}
		return null;
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Delete a home";
	}
}
