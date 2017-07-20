package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.event.PlayerRespawnEvent;
import wurmcraft.serveressentials.common.reference.Perm;

public class SuicideCommand extends SECommand {

	public SuicideCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "suicide";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/suicide";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerRespawnEvent.add (player.getGameProfile ().getId (),new ItemStack[][] {player.inventory.mainInventory.toArray (new ItemStack[0]),player.inventory.armorInventory.toArray (new ItemStack[0]),player.inventory.offHandInventory.toArray (new ItemStack[0])});
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
