package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ChatManager;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeleteMotdCommand extends EssentialsCommand {

	public DeleteMotdCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "deleteMotd";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/delMotd <motd No.>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("DeleteMotd");
		aliases.add ("DeleteMOTD");
		aliases.add ("deletemotd");
		aliases.add ("DELETEMOTD");
		aliases.add ("delmotd");
		aliases.add ("Delmotd");
		aliases.add ("DelMotd");
		aliases.add ("DELMOTD");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			Integer motdIndex = Integer.parseInt (args[0]);
			if (motdIndex >= 0) {
				DataHelper.globalSettings.removeMotd (motdIndex);
				ChatManager.sendMessage (sender,Local.MOTD_REMOVED.replaceAll ("#",args[0]));
			} else
				ChatManager.sendMessage (sender,Local.MOTD_INVALID_INDEX.replaceAll ("#",args[0]));
		} else
			ChatManager.sendMessage (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			for (int i = 0; i > DataHelper.globalSettings.getMotd ().length; i++)
				list.add (Integer.toString (i)); return list;
	}
}
