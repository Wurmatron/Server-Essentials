package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.WorldGenHandler;

public class PreGenCommand extends SECommand {

	private static WorldGenHandler worldGen;

	public PreGenCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "pregen";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/regen";
	}

	@Override
	public String[] getAliases () {
		return new String[] {"gen"};
	}

	@Override
	public String getDescription () {
		return "Pre-Generate the world";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (worldGen == null)
			worldGen = new WorldGenHandler (server.worldServers[0].getChunkProvider (),server.worldServers[0],20);
		else
			worldGen = null;
		ChatHelper.sendMessageTo (sender,"" + worldGen);
	}

	@SubscribeEvent
	public void tick (TickEvent.ServerTickEvent e) {
		if (worldGen != null)
			worldGen.cycle ();
	}
}
