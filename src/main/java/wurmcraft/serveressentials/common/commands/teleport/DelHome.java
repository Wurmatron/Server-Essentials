package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DelHome extends EssentialsCommand {

	public DelHome (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "delHome";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/delhome <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		ArrayList <String> aliases = new ArrayList <> ();
		aliases.add ("deletehome");
		aliases.add ("DeleteHome");
		aliases.add ("deletehome");
		aliases.add ("DelHome");
		aliases.add ("delhome");
		aliases.add ("DELETEHOME");
		aliases.add ("DELHOME");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
		if (data == null)
			DataHelper.reloadPlayerData (player.getGameProfile ().getId ());
		if (args.length == 0)
			ChatHelper.sendMessageTo (sender,DataHelper.deleteHome (player.getGameProfile ().getId (),Settings.home_name));
		else if (args.length == 1)
			ChatHelper.sendMessageTo (sender,DataHelper.deleteHome (player.getGameProfile ().getId (),args[0]));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			return autoCompleteHomes(args,DataHelper.getPlayerData (player.getGameProfile ().getId ()).getHomes ());
		}
		return null;
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Delete a home";
	}
}
