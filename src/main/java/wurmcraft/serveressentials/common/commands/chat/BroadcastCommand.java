package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

public class BroadcastCommand extends SECommand {

	public BroadcastCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "broadcast";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/broadcast <message>";
	}

	@Override
	public String[] getAliases () {
		return new String[] {"bc"};
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0)
			FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendChatMsg (new TextComponentString (Strings.join (args," ").replaceAll ("&","\u00A7")));
		else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public String getDescription () {
		return "Broadcast a message to everyone on the server";
	}
}
