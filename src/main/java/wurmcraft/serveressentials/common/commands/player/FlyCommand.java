package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class FlyCommand extends EssentialsCommand {

	public FlyCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "fly";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/fly <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		boolean playerFound = false;
		if (args.length > 0) {
			EntityPlayer user = UsernameResolver.getPlayer (args[0]);
			if (user != null) {
				playerFound = true;
				if (!user.capabilities.allowFlying) {
					user.capabilities.allowFlying = true;
					user.capabilities.isFlying = true;
					ChatHelper.sendMessageTo (user,Local.FLY_ENABLED);
					ChatHelper.sendMessageTo (player,Local.FLY_ENABLED_OTHER.replaceFirst ("#",user.getDisplayName ().getUnformattedText ()));
					user.sendPlayerAbilities ();
				} else {
					user.capabilities.allowFlying = false;
					ChatHelper.sendMessageTo (user,Local.FLY_DISABLED);
					ChatHelper.sendMessageTo (player,Local.FLY_DISABLED_OTHER.replaceFirst ("#",user.getDisplayName ().getUnformattedText ()));
					user.sendPlayerAbilities ();
				}
			}
			if (!playerFound)
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else {
			if (!player.capabilities.allowFlying) {
				player.capabilities.allowFlying = true;
				ChatHelper.sendMessageTo (player,Local.FLY_ENABLED);
				player.sendPlayerAbilities ();
			} else {
				player.capabilities.allowFlying = false;
				ChatHelper.sendMessageTo (player,Local.FLY_DISABLED);
				player.sendPlayerAbilities ();
			}
		}
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Allows a player to fly";
	}
}
