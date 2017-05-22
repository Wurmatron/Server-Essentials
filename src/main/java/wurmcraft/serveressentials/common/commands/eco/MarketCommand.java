package wurmcraft.serveressentials.common.commands.eco;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.commands.utils.MarketInventory;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class MarketCommand extends EssentialsCommand {

	public static final int MAX_PER_PAGE = 45;

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
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Market");
		aliases.add ("MARKET");
		aliases.add ("M");
		aliases.add ("m");
		aliases.add ("Shop");
		aliases.add ("shop");
		aliases.add ("SHOP");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
		player.displayGUIChest (new MarketInventory (player,player.getGameProfile ().getId (),0,DataHelper.loadMarket (player.getGameProfile ().getId ())));
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Opens the market";
	}
}
