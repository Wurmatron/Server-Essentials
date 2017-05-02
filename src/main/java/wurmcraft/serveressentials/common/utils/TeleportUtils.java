package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.config.Settings;

import java.util.UUID;

public class TeleportUtils {

	public static void teleportTo (MinecraftServer server,UUID uuid,BlockPos pos,boolean timer) {
		PlayerList players = server.getPlayerList ();
		if (players.getCurrentPlayerCount () > 0) {
			for (EntityPlayer player : players.getPlayerList ())
				if (player.getGameProfile ().getId ().equals (uuid))
					teleportTo (player,pos,timer);
		}
	}

	public static void teleportTo (EntityPlayer player,BlockPos pos,boolean timer) {
		if (player != null && pos != null) {
			DataHelper.setLastLocation (player.getGameProfile ().getId (),new BlockPos (player.posX,player.posY,player.posZ));
			player.setPositionAndUpdate (pos.getX (),pos.getY (),pos.getZ ());
			if (timer)
				DataHelper.updateTeleportTimer (player.getGameProfile ().getId ());
		}
	}

	public static void teleportTo (EntityPlayer player,BlockPos pos,int dim,boolean timer) {
		if (player != null && pos != null) {
			DataHelper.setLastLocation (player.getGameProfile ().getId (),new BlockPos (player.posX,player.posY,player.posZ));
			if (player.dimension != dim)
				player.changeDimension (dim);
			player.setPositionAndUpdate (pos.getX (),pos.getY (),pos.getZ ());
			if (timer)
				DataHelper.updateTeleportTimer (player.getGameProfile ().getId ());
		}
	}

	public static EntityPlayer getPlayerFromUsername (MinecraftServer server,String username) {
		PlayerList players = server.getPlayerList ();
		if (players.getCurrentPlayerCount () > 0) {
			for (EntityPlayer player : players.getPlayerList ())
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (username))
					return player;
		}
		return null;
	}

	public static String getRemainingCooldown (UUID uuid) {
		return getRemainingCooldown (DataHelper.getPlayerData (uuid).getTeleport_timer ());
	}

	public static String getRemainingCooldown (long playerTimer) {
		return Integer.toString (Math.round ((System.currentTimeMillis () - playerTimer)));
	}

	public static boolean canTeleport (UUID uuid) {
		if (uuid != null && DataHelper.getPlayerData (uuid) != null) {
			long teleport_timer = DataHelper.getPlayerData (uuid).getTeleport_timer ();
			return teleport_timer + (Settings.teleport_cooldown * 1000) <= System.currentTimeMillis ();
		}
		return false;
	}
}
