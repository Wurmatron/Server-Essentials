package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ReloadPlayerDataCommand extends EssentialsCommand {

	public ReloadPlayerDataCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "reloadPlayerData";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/reloadPlayerData <username>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("reloadplayerdata");
		aliases.add ("Reloadplayerdata");
		aliases.add ("ReloadPlayerdata");
		aliases.add ("ReloadPlayerData");
		aliases.add ("RELOADPLAYERDATA");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && args[0] != null) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (player != null) {
				DataHelper.unloadPlayerData (player.getGameProfile ().getId ());
				DataHelper.loadPlayerData (player.getGameProfile ().getId ());
				ChatHelper.sendMessageTo (player,Local.DATA_RELOADED);
				ChatHelper.sendMessageTo (sender,Local.DATA_RELOADED_OTHER.replaceAll ("#",player.getDisplayNameString ()));

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
		return "Reloads a certain player's PlayerData";
	}
}
