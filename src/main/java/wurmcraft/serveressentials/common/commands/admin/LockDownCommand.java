package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class LockDownCommand extends SECommand {

	public LockDownCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "lockdown";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase ("true")) {
				DataHelper.globalSettings.setLockDown (true);
				FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendMessage (new TextComponentString (Local.LOCKDOWN_ENABLED));
			} else if (args[0].equalsIgnoreCase ("false")) {
				DataHelper.globalSettings.setLockDown (false);
				FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendMessage (new TextComponentString (Local.LOCKDOWN_DISABLED));
			}
		} else if (args.length == 0) {
			ChatHelper.sendMessageTo (sender,Local.LOCKDOWN.replaceAll ("#",DataHelper.globalSettings.getLockDown () ? "Enabled" : "Disabled"));
		} else {
			ChatHelper.sendMessageTo (sender,getUsage (sender));
		}
	}
}
