package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.test.SECommand;
import wurmcraft.serveressentials.common.event.PlayerRespawnEvent;
import wurmcraft.serveressentials.common.reference.Perm;

public class SuicideCommand extends SECommand {

	public SuicideCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "suicide";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/suicide";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerRespawnEvent.add (player.getGameProfile ().getId (),new ItemStack[][] {player.inventory.mainInventory.clone (),player.inventory.armorInventory.clone (),player.inventory.offHandInventory.clone ()});
		player.inventory.clear ();
		player.onKillCommand ();
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Kill yourself";
	}
}
