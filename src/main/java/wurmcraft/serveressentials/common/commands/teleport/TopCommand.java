package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

public class TopCommand extends SECommand {

	public TopCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "top";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/top";
	}



	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		for (int y = 256; y >= player.posY; y--) {
			if (player.world.getBlockState (new BlockPos (player.posX,y,player.posZ)).getBlock () != Blocks.AIR) {
				TeleportUtils.teleportTo (player,new BlockPos (player.posX,y + 2,player.posZ),false);
				ChatHelper.sendMessageTo (player,Local.TOP);
				return;
			}
		}
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Teleports you to the highest location above your current position.";
	}
}
