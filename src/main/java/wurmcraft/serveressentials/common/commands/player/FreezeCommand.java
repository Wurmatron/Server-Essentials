package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.event.PlayerTickEvent;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FreezeCommand extends SECommand {

	public FreezeCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "freeze";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"fz"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/freeze <username>";
	}

	@Override
	public List <String> getAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("f");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (player != null) {
				PlayerTickEvent.toggleFrozen (player,player.getPosition ());
				if (PlayerTickEvent.isFrozen (player)) {
					ChatHelper.sendMessageTo (sender,Local.FROZEN_OTHER.replaceAll ("#",player.getDisplayNameString ()));
					ChatHelper.sendMessageTo (player,Local.FROZEN);
				} else {
					ChatHelper.sendMessageTo (sender,Local.UNFROZEN_OTHER.replaceAll ("#",player.getDisplayNameString ()));
					ChatHelper.sendMessageTo (player,Local.UNFROZEN);
				}
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Disables a player's movement";
	}
}
