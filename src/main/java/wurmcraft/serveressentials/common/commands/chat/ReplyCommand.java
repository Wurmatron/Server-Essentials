package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class ReplyCommand extends EssentialsCommand {

	public ReplyCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "reply";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/reply <message>";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("r");
		aliases.add ("R");
		aliases.add ("Reply");
		aliases.add ("REPLY");
		return aliases;
	}

	@Override
	public String getDescription () {
		return "Sends a message to the last known person";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (DataHelper.lastMessage.containsKey (player.getGameProfile ().getId ())) {
			for (EntityPlayer entity : server.getPlayerList ().getPlayerList ())
				if (entity.getGameProfile ().getId ().equals (DataHelper.lastMessage.get (player.getGameProfile ().getId ())))
					FMLCommonHandler.instance ().getMinecraftServerInstance ().getCommandManager ().executeCommand (sender,"/msg " + entity.getDisplayNameString () + " " + Strings.join (args," "));
		} else
			ChatHelper.sendMessageTo (player,getDescription ());
	}
}
