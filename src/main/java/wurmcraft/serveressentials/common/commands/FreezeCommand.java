package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.event.PlayerTickEvent;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

public class FreezeCommand extends EssentialsCommand {

	public FreezeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "freeze";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/freeze <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer player = TeleportUtils.getPlayerFromUsername (server,args[0]);
			if (player != null) {
				PlayerTickEvent.toggleFrozen (player,player.getPosition ());
				if (PlayerTickEvent.isFrozen (player)) {
					sender.addChatMessage (new TextComponentString ("# has been frozen".replaceAll ("#",player.getDisplayNameString ())));
					player.addChatComponentMessage (new TextComponentString ("You have been frozen"));
				} else {
					sender.addChatMessage (new TextComponentString ("# has been unfrozen".replaceAll ("#",player.getDisplayNameString ())));
					player.addChatComponentMessage (new TextComponentString ("You have been unfrozen"));
				}
			}
		}
	}
}
