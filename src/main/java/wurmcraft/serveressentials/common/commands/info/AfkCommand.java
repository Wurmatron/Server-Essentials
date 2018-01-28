package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class AfkCommand extends SECommand {

	public AfkCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "afk";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/afk";
	}

	@Override
	public String getDescription () {
		return "Used to tell if a person is AFK";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (DataHelper2.getTemp (Keys.AFK,player.getGameProfile ().getId ()) != null && DataHelper2.getTemp (Keys.AFK,player.getGameProfile ().getId ()).get (player.getGameProfile ().getId ()) != null) {
			FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendMessage (new TextComponentString (Local.AFK_OFF.replaceAll ("#",player.getDisplayNameString ())));
			DataHelper2.addTemp (Keys.AFK,player.getGameProfile ().getId (),false,true);
		} else {
			FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ().sendMessage (new TextComponentString (Local.AFK_NOW.replaceAll ("#",player.getDisplayNameString ())));
			DataHelper2.addTemp (Keys.AFK,player.getGameProfile ().getId (),true,false);
		}
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}
}
