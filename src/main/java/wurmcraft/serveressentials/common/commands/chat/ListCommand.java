package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListCommand extends SECommand {

	public ListCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "list";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"players","online"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/list";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		List <EntityPlayerMP> players = server.getPlayerList ().getPlayers ();
		List <UUID> pList = new ArrayList <> ();
		for (EntityPlayerMP player : players)
			pList.add (player.getGameProfile ().getId ());
		ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER);
		for (UUID name : pList) {
			IRank rank = ((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,name.toString ())).getRank ();
			ChatHelper.sendMessageTo (sender,TextFormatting.AQUA + UsernameCache.getLastKnownUsername (name) + " : " + rank.getName (),clickEvent (UsernameCache.getLastKnownUsername (name)),null);
		}
		ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER);
	}

	@Override
	public String getDescription () {
		return "Lists all the players on the server";
	}

	private static ClickEvent clickEvent (String name) {
		return new ClickEvent (ClickEvent.Action.SUGGEST_COMMAND,"/msg # ".replaceAll ("#","" + name));
	}
}
