package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.LogHandler;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import java.util.Random;

public class RandomTeleportCommand extends SECommand {

	private static final Random rand = new Random ();

	public RandomTeleportCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "randomTP";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"rtp"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/rtp";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Teleport to a random world location";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (TeleportUtils.canTeleport (player.getGameProfile ().getId ())) {
			BlockPos randPos = getRandomPos (player);
			if (TeleportUtils.safeLocation (player.world,randPos)) {
				TeleportUtils.teleportTo (player,randPos,false);
				ChatHelper.sendMessageTo (player,Local.RAND_TP);
			} else {
				for (int tries = 0; tries < 5; tries++) {
					BlockPos tempPos = getRandomPos (player);
					LogHandler.info ("Test: ");
					if (TeleportUtils.safeLocation (player.world,randPos)) {
						TeleportUtils.teleportTo (player,tempPos,false);
						ChatHelper.sendMessageTo (player,Local.RAND_TP);
						return;
					}
				}
				ChatHelper.sendMessageTo (player,Local.RTP_FAIL);
			}
		} else
			ChatHelper.sendMessageTo (player,TeleportUtils.getRemainingCooldown (player.getGameProfile ().getId ()));
	}

	private int chance (int no) {
		if (rand.nextBoolean ())
			return no;
		else
			return -no;
	}

	private BlockPos getRandomPos (EntityPlayer player) {
		WorldBorder border = player.world.getWorldBorder ();
		double maxLocationX = (border.getDiameter () / 2) + border.getCenterX ();
		double maxLocationZ = (border.getDiameter () / 2) + border.getCenterZ ();
		int x = rand.nextInt ((int) maxLocationX);
		int z = rand.nextInt ((int) maxLocationZ);
		return player.world.getTopSolidOrLiquidBlock (new BlockPos (chance (x),64,chance (z)));
	}
}
