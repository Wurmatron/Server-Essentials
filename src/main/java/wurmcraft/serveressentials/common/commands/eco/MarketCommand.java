package wurmcraft.serveressentials.common.commands.eco;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.commands.utils.MarketInventory;
import wurmcraft.serveressentials.common.reference.Local;

import java.util.List;

public class MarketCommand extends EssentialsCommand {

	public MarketCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "market";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/market <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if(sender.getCommandSenderEntity () instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
			player.displayGUIChest (new MarketInventory (player));
		} else
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
	}

	@Override
	public List<String> getCommandAliases () {
		return super.getCommandAliases ();
	}
}
