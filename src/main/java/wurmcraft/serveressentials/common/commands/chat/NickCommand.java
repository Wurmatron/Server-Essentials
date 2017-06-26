package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class NickCommand extends EssentialsCommand {

	public NickCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "nick";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/nick <nick> | /nick <name> <nick>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Nick");
		aliases.add ("NICK");
		return aliases;
	}

	@Override
	public String getDescription () {
		return "Changes a players name";
	}

	//TODO filter non: A-z0-9\-\_\+\=\&
	//TODO config for max length
	//TODO
	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (UsernameResolver.isValidNickname(args[0])) {ChatHelper.sendMessageTo(player,
					TextFormatting.RED+"Nickname: '"+TextFormatting.DARK_AQUA+args[0]+TextFormatting.RED+"' already taken!");return;}
			DataHelper.setNickname (player.getGameProfile ().getId (),args[0]);
			ChatHelper.sendMessageTo (player,Local.NICKNAME.replaceAll ("#",args[0]));
		} else if (args.length == 2) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (UsernameResolver.isValidNickname(args[1])) {ChatHelper.sendMessageTo(player,
					TextFormatting.RED+"Nickname: '"+TextFormatting.DARK_AQUA+args[0]+TextFormatting.RED+"' already taken!");return;}
			if (player != null) {
				DataHelper.setNickname (player.getGameProfile ().getId (),args[1]);
				ChatHelper.sendMessageTo (player,Local.NICKNAME.replaceAll ("#",args[1]));
				ChatHelper.sendMessageTo (sender,Local.NICKNAME_OTHER.replaceAll ("#",args[0]).replaceAll ("&",args[1]));
			}
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
