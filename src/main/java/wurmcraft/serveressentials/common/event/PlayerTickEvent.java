package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

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
			DataHelper.setFrozen (player.getGameProfile ().getId (),true);
		}
	}

	public static void removeFrozen (EntityPlayer player) {
		if (frozenPlayers.size () > 0 && frozenPlayers.keySet ().contains (player)) {
			frozenPlayers.remove (player);
			player.capabilities.disableDamage = false;
			DataHelper.setFrozen (player.getGameProfile ().getId (),false);
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
		if (DataHelper.activeRequests.size () > 0 && e.player.world.getWorldTime () % 20 == 0)
			for (long time : DataHelper.activeRequests.keySet ())
				if ((time + (Settings.tpa_timeout * 1000)) <= System.currentTimeMillis ())
					DataHelper.activeRequests.remove (time);
		if (frozenPlayers.size () > 0 && frozenPlayers.keySet ().contains (e.player)) {
			BlockPos lockedPos = frozenPlayers.get (e.player);
			if (e.player.getPosition () != lockedPos)
				e.player.setPositionAndUpdate (lockedPos.getX (),lockedPos.getY (),lockedPos.getZ ());
		}
	}
}
