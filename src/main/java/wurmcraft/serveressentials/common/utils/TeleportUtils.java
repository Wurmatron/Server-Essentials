package wurmcraft.serveressentials.common.utils;

import net.minecraft.block.BlockLiquid;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.commands.teleport.TpaCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Keys;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class TeleportUtils {

	public static void teleportTo (EntityPlayer player,BlockPos pos,boolean timer) {
		if (player instanceof EntityPlayerMP && pos != null) {
			PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
			data.setLastLocation (new BlockPos (player.posX,player.posY,player.posZ));
			doTeleport ((EntityPlayerMP) player,pos.getX (),pos.getY (),pos.getZ (),player.dimension);
			if (timer)
				data.setTeleportTimer (System.currentTimeMillis ());
		}
	}

	public static void teleportTo (EntityPlayer player,BlockPos pos,int dim,boolean timer) {
		if (player != null && pos != null) {
			PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
			data.setLastLocation (new BlockPos (player.posX,player.posY,player.posZ));
			if (player.dimension != dim)
				player.changeDimension (dim);
			doTeleport ((EntityPlayerMP) player,pos.getX (),pos.getY (),pos.getZ (),dim);
			if (timer)
				data.setTeleportTimer (System.currentTimeMillis ());
		}
	}

	public static void doTeleport (EntityPlayerMP player,double x,double y,double z,int dimID) {
		if (player.dimension != dimID) {
			int id = player.dimension;
			WorldServer oldWorld = player.mcServer.getWorld (player.dimension);
			player.dimension = dimID;
			WorldServer newWorld = player.mcServer.getWorld (player.dimension);
			player.connection.sendPacket (new SPacketRespawn (player.dimension,player.world.getDifficulty (),newWorld.getWorldInfo ().getTerrainType (),player.interactionManager.getGameType ()));
			oldWorld.removeEntityDangerously (player);
			player.isDead = false;
			if (player.isEntityAlive ()) {
				newWorld.spawnEntity (player);
				player.setLocationAndAngles (x + 0.5,y + 1,z + 0.5,player.rotationYaw,player.rotationPitch);
				newWorld.updateEntityWithOptionalForce (player,false);
				player.setWorld (newWorld);
			}
			player.mcServer.getPlayerList ().preparePlayer (player,oldWorld);
			player.connection.setPlayerLocation (x + 0.5,y + 1,z + 0.5,player.rotationYaw,player.rotationPitch);
			player.interactionManager.setWorld (newWorld);
			player.mcServer.getPlayerList ().updateTimeAndWeatherForPlayer (player,newWorld);
			player.mcServer.getPlayerList ().syncPlayerInventory (player);
			for (PotionEffect potioneffect : player.getActivePotionEffects ())
				player.connection.sendPacket (new SPacketEntityEffect (player.getEntityId (),potioneffect));
			player.connection.sendPacket (new SPacketSetExperience (player.experience,player.experienceTotal,player.experienceLevel)); // Force XP sync
			FMLCommonHandler.instance ().firePlayerChangedDimensionEvent (player,id,dimID);
		} else
			player.connection.setPlayerLocation (x + 0.5,y + 1,z + 0.5,player.rotationYaw,player.rotationPitch);
	}

	public static String getRemainingCooldown (UUID uuid) {
		return getRemainingCooldown (UsernameResolver.getPlayerData (uuid).getTeleportTimer ());
	}

	public static String getRemainingCooldown (long playerTimer) {
		return Integer.toString (Math.round ((System.currentTimeMillis () - playerTimer)) / 1000);
	}

	public static boolean canTeleport (UUID uuid) {
		if (uuid != null) {
			PlayerData data = UsernameResolver.getPlayerData (uuid);
			return data.getTeleportTimer () + (Settings.teleport_cooldown * 1000) <= System.currentTimeMillis ();
		}
		return false;
	}

	public static boolean addTeleport (EntityPlayer player,EntityPlayer other) {
		if (TpaCommand.activeRequests.size () > 0) {
			for (EntityPlayer[] players : TpaCommand.activeRequests.values ())
				if (!players[0].getGameProfile ().getId ().equals (player.getGameProfile ().getId ())) {
					TpaCommand.activeRequests.put (System.currentTimeMillis (),new EntityPlayer[] {player,other});
					return true;
				}
		} else {
			TpaCommand.activeRequests.put (System.currentTimeMillis (),new EntityPlayer[] {player,other});
			return true;
		}
		return false;
	}

	public static boolean safeLocation (World world,BlockPos pos) {
		return world.getBlockState (pos.down ()).getBlock () != Blocks.AIR && !(world.getBlockState (pos.down ()).getBlock () instanceof BlockLiquid) && world.getBlockState (pos).getBlock () == Blocks.AIR;
	}
}
