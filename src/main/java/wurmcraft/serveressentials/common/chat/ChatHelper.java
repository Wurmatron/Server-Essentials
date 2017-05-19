package wurmcraft.serveressentials.common.chat;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.LogHandler;

import java.util.List;
import java.util.UUID;

public class ChatHelper {

	public static final String USERNAME_KEY = "%username%";
	public static final String CHANNEL_KEY = "%channel%";
	public static final String MESSAGE_KEY = "%message%";
	public static final String DIMENSION_KEY = "%dimension%";
	public static final String RANK_PREFIX_KEY = "%rankPrefix%";
	public static final String RANK_SUFFIX_KEY = "%rankSuffix%";

	public static String format (String username,IRank rank,Channel channel,int dimension,String message) {
		return StringUtils.replaceEach (Settings.chatFormat,new String[] {USERNAME_KEY,CHANNEL_KEY,MESSAGE_KEY,DIMENSION_KEY,RANK_PREFIX_KEY,RANK_SUFFIX_KEY},new String[] {username,channel.getPrefix ().replaceAll ("&","\u00A7") + TextFormatting.RESET,TextFormatting.RESET + message,Integer.toString (dimension),rank.getPrefix ().replaceAll ("&","\u00A7") + TextFormatting.RESET,rank.getSuffix ().replaceAll ("&","\u00A7") + TextFormatting.RESET});
	}

	public static void sendMessageTo (ICommandSender sender,String message,ClickEvent clickEvent, int index) {
		TextComponentString msg = new TextComponentString (message);
		if (clickEvent != null)
			msg.getStyle ().setClickEvent (clickEvent);
		sender.addChatMessage (msg);
	}

	public static void sendMessageTo (EntityPlayer player,String message,HoverEvent hoverEvent) {
		TextComponentString msg = new TextComponentString (message);
		if (hoverEvent != null)
			msg.getStyle ().setHoverEvent (hoverEvent);
		player.addChatComponentMessage (msg);
	}

	public static void sendMessageTo (EntityPlayer player,String message) {
		sendMessageTo (player,message,null);
	}

	public static void sendMessageTo (ICommandSender sender,String message) {
		sender.addChatMessage (new TextComponentString (message));
	}

	public static void sendMessage (String displayName,IRank rank,Channel channel,int dimension,String message) {
		LogHandler.chat (format (displayName,rank,channel,dimension,message));
		sendChannelMessage (channel,displayName,rank,dimension,message);
	}

	public static void sendChannelMessage (Channel channel,String displayName,IRank rank,int dimension,String message) {
		PlayerList players = FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ();
		List <UUID> recivers = ChannelManager.getPlayersInChannel (channel);
		for (EntityPlayerMP player : players.getPlayerList ()) {
			if (recivers.contains (player.getGameProfile ().getId ()))
				player.addChatMessage (new TextComponentString (format (displayName,rank,channel,dimension,message)));
		}
	}
}
