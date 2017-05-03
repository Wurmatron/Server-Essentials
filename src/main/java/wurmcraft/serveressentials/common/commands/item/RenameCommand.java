package wurmcraft.serveressentials.common.commands.item;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.chat.ChatHelper;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand extends EssentialsCommand {

	public RenameCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "rename";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/rename <name>";
	}

	@Override
	public List<String> getCommandAliases () {
		List <String> aliases = new ArrayList<> ();
		aliases.add ("Rename");
		aliases.add ("ReName");
		aliases.add ("reName");
		aliases.add ("RENAME");
		aliases.add ("name");
		aliases.add ("Name");
		aliases.add ("NAME");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			if(args.length > 0) {
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				if (player.getHeldItemMainhand () != null) {
					player.getHeldItemMainhand ().setStackDisplayName (Strings.join (args," ").replaceAll ("&", "\u00A7"));
					ChatHelper.sendMessageTo (player,Local.NAME_CHANGED.replaceAll ("#", Strings.join (args," ").replaceAll ("&", "\u00A7")));
				} else
					ChatHelper.sendMessageTo (player,Local.NO_ITEM);
			} else
				ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
		} else
			ChatHelper.sendMessageTo (sender, Local.PLAYER_ONLY);
	}
}
