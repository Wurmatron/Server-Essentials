package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.teleport.TpaCommand;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import java.util.HashMap;

public class PlayerTickEvent {

	private static HashMap <EntityPlayer, PlayerInventory> openInv = new HashMap <> ();
	private static HashMap <EntityPlayer, BlockPos> frozenPlayers = new HashMap <> ();
	private static HashMap <EntityPlayer, ChunkPos> playerChunkLoc = new HashMap <> ();

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
		if (ConfigHandler.claimingEnabled && chunkChanged (e.player)) {
			String name = getClaimName (e.player);
			if (name != null)
				e.player.sendMessage (new TextComponentString (TextFormatting.RED + name));
			playerChunkLoc.put (e.player,new ChunkPos (e.player.getPosition ()));
		} else if (!playerChunkLoc.containsKey (e.player))
			playerChunkLoc.put (e.player,new ChunkPos (e.player.getPosition ()));
	}

	private boolean chunkChanged (EntityPlayer player) {
		if (playerChunkLoc.containsKey (player))
			return !(playerChunkLoc.get (player).x == ((int) player.posX >> 4) && playerChunkLoc.get (player).z == ((int) player.posZ >> 4));
		return false;
	}

	private String getClaimName (EntityPlayer player) {
		Claim oldClaim = ChunkManager.getClaim (playerChunkLoc.get (player).getBlock (8,0,8));
		Claim claim = ChunkManager.getClaim (player.getPosition ());
		if (oldClaim != null && claim != null) {
			if (oldClaim.getTeam () != null && claim.getTeam () != null && !claim.getTeam ().getName ().equalsIgnoreCase (oldClaim.getTeam ().getName ()))
				return claim.getTeam ().getName ();
			else if (oldClaim.getOwner () != null && claim.getOwner () != null && !oldClaim.getOwner ().equals (claim.getOwner ()))
				return UsernameResolver.getUsername (claim.getOwner ());
		} else if (claim != null) {
			if (claim.getTeam () != null)
				return claim.getTeam ().getName ();
			else if (claim.getOwner () != null)
				return UsernameResolver.getUsername (claim.getOwner ());
		}
		if (oldClaim != null && claim == null)
			return "Wild";
		return null;
	}

}
