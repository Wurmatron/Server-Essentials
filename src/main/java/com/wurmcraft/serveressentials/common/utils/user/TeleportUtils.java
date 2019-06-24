package com.wurmcraft.serveressentials.common.utils.user;

import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportUtils {

  public boolean isSafe(World world, LocationWrapper wrapper) {
    return true;
  }

  public static boolean teleportTo(
      EntityPlayerMP player, LocationWrapper location, boolean requiresTimer, boolean safeCheck) {
    if (!canTeleport(player) || location != null) {
      return false;
    }
    if (safeCheck && !safeToTeleport(player, location)) {
      return false;
    }
    if (!isWithinBorder(location) && !UserManager.hasPerm(player, "teleportation.bypass")) {
      return false;
    }
    UserManager.setLastLocation(player, location);
    if (player.dimension != location.getDim()) {
      int saveDim = player.dimension;
      WorldServer newWorld = player.mcServer.getWorld(location.getDim());
      player.dimension = location.getDim();
      player.connection.sendPacket(
          new SPacketRespawn(
              location.getDim(),
              newWorld.getDifficulty(),
              newWorld.getWorldInfo().getTerrainType(),
              player.interactionManager.getGameType()));
      player.mcServer.getWorld(saveDim).removeEntityDangerously(player);
      player.isDead = false;
      if (player.isEntityAlive()) {
        newWorld.spawnEntity(player);
        player.setLocationAndAngles(
            location.getX() + .5,
            location.getY() + 1,
            location.getZ() + .5,
            player.rotationYaw,
            player.rotationPitch);
        newWorld.updateEntityWithOptionalForce(player, false);
        player.setWorld(newWorld);
      }
      player.mcServer.getPlayerList().preparePlayer(player, player.mcServer.getWorld(saveDim));
      player.connection.setPlayerLocation(
          location.getX(),
          location.getY(),
          location.getZ(),
          player.rotationYaw,
          player.rotationPitch);
      player.interactionManager.setWorld(newWorld);
      player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, newWorld);
      player.mcServer.getPlayerList().syncPlayerInventory(player);
      for (PotionEffect potioneffect : player.getActivePotionEffects()) {
        player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
      }
      player.connection.sendPacket(
          new SPacketSetExperience(
              player.experience, player.experienceTotal, player.experienceLevel));
      FMLCommonHandler.instance()
          .firePlayerChangedDimensionEvent(player, saveDim, location.getDim());
      if (requiresTimer) {
        updateTeleportTimer(player);
      }
    }
    return true;
  }

  private static boolean canTeleport(EntityPlayer player) {
    if (UserManager.hasPerm(player, "teleportation.cooldown")) {
      return true;
    } else {
      return UserManager.getLastTeleport(player) + (ConfigHandler.teleportTimer * 1000)
          <= System.currentTimeMillis();
    }
  }

  private static boolean safeToTeleport(EntityPlayer player, LocationWrapper wrapper) {
    World world = player.world;
    // Check Player Location
    if (!world.isAirBlock(wrapper.getPos()) || world.isAirBlock(wrapper.getPos().add(0, 1, 0))) {
      // Check Player Location
      return false;
    }
    // Check for a valid standing block
    if (world.getBlockState(wrapper.getPos().down()).getBlock() instanceof IFluidBlock
        || world.isAirBlock(wrapper.getPos().down())
            && world.isAirBlock(wrapper.getPos().down().down())) {
      return false;
    }
    // Check for a small spawn area of air (3x3)
    for (int x = 0; x < 3; x++) {
      for (int z = 0; z < 3; z++) {
        for (int y = 0; y < 2; y++) {
          if (!world.isAirBlock(wrapper.getPos().add(x, y, z))
              || !world.isAirBlock(wrapper.getPos().add(-x, y, z))
                  && !world.isAirBlock(wrapper.getPos().add(x, y, -z))
              || !world.isAirBlock(wrapper.getPos().add(-x, y, -z))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private static boolean updateTeleportTimer(EntityPlayer player) {
    return false;
  }

  public static boolean isWithinBorder(LocationWrapper location) {
    WorldBorder border =
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getWorld(location.getDim())
            .getWorldBorder();
    BlockPos pos = location.getPos();
    return border.minX() <= pos.getX()
        && border.maxX() >= pos.getX()
        && border.minZ() <= pos.getZ()
        && border.maxZ() >= pos.getZ();
  }
}
