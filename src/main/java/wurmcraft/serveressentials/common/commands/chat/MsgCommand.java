package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class MsgCommand extends SECommand {

	public MsgCommand (Perm perm) {
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
	public String[] getAliases () {
		return new String[] {"pm"};
	}

	@Override
	public String getDescription () {
		return "Send to message to another player";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer senderPlayer = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length >= 2) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (player != null) {
				String[] lines = new String[args.length - 1];
				for (int index = 1; index < args.length; index++)
					lines[index - 1] = args[index];
				String message = Strings.join (lines," ");
				PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
				String dataName = data.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + data.getNickname ().replaceAll ("&","\u00A7") : player.getDisplayNameString ();
				ChatHelper.sendMessageTo (sender,Local.MESSAGE_SENT.replaceAll ("#",dataName));
				if (senderPlayer != null) {
					PlayerData senderData = DataHelper.getPlayerData (senderPlayer.getGameProfile ().getId ());
					String senderName = senderData.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + senderData.getNickname ().replaceAll ("&","\u00A7") : senderPlayer.getDisplayNameString ();
					DataHelper.lastMessage.put (player.getGameProfile ().getId (),senderPlayer.getGameProfile ().getId ());
					ChatHelper.sendMessageTo (senderPlayer,player,Settings.messageFormat.replaceAll (ChatHelper.USERNAME_KEY,TextFormatting.AQUA + senderName).replaceAll (ChatHelper.MESSAGE_KEY,TextFormatting.GRAY + message));
				} else {
					ChatHelper.sendMessageTo (sender,player,Settings.messageFormat.replaceAll (ChatHelper.USERNAME_KEY,TextFormatting.AQUA + sender.getName ()).replaceAll (ChatHelper.MESSAGE_KEY,TextFormatting.GRAY + message));
				}
			}
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
