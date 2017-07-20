package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.List;

public class DelWarp extends SECommand {

	public DelWarp (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "delWarp";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/delwarp <name>";
	}

//	@Override
//	public String[] getAliases () {
//		return new String[] {"deleteWarp", "removeWarp", "remWarp"};
//	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			if (DataHelper.getWarp (args[0]) != null) {
				Warp warp = DataHelper.getWarp (args[0]);
				DataHelper.deleteWarp (DataHelper.getWarp (args[0]));
				ChatHelper.sendMessageTo (sender,Local.WARP_DELETE.replaceAll ("#",warp.getName ()));
			} else
				ChatHelper.sendMessageTo (sender,Local.WARP_NAME);
		} else
			ChatHelper.sendMessageTo (sender,Local.WARPS_NONE);
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteWarps (args,DataHelper.getWarps ());
	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Removes a Warp";
	}
}
