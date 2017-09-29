package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.WorldGenHandler;

public class PreGenCommand extends SECommand {

	private static WorldGenHandler worldGen;

	public PreGenCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "pregen";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/pregen | /pregen start <dim> <speed> | /pregen stop | /pregen pause";
	}

	@Override
	public String getDescription () {
		return "Pre-Generate the world";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer && args.length == 0) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (worldGen == null) {
				worldGen = new WorldGenHandler (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[player.dimension].getChunkProvider (),FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[player.dimension],20);
				ChatHelper.sendMessageTo (player,Local.PREGEN_STARTED);
			} else if (worldGen.running && !worldGen.finished) {
				worldGen.running = false;
				ChatHelper.sendMessageTo (player,Local.PREGEN.replaceAll ("#","Paused"));
			} else if (!worldGen.finished) {
				worldGen.running = true;
				ChatHelper.sendMessageTo (player,Local.PREGEN.replaceAll ("#","Unpaused"));
			}
		} else
			super.execute (server,sender,args);
	}

	@SubscribeEvent
	public void tick (TickEvent.WorldTickEvent e) {
		if (worldGen != null)
			worldGen.cycle (e.world);
	}

	private boolean checkBorderSize (World world) {
		return world.getWorldBorder ().getDiameter () <= 100000;
	}

	@Override
	public boolean hasSubCommand () {
		return true;
	}

	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@SubCommand
	public void start (ICommandSender sender,String[] args) {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (args.length == 0) {
				worldGen = new WorldGenHandler (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[player.dimension].getChunkProvider (),FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[player.dimension],20);
				ChatHelper.sendMessageTo (player,Local.PREGEN_STARTED);
			} else if (args.length == 1) {
				try {
					int dim = Integer.parseInt (args[0]);
					if (checkBorderSize (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim]))
						ChatHelper.sendMessageTo (player,Local.PREGEN_WARN);
					worldGen = new WorldGenHandler (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim].getChunkProvider (),FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim],20);
					ChatHelper.sendMessageTo (player,Local.PREGEN_STARTED);
				} catch (NumberFormatException e) {
					ChatHelper.sendMessageTo (sender.getCommandSenderEntity (),Local.INVALID_NUMBER.replaceAll ("#",args[0]));
				}
			} else if (args.length == 2) {
				try {
					int dim = Integer.parseInt (args[0]);
					int speed = Integer.parseInt (args[1]);
					if (checkBorderSize (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim]))
						ChatHelper.sendMessageTo (player,Local.PREGEN_WARN);
					worldGen = new WorldGenHandler (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim].getChunkProvider (),FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim],speed);
					ChatHelper.sendMessageTo (player,Local.PREGEN_STARTED);
				} catch (NumberFormatException e) {
					ChatHelper.sendMessageTo (sender.getCommandSenderEntity (),Local.INVALID_NUMBER.replaceAll ("#",args[0]));
				}
			}
		} else {
			if (args.length == 0)
				ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
			else if (args.length == 1) {
				try {
					int dim = Integer.parseInt (args[0]);
					if (checkBorderSize (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim]))
						ChatHelper.sendMessageTo (sender,Local.PREGEN_WARN);
					worldGen = new WorldGenHandler (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim].getChunkProvider (),FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim],20);
					ChatHelper.sendMessageTo (sender,Local.PREGEN_STARTED);
				} catch (NumberFormatException e) {
					ChatHelper.sendMessageTo (sender.getCommandSenderEntity (),Local.INVALID_NUMBER.replaceAll ("#",args[0]));
				}
			} else if (args.length == 2) {
				try {
					int dim = Integer.parseInt (args[0]);
					int speed = Integer.parseInt (args[1]);
					if (checkBorderSize (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim]))
						ChatHelper.sendMessageTo (sender,Local.PREGEN_WARN);
					worldGen = new WorldGenHandler (FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim].getChunkProvider (),FMLCommonHandler.instance ().getMinecraftServerInstance ().worlds[dim],speed);
					ChatHelper.sendMessageTo (sender,Local.PREGEN_STARTED);
				} catch (NumberFormatException e) {
					ChatHelper.sendMessageTo (sender.getCommandSenderEntity (),Local.INVALID_NUMBER.replaceAll ("#",args[0]));
				}
			}
		}
	}

	@SubCommand
	public void stop (ICommandSender sender,String[] args) {
		worldGen = null;
		ChatHelper.sendMessageTo (sender,Local.PREGEN_STOP);
	}

	public void pause (ICommandSender sender,String[] args) {
		worldGen.running = !worldGen.running;
		ChatHelper.sendMessageTo (sender,Local.PREGEN.replaceAll ("#",worldGen.running ? "Paused" : "Unpaused"));
	}
}
