package wurmcraft.serveressentials.common.claim;

import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ChatManager;

public class ClaimEvent {

	@SubscribeEvent
	public void onBlockBreak (BlockEvent.BreakEvent e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if (claim != null && !ChunkManager.canDestroy (claim,e.getPlayer ().getGameProfile ().getId ())) {
			e.setCanceled (true);
			e.getPlayer ().worldObj.notifyBlockUpdate (e.getPos (),e.getState (),e.getState (),2);
			ChatManager.sendMessage (e.getPlayer (),Local.CLAIM_BREAK.replaceAll ("#", getOwner (claim)));
		}
	}

	@SubscribeEvent
	public void onBlockPlace (BlockEvent.PlaceEvent e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if (claim != null && !ChunkManager.canDestroy (claim,e.getPlayer ().getGameProfile ().getId ())) {
			e.setCanceled (true);
			e.getPlayer ().worldObj.notifyBlockUpdate (e.getPos (),e.getState (),e.getState (),2);
			e.getPlayer ().inventory.markDirty ();
			ChatManager.sendMessage (e.getPlayer (),Local.CLAIM_PLACE.replaceAll ("#", getOwner (claim)));
		}
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if(claim != null && !ChunkManager.canDestroy (claim,e.getEntityPlayer ().getGameProfile ().getId ())) {
			if(!ChunkManager.isItemSafe (e.getItemStack ())) {
				e.setCanceled (true);
				ChatManager.sendMessage (e.getEntityPlayer (),Local.CLAIM_INTERACT.replaceAll ("#",getOwner (claim)));
			}
		}
	}

	@SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if(claim != null && !ChunkManager.canDestroy (claim,e.getEntityPlayer ().getGameProfile ().getId ())) {
			if(!ChunkManager.isItemSafe (e.getItemStack ())) {
				e.setCanceled (true);
				ChatManager.sendMessage (e.getEntityPlayer (),Local.CLAIM_INTERACT.replaceAll ("#",getOwner (claim)));
			}
		}
	}

	@SubscribeEvent
	public void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if(claim != null && !ChunkManager.canDestroy (claim,e.getEntityPlayer ().getGameProfile ().getId ())) {
			if(!ChunkManager.isItemSafe (e.getItemStack ())) {
				e.setCanceled (true);
				ChatManager.sendMessage (e.getEntityPlayer (),Local.CLAIM_INTERACT.replaceAll ("#",getOwner (claim)));
			}
		}
	}

	@SubscribeEvent
	public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if(claim != null && !ChunkManager.canDestroy (claim,e.getEntityPlayer ().getGameProfile ().getId ())) {
			if(!ChunkManager.isItemSafe (e.getItemStack ())) {
				e.setCanceled (true);
				ChatManager.sendMessage (e.getEntityPlayer (),Local.CLAIM_INTERACT.replaceAll ("#",getOwner (claim)));
			}
		}
	}

	@SubscribeEvent
	public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e) {
		Claim claim = ChunkManager.getClaim (e.getPos ());
		if(claim != null && !ChunkManager.canDestroy (claim,e.getEntityPlayer ().getGameProfile ().getId ())) {
			if(!ChunkManager.isItemSafe (e.getItemStack ())) {
				e.setCanceled (true);
				ChatManager.sendMessage (e.getEntityPlayer (),Local.CLAIM_INTERACT.replaceAll ("#",getOwner (claim)));
			}
		}
	}

	public static String getOwner (Claim claim) {
		if (claim != null && claim.getTeam () != null) {
			return claim.getTeam ().getName ();
		} else if (claim != null && claim.getOwner () != null)
			return UsernameCache.getLastKnownUsername (claim.getOwner ());
		return "Unknown";
	}
}
