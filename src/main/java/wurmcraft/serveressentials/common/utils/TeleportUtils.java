package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.config.Settings;

import java.util.List;
import java.util.UUID;

public class TeleportUtils {

	private final static long ONE_SECOND = 1000;
	private final static long ONE_MINUTE = ONE_SECOND * 60;
	private final static long ONE_HOUR = ONE_MINUTE * 60;
	private final static long ONE_DAY = ONE_HOUR * 24;

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

	public static String getRemainingCooldown (UUID uuid) {
		return getRemainingCooldown (DataHelper.getPlayerData (uuid).getTeleportTimer ());
	}

	public static String getRemainingCooldown (long playerTimer) {
		return Integer.toString (Math.round ((System.currentTimeMillis () - playerTimer)) / 1000);
	}

	public static boolean canTeleport (UUID uuid) {
		if (uuid != null && DataHelper.getPlayerData (uuid) != null) {
			long teleport_timer = DataHelper.getPlayerData (uuid).getTeleportTimer ();
			return teleport_timer + (Settings.teleport_cooldown * 1000) <= System.currentTimeMillis ();
		}
		return false;
	}

}
