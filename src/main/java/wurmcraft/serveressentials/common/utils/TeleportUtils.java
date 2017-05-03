package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.config.Settings;

import java.util.UUID;

public class TeleportUtils {

	private final static long ONE_SECOND = 1000;
	private final static long ONE_MINUTE = ONE_SECOND * 60;
	private final static long ONE_HOUR = ONE_MINUTE * 60;
	private final static long ONE_DAY = ONE_HOUR * 24;

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

	public static String convertToHumanReadable (long duration) {
		String readable = "";
		long temp = 0;
		if (duration >= ONE_SECOND) {
			temp = duration / ONE_DAY;
			if (temp > 0) {
				duration -= temp * ONE_DAY;
				readable = temp + " day";
				if (temp > 0)
					readable = readable + "s";
				if (duration >= ONE_MINUTE)
					readable = readable + ", ";
			}
			temp = duration / ONE_HOUR;
			if (temp > 0) {
				duration -= temp * ONE_HOUR;
				readable = readable + temp + " hour";
				if (temp > 1)
					readable = readable + "s";
				if (duration >= ONE_MINUTE)
					readable = readable + ", ";
			}
			temp = duration / ONE_MINUTE;
			if (temp > 0)
				duration -= temp * ONE_MINUTE;
			readable = readable + temp + " minute";
			if (temp > 1)
				readable = readable + "s";
			if (!readable.equals ("") && duration >= ONE_SECOND)
				readable = readable + " and ";
			temp = duration / ONE_SECOND;
			if (temp > 0)
				readable = readable + temp + " second";
			if (temp > 1)
				readable = readable + "s";
			return readable;
		} else
			return "Online";
	}
}
