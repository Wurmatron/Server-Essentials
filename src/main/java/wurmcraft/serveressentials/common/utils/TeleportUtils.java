package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;

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
			player.setPosition (pos.getX (),pos.getY (),pos.getZ ());
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
}
