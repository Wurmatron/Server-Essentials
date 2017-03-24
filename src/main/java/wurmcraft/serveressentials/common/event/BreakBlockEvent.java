package wurmcraft.serveressentials.common.event;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.utils.ClaimManager;

public class BreakBlockEvent {

		/**
			* TODO I Know this is a terrable idea but i wanted to test it before working on a better one
			*/
		@SubscribeEvent
		public void onBlockBreak(BlockEvent.BreakEvent e) {
				Claim claim = ClaimManager.getClaim(e.getPlayer().dimension, e.getPos());
				if (claim != null && !e.getPlayer().getGameProfile().getId().equals(claim.getOwner())) {
						e.setCanceled(true);
						e.getPlayer().worldObj.notifyBlockUpdate(e.getPos(),e.getState(),e.getState(),2);
				}
		}
}
