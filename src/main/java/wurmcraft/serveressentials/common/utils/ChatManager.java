package wurmcraft.serveressentials.common.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class ChatManager {

	public static void sendMessage (EntityPlayer player,String message) {
		sendMessage (player,message,null,null,null);
	}

	public static void sendMessage (ICommandSender sender,String message) {
		sendMessage (sender,message,null,null,null);
	}

	public static void sendMessage(EntityPlayer player, String message, HoverEvent hoverEvent) {
		sendMessage (player,message,null,hoverEvent,null);
	}

	public static void sendMessage(ICommandSender sender,String message,ClickEvent clickEvent,HoverEvent hoverEvent, String shift) {
		TextComponentString msg = new TextComponentString (message);
		msg.getStyle ().setClickEvent (clickEvent).setHoverEvent (hoverEvent).setInsertion (shift);
		sender.addChatMessage (msg);
	}
}
