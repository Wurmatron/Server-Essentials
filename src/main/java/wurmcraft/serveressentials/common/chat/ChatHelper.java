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
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatHelper {

	public static final String USERNAME_KEY = "%username%";
	public static final String CHANNEL_KEY = "%channel%";
	public static final String MESSAGE_KEY = "%message%";
	public static final String DIMENSION_KEY = "%dimension%";
	public static final String RANK_PREFIX_KEY = "%rankPrefix%";
	public static final String RANK_SUFFIX_KEY = "%rankSuffix%";
	public static HashMap <String, String[]> lastChat = new HashMap <> ();

	public static String format (String username,IRank rank,Channel channel,int dimension,String message) {
		String format;
		if (rank.getSuffix () != null && !rank.getSuffix ().equals (""))
			format = StringUtils.replaceEach (Settings.chatFormat,new String[] {USERNAME_KEY,CHANNEL_KEY,MESSAGE_KEY,DIMENSION_KEY,RANK_PREFIX_KEY,RANK_SUFFIX_KEY},new String[] {username,channel.getPrefix ().replaceAll ("&","\u00A7") + TextFormatting.RESET,message,Integer.toString (dimension),rank.getPrefix ().replaceAll ("&","\u00A7") + TextFormatting.RESET,rank.getSuffix ().replaceAll ("&","\u00A7") + TextFormatting.RESET});
		else
			format = StringUtils.replaceEach (Settings.chatFormat.replaceAll (" " + RANK_SUFFIX_KEY,""),new String[] {USERNAME_KEY,CHANNEL_KEY,MESSAGE_KEY,DIMENSION_KEY,RANK_PREFIX_KEY},new String[] {username,channel.getPrefix ().replaceAll ("&","\u00A7") + TextFormatting.RESET,message,Integer.toString (dimension),rank.getPrefix ().replaceAll ("&","\u00A7") + TextFormatting.RESET});
		return format;
	}

	public static void sendMessageTo (ICommandSender sender,String message,ClickEvent clickEvent,Void differentiator) {
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

	public static void sendMessageTo (ICommandSender sender,EntityPlayer reciver,String message) {
		sendMessageTo (reciver,message);
		if (DataHelper.spys.size () > 0)
			for (UUID uuid : DataHelper.spys)
				for (EntityPlayer spy : sender.getServer ().getPlayerList ().getPlayerList ())
					if (spy.getGameProfile ().getId ().equals (uuid))
						sendMessageTo (spy,TextFormatting.RED + "[Spy] " + TextFormatting.DARK_AQUA + reciver.getName () + TextFormatting.GREEN + " <- " + message);
	}

	public static void sendMessageTo (EntityPlayer sender,EntityPlayer reciver,String message) {
		PlayerData reciverData = DataHelper.getPlayerData (reciver.getGameProfile ().getId ());
		String reciverName = reciverData.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + reciverData.getNickname ().replaceAll ("&","\u00A7") : reciver.getDisplayNameString ();
		sendMessageTo (reciver,message);
		if (DataHelper.spys.size () > 0)
			for (UUID uuid : DataHelper.spys)
				for (EntityPlayer spy : sender.getServer ().getPlayerList ().getPlayerList ())
					if (spy.getGameProfile ().getId ().equals (uuid))
						sendMessageTo (spy,TextFormatting.RED + "[Spy] " + TextFormatting.DARK_AQUA + reciverName + TextFormatting.GREEN + " <- " + message);
	}

	public static void sendMessageTo (EntityPlayer player,String message) {
		sendMessageTo (player,message,null);
	}

	public static void sendMessageTo (ICommandSender sender,String message) {
		sender.addChatMessage (new TextComponentString (message));
	}

	public static void sendMessage (String displayName,IRank rank,Channel channel,int dimension,String message) {
		if (Settings.logChat) {
			LogHelper.addChat (channel,"[" + new Date (System.currentTimeMillis ()).toString () + "] " + TextFormatting.getTextWithoutFormattingCodes (displayName) + " " + message);
			LogHelper.checkAndSave ();
		}
		if (handleMessage (displayName,message)) {
			sendChannelMessage (channel,displayName,rank,dimension,message);
			LogHandler.chat (format (displayName,rank,channel,dimension,message));
		} else {
			EntityPlayer player = UsernameResolver.getPlayer (displayName);
			if (player != null)
				sendMessageTo (player,Local.SPAM);
		}
	}

	public static void sendChannelMessage (Channel channel,String displayName,IRank rank,int dimension,String message) {
		PlayerList players = FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ();
		List <UUID> recivers = ChannelManager.getPlayersInChannel (channel);
		for (EntityPlayerMP player : players.getPlayerList ()) {
			if (recivers.contains (player.getGameProfile ().getId ()))
				player.addChatMessage (new TextComponentString (format (displayName,rank,channel,dimension,message)));
		}
	}

	private static boolean handleMessage (String name,String message) {
		if (lastChat.containsKey (name)) {
			String[] chat = lastChat.get (name);
			if (chat[0].equalsIgnoreCase (message)) {
				int count = 0;
				for (int index = 0; index < chat.length; index++)
					if (message.equalsIgnoreCase (chat[index]))
						count++;
					else if (chat[index] == null) {
						chat[index] = message;
						count++;
						break;
					}
				if (count >= Settings.spamLimit)
					return false;
			} else {
				lastChat.remove (name);
				return true;
			}
		} else {
			String[] chat = new String[Settings.spamLimit];
			chat[0] = message;
			lastChat.put (name,chat);
		}
		return true;
	}

}
