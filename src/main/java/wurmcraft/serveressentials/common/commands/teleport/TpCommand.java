package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import javax.annotation.Nullable;
import java.util.List;

// TODO Username lookup
public class TpCommand extends EssentialsCommand {

	public TpCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "tp";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/tp <username> | /tp <from> <to> | /tp <x> <y> <z> | /tp <username> <x> <y> <z>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			PlayerList players = server.getPlayerList ();
			if (players.getCurrentPlayerCount () > 0) {
				for (EntityPlayer p : players.getPlayerList ())
					if (UsernameCache.getLastKnownUsername (p.getGameProfile ().getId ()) != null && UsernameCache.getLastKnownUsername (p.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
						TeleportUtils.teleportTo (player,new BlockPos (p.posX,p.posY,p.posZ),false);
						ChatHelper.sendMessageTo (player,Local.TELEPORTED);
					}
			}
		} else if (args.length == 2) {
			EntityPlayer from = TeleportUtils.getPlayerFromUsername (server,args[0]);
			EntityPlayer to = TeleportUtils.getPlayerFromUsername (server,args[1]);
			if (from != null && to != null) {
				TeleportUtils.teleportTo (from,new BlockPos (to.posX,to.posY,to.posZ),false);
				ChatHelper.sendMessageTo (sender,Local.TELEPORTED_FROM.replaceAll ("#",from.getDisplayNameString ()).replaceAll ("%",to.getDisplayNameString ()));
				ChatHelper.sendMessageTo (from,Local.TELEPORT_TO.replaceAll ("#",to.getDisplayNameString ()));
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND);
		} else if (args.length == 3 && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			try {
				int x = Integer.parseInt (args[0]);
				int y = Integer.parseInt (args[1]);
				int z = Integer.parseInt (args[2]);
				if (x != -1 && y != -1 & z != -1) {
					TeleportUtils.teleportTo (player,new BlockPos (x,y,z),false);
					ChatHelper.sendMessageTo (player,Local.TELEPORTED);
				}
			} catch (NumberFormatException e) {
			}
		} else if (args.length == 4) {
			EntityPlayer player = TeleportUtils.getPlayerFromUsername (server,args[0]);
			if (player != null) {
				try {
					int x = Integer.parseInt (args[0]);
					int y = Integer.parseInt (args[1]);
					int z = Integer.parseInt (args[2]);
					if (x != -1 && y != -1 & z != -1) {
						TeleportUtils.teleportTo (player,new BlockPos (x,y,z),false);
						ChatHelper.sendMessageTo (player,Local.TELEPORTED);
					}
				} catch (NumberFormatException e) {
				}
			}
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> args0 = autoCompleteUsername (args,0);
		List <String> args1 = autoCompleteUsername (args,1);
		return null;
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Teleport to another player";
	}
}
