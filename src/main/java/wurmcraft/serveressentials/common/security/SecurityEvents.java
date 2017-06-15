package wurmcraft.serveressentials.common.security;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;

public class SecurityEvents {

	@SubscribeEvent
	public void creativeCheck (TickEvent.PlayerTickEvent e) {
		if (e.player.worldObj.getWorldTime () % 100 == 0) {
			IRank rank = DataHelper.getPlayerData (e.player.getGameProfile ().getId ()).getRank ();
			boolean validInCreative = false;
			if (rank.getPermissions () != null && rank.getPermissions ().length > 0) {
				for (String perm : rank.getPermissions ())
					if (perm.equalsIgnoreCase (Perm.CREATIVE))
						validInCreative = true;
				if (!validInCreative && !SecurityUtils.isTrustedMember (e.player)) {
					EntityPlayerMP player = (EntityPlayerMP) e.player;
					player.setGameType (GameType.SURVIVAL);
					player.connection.kickPlayerFromServer (Local.SECURITY_CREATIVE_KICK);
					LogHandler.info ("# was kicked from the server for being in creative!".replaceAll ("#",player.getDisplayNameString ()));
				}
			}
		}
	}
}
