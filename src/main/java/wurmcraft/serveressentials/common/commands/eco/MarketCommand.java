package wurmcraft.serveressentials.common.commands.eco;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.MarketInventory;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class MarketCommand extends SECommand {

	public static final int MAX_PER_PAGE = 45;

	public MarketCommand (Perm perm) {
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
	public String[] getAliases () {
		return new String[] {"shop","shops","m"};
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
		player.displayGUIChest (new MarketInventory (player,player.getGameProfile ().getId (),0,DataHelper.loadMarket (player.getGameProfile ().getId ())));
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Opens the market";
	}
}
