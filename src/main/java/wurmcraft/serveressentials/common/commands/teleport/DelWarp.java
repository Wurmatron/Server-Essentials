package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DelWarp extends EssentialsCommand {

	public DelWarp (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "delWarp";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/delwarp <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		ArrayList <String> aliases = new ArrayList <> ();
		aliases.add ("deleteWarp");
		aliases.add ("DeleteWarp");
		aliases.add ("DelWarp");
		aliases.add ("Delwarp");
		aliases.add ("DELETEWARP");
		aliases.add ("DELWARP");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			if (DataHelper.getWarp (args[0]) != null) {
				Warp warp = DataHelper.getWarp (args[0]);
				DataHelper.deleteWarp (DataHelper.getWarp (args[0]));
				ChatHelper.sendMessageTo (sender,Local.WARP_DELETE.replaceAll ("#",warp.getName ()));
			}
		} else
			ChatHelper.sendMessageTo (sender,Local.WARPS_NONE);
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			for (Warp warp : DataHelper.getWarps ())
				if (warp != null && warp.getName ().length () > 0)
					list.add (warp.getName ());
		return list;
	}
}
