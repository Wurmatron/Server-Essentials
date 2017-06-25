package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChannelCommand extends EssentialsCommand {

	public ChannelCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "channel";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/channel <name> | /channel list";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("ch");
		aliases.add ("CH");
		aliases.add ("Channel");
		aliases.add ("CHANNEL");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length == 0)
			execute (server,sender,new String[] {"list"});
		else if (args.length == 1) {
			if (args[0].equalsIgnoreCase ("list")) {
				List <Channel> channels = ChannelManager.getChannels ();
				List <String> channelNames = new ArrayList <> ();
				for (Channel channel : channels)
					channelNames.add (channel.getName ());
				ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Channels: " + TextFormatting.GOLD + Strings.join (channelNames,", "));
			} else {
				Channel channel = ChannelManager.getFromName (args[0]);
				if (channel != null && EssentialsCommand.checkPerms (DataHelper.getPlayerData (player.getGameProfile ().getId ()).getRank (),"channel." + channel.getName ())) {
					DataHelper.setChannel (player.getGameProfile ().getId (),channel);
					ChatHelper.sendMessageTo (player,Local.CHANNEL_CHANGED.replaceAll ("#",channel.getName ()));
				} else if (channel != null)
					ChatHelper.sendMessageTo (player,Local.CHANNEL_PERMS.replaceAll ("#",channel.getName ()));
				else
					ChatHelper.sendMessageTo (player,Local.CHANNEL_INVALID.replaceAll ("#",args[0]));
			}
		} else
			ChatHelper.sendMessageTo (player,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteChannel (args,ChannelManager.getChannels ());
	}

	@Override
	public String getDescription () {
		return "Handles changing of channels";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}
}
