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
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class MsgCommand extends SECommand {

	public MsgCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "msg";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/msg <user> <message>";
	}

	@Override
	public String[] getCommandAliases () {
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
				PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
				String dataName = data.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + data.getNickname ().replaceAll ("&","\u00A7") : player.getDisplayNameString ();
				ChatHelper.sendMessageTo (sender,Local.MESSAGE_SENT.replaceAll ("#",dataName));
				if (senderPlayer != null) {
					PlayerData senderData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,senderPlayer.getGameProfile ().getId ().toString ());
					String senderName = senderData.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + senderData.getNickname ().replaceAll ("&","\u00A7") : senderPlayer.getDisplayNameString ();
					DataHelper2.addTemp (Keys.LAST_MESSAGE,player.getGameProfile ().getId (),senderPlayer.getGameProfile ().getId (),false);
					ChatHelper.sendMessageTo (senderPlayer,player,ConfigHandler.msgFormat.replaceAll (ChatHelper.USERNAME_KEY,
						TextFormatting.AQUA + senderName).replaceAll (ChatHelper.MESSAGE_KEY,TextFormatting.GRAY + message));
				} else {
					ChatHelper.sendMessageTo (sender,player,ConfigHandler.msgFormat.replaceAll (ChatHelper.USERNAME_KEY,TextFormatting.AQUA + sender.getName ()).replaceAll (ChatHelper.MESSAGE_KEY,TextFormatting.GRAY + message));
				}
			}
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
