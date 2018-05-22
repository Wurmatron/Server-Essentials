package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChannelCommand extends SECommand {

	public ChannelCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "channel";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/channel <name> | /channel list";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"ch"};
	}


	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length == 1 && !args[0].equalsIgnoreCase ("list")) {
			Channel channel = ChannelManager.getFromName (args[0]);
			if (channel != null && hasPerm (player,"channel." + channel.getName ())) {
				PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
				data.setCurrentChannel (channel);
				ChannelManager.setPlayerChannel (player.getGameProfile ().getId (),channel);
				DataHelper2.forceSave (Keys.PLAYER_DATA,data);
				ChatHelper.sendMessageTo (player,Local.CHANNEL_CHANGED.replaceAll ("#",channel.getName ()));
			} else if (channel != null)
				ChatHelper.sendMessageTo (player,Local.CHANNEL_PERMS.replaceAll ("#",channel.getName ()));
			else
				ChatHelper.sendMessageTo (player,Local.CHANNEL_INVALID.replaceAll ("#",args[0]));
		}
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoComplete (args,ChannelManager.getChannels ());
	}

	@Override
	public String getDescription () {
		return "Handles changing of channels";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@SubCommand
	public void list (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		List <IDataType> channels = ChannelManager.getChannels ();
		List <String> channelNames = new ArrayList <> ();
		for (IDataType channel : channels)
			channelNames.add (channel.getID ());
		ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Channels: " + TextFormatting.GOLD + Strings.join (channelNames,", "));
	}

	@Override
	public boolean hasSubCommand () {
		return true;
	}
}
