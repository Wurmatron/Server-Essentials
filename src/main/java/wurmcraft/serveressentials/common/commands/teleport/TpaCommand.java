package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.test.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

import static wurmcraft.serveressentials.common.utils.DataHelper.activeRequests;

public class TpaCommand extends SECommand {

	public TpaCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "tpa";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/tpa <user>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase (player.getDisplayNameString ())) {
				ChatHelper.sendMessageTo (player,Local.TPA_SELF);
				return;
			}
			PlayerList players = server.getServer ().getPlayerList ();
			if (players.getPlayerList ().size () > 0) {
				boolean found = false;
				EntityPlayer otherPlayer = UsernameResolver.getPlayer (args[0]);
				if (otherPlayer != null) {
					found = true;
					if (!activeRequests.values ().contains (new EntityPlayer[] {player,otherPlayer})) {
						activeRequests.put (System.currentTimeMillis (),new EntityPlayer[] {player,otherPlayer});
						ChatHelper.sendMessageTo (player,Local.TPA_REQUEST_SENT.replaceAll ("#",otherPlayer.getDisplayName ().getUnformattedText ()));
						ChatHelper.sendMessageTo (otherPlayer,Local.TPA_REQUEST.replaceAll ("#",player.getDisplayName ().getUnformattedText ()));
					}
				}
				if (!found)
					ChatHelper.sendMessageTo (sender,Local.TPA_USER_NOTFOUND);
			}
		} else
			ChatHelper.sendMessageTo (sender,Local.TPA_USERNAME_NONE);
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Request to teleport to another player";
	}
}
