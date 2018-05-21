package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.commands.teleport.TpaCommand;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import java.util.HashMap;

public class PlayerTickEvent {

	private static HashMap <EntityPlayer, PlayerInventory> openInv = new HashMap <> ();
	private static HashMap <EntityPlayer, BlockPos> frozenPlayers = new HashMap <> ();

	public static void register (PlayerInventory inv) {
		openInv.put (inv.owner,inv);
	}

	public static void remove (PlayerInventory inv) {
		openInv.remove (inv.owner,inv);
	}

	public static void addFrozen (EntityPlayer player,BlockPos pos) {
		if (!frozenPlayers.keySet ().contains (player)) {
			player.capabilities.disableDamage = true;
			frozenPlayers.put (player,pos);
			UsernameResolver.getPlayerData (player.getGameProfile ().getId ()).setFrozen (true);
		}
	}

	public static void removeFrozen (EntityPlayer player) {
		if (frozenPlayers.size () > 0 && frozenPlayers.keySet ().contains (player)) {
			frozenPlayers.remove (player);
			player.capabilities.disableDamage = false;
			UsernameResolver.getPlayerData (player.getGameProfile ().getId ()).setFrozen (false);
		}
	}

	public static void toggleFrozen (EntityPlayer player,BlockPos pos) {
		if (frozenPlayers.keySet ().contains (player))
			removeFrozen (player);
		else
			addFrozen (player,pos);
	}

	public static boolean isFrozen (EntityPlayer player) {
		return frozenPlayers.keySet ().contains (player);
	}

	@SubscribeEvent
	public void tickStart (TickEvent.PlayerTickEvent e) {
		if (openInv.size () > 0 && openInv.containsKey (e.player))
			openInv.get (e.player).update ();
		if (TpaCommand.activeRequests.size () > 0 && e.player.world.getWorldTime () % 20 == 0)
			for (long time : TpaCommand.activeRequests.keySet ())
				if ((time + (ConfigHandler.tpaTimeout * 1000)) <= System.currentTimeMillis ())
					TpaCommand.activeRequests.remove (time);
		if (frozenPlayers.size () > 0 && frozenPlayers.keySet ().contains (e.player)) {
			BlockPos lockedPos = frozenPlayers.get (e.player);
			if (e.player.getPosition () != lockedPos)
				e.player.setPositionAndUpdate (lockedPos.getX (),lockedPos.getY (),lockedPos.getZ ());
		}
	}
}
