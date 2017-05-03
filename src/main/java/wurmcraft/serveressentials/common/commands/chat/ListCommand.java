package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListCommand extends EssentialsCommand {

	public ListCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "list";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/list";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		List <EntityPlayerMP> players = server.getPlayerList ().getPlayerList ();
		List <UUID> pList = new ArrayList <> ();
		for (EntityPlayerMP player : players)
			pList.add (player.getGameProfile ().getId ());
		ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER);
		for (UUID name : pList) {
			IRank rank = DataHelper.getPlayerData (name).getRank ();
			ChatHelper.sendMessageTo (sender,TextFormatting.AQUA + UsernameCache.getLastKnownUsername (name) + " : " + rank.getName ());
		}
		ChatHelper.sendMessageTo (sender,TextFormatting.RED + Local.SPACER);
	}
}
