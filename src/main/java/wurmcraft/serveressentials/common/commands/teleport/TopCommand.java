package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ChatManager;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import java.util.ArrayList;
import java.util.List;

public class TopCommand extends EssentialsCommand {

	public TopCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "top";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/top";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Top");
		aliases.add ("TOP");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			for (int y = 256; y >= player.posY; y--) {
				if (player.worldObj.getBlockState (new BlockPos (player.posX,y,player.posZ)).getBlock () != Blocks.AIR) {
					TeleportUtils.teleportTo (player,new BlockPos (player.posX,y + 2,player.posZ),false);
					ChatManager.sendMessage (player,Local.TOP);
					return;
				}
			}
		} else
			sender.addChatMessage (new TextComponentString ("Command can only be run by players!"));
	}
}
