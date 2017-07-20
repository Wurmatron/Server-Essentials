package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeleteMotdCommand extends SECommand {

	public DeleteMotdCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "deleteMotd";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/delMotd <motd No.>";
	}

//	@Override
//	public String[] getAliases () {
//		return new String[] {"delMotd", "removeMotd", "remMotd"};
//	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			Integer motdIndex = Integer.parseInt (args[0]);
			if (motdIndex >= 0 && DataHelper.globalSettings.getMotd ().length > motdIndex) {
				DataHelper.globalSettings.removeMotd (motdIndex);
				ChatHelper.sendMessageTo (sender,Local.MOTD_REMOVED.replaceAll ("#",args[0]));
			} else
				ChatHelper.sendMessageTo (sender,Local.MOTD_INVALID_INDEX.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			for (int i = 0; i > DataHelper.globalSettings.getMotd ().length; i++)
				list.add (Integer.toString (i));
		return list;
	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Deletes a Line from the MOTD";
	}
}
