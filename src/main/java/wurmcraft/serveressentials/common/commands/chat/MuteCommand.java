package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MuteCommand extends EssentialsCommand {

	public MuteCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "mute";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/mute <username>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Mute");
		aliases.add ("mu");
		aliases.add ("MUTE");
		aliases.add ("MU");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (player != null) {
				DataHelper.setMute (player.getGameProfile ().getId (),!DataHelper.isMuted (player.getGameProfile ().getId ()));
				if (!DataHelper.isMuted (player.getGameProfile ().getId ())) {
					ChatHelper.sendMessageTo (player,Local.UNMUTED);
					ChatHelper.sendMessageTo (sender,Local.UNMUTED_OTHER.replaceAll ("#",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())));
				} else {
					ChatHelper.sendMessageTo (player,Local.MUTED);
					ChatHelper.sendMessageTo (sender,Local.MUTED_OTHER.replaceAll ("#",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())));
				}
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));

	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Disables someone from talking in chat";
	}
}
