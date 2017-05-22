package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MsgCommand extends EssentialsCommand {

	public MsgCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "msg";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/msg <user> <message>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("MSG");
		aliases.add ("Msg");
		aliases.add ("pm");
		aliases.add ("PM");
		return aliases;
	}

	@Override
	public String getDescription () {
		return "Send to message to another player";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length >= 2) {
			PlayerList players = server.getPlayerList ();
			for (EntityPlayer player : players.getPlayerList ())
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
					String[] lines = new String[args.length - 1];
					for (int index = 1; index < args.length; index++)
						lines[index - 1] = args[index];
					String message = Strings.join (lines," ");
					ChatHelper.sendMessageTo (player,Settings.messageFormat.replaceAll (ChatHelper.USERNAME_KEY,TextFormatting.AQUA + sender.getDisplayName ().getUnformattedText ()).replaceAll (ChatHelper.MESSAGE_KEY,TextFormatting.GRAY + message));
					ChatHelper.sendMessageTo (sender,Local.MESSAGE_SENT.replaceAll ("#",player.getDisplayNameString ()));
				}
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		if (args.length == 1) {
			List <String> usernames = new ArrayList <> ();
			for (EntityPlayer player : server.getPlayerList ().getPlayerList ())
				usernames.add (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()));
			return usernames;
		}
		return null;
	}
}
