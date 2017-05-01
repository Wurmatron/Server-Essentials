package wurmcraft.serveressentials.common.event;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.claim.ChunkManager;

public class ClaimEvent {

		@SubscribeEvent
		public void onBlockBreak(BlockEvent.BreakEvent e) {
				Claim claim = ChunkManager.getClaim(e.getPos());
				if (claim != null && !ChunkManager.canDestroy(claim, e.getPlayer().getGameProfile().getId())) {
						e.setCanceled(true); e.getPlayer().worldObj.notifyBlockUpdate(e.getPos(), e.getState(), e.getState(), 2);
						e.getPlayer().addChatComponentMessage(new TextComponentString("You cannot break that block!"));
				}
		}

		@SubscribeEvent
		public void onBlockPlace(BlockEvent.PlaceEvent e) {
				Claim claim = ChunkManager.getClaim(e.getPos());
				if (claim != null && !ChunkManager.canDestroy(claim, e.getPlayer().getGameProfile().getId())) {
						e.setCanceled(true);
						e.getPlayer().worldObj.notifyBlockUpdate(e.getPos(), e.getState(), e.getState(), 2);
						e.getPlayer().addChatComponentMessage(new TextComponentString("You cannot place a block!"));
				}
		}
}
