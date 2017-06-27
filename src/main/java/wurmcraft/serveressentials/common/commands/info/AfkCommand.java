package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class AfkCommand extends EssentialsCommand {

	public AfkCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "afk";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/afk";
	}

	@Override
	public String getDescription () {
		return "Used to tell if a person is AFK";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Afk");
		aliases.add ("AFK");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (!DataHelper.isAfk (player.getGameProfile ().getId ())) {
			FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendChatMsg (new TextComponentString (Local.AFK_NOW.replaceAll ("#",player.getDisplayNameString ())));
			DataHelper.addAfkPlayer (player);
		} else {
			FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendChatMsg (new TextComponentString (Local.AFK_OFF.replaceAll ("#",player.getDisplayNameString ())));
			DataHelper.addAfkPlayer (player);
			DataHelper.removeAfkPlayer (player);
		}
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}
}
