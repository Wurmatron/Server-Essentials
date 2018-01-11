package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

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
		//		super.execute (server,sender,args);
		//		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		//		PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
		//		if (data == null)
		//			DataHelper.reloadPlayerData (player.getGameProfile ().getId ());
		//		if (args.length == 0)
		//			ChatHelper.sendMessageTo (sender,DataHelper.deleteHome (player.getGameProfile ().getId (),Settings.home_name));
		//		else if (args.length == 1)
		//			ChatHelper.sendMessageTo (sender,DataHelper.deleteHome (player.getGameProfile ().getId (),args[0]));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		//		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
		//			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		//			return autoCompleteHomes (args,DataHelper.getPlayerData (player.getGameProfile ().getId ()).getHomes ());
		//		}
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
