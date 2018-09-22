package com.wurmcraft.serveressentials.common.teleport.utils;

import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportUtils {

  public static boolean teleportTo(
      EntityPlayerMP player, LocationWrapper location, boolean teleportTimer) {
    return teleportTo(player, location, teleportTimer, true);
  }

  public static boolean teleportTo(
      EntityPlayerMP player, LocationWrapper location, boolean teleportTimer, boolean checkSafe) {
    if (canTeleport(player) && location != null) {
      if (checkSafe && !safeToTeleport(player, location)) {
        ChatHelper.sendMessage(
            player,
            LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                .TPA_NOTSAFE
                .replaceAll(
                    "%PLAYER%",
                    "[" + location.getX() + ", " + location.getY() + ", " + location.getZ() + "]"));
        return false;
      }
      setLastLocation(player, player.getPosition());
      player.connection.setPlayerLocation(
          location.getX(),
          location.getY(),
          location.getZ(),
          player.rotationYaw,
          player.rotationPitch);
      if (player.dimension != location.getDim()) {
        int id = player.dimension;
        WorldServer oldWorld = player.mcServer.getWorld(player.dimension);
        player.dimension = location.getDim();
        WorldServer newWorld = player.mcServer.getWorld(player.dimension);
        player.connection.sendPacket(
            new SPacketRespawn(
                player.dimension,
                player.world.getDifficulty(),
                newWorld.getWorldInfo().getTerrainType(),
                player.interactionManager.getGameType()));
        oldWorld.removeEntityDangerously(player);
        player.isDead = false;
        if (player.isEntityAlive()) {
          newWorld.spawnEntity(player);
          player.setLocationAndAngles(
              location.getX() + 0.5,
              location.getY() + 1,
              location.getZ() + 0.5,
              player.rotationYaw,
              player.rotationPitch);
          newWorld.updateEntityWithOptionalForce(player, false);
          player.setWorld(newWorld);
        }
        player.mcServer.getPlayerList().preparePlayer(player, oldWorld);
        player.connection.setPlayerLocation(
            location.getX() + 0.5,
            location.getY() + 1,
            location.getZ() + 0.5,
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
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, id, location.getDim());
      }
      if (teleportTimer) {
        setTeleportTimer(player);
      }
    }
    return true;
  }

  public static boolean teleportTo(EntityPlayer teleporter, EntityPlayer toPlayer) {
    return teleportTo(
        (EntityPlayerMP) teleporter,
        new LocationWrapper(toPlayer.posX, toPlayer.posY, toPlayer.posZ, toPlayer.dimension),
        true);
  }

  private static boolean canTeleport(EntityPlayer player) {
    if (bypassTeleportCooldown(player)) {
      return true;
    }
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      return data.getTeleportTimer() + (ConfigHandler.teleportTimer * 1000)
          <= System.currentTimeMillis();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser data = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      return data.getTeleportTimer() + (ConfigHandler.teleportTimer * 1000)
          <= System.currentTimeMillis();
    }
    return false;
  }

  public static void setLastLocation(EntityPlayer player, BlockPos pos) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      data.setLastLocation(new LocationWrapper(pos, player.dimension));
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser data = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      data.setLastLocation(new LocationWrapper(pos, player.dimension));
    }
  }

  private static boolean bypassTeleportCooldown(EntityPlayer player) {
    GlobalUser user = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
    return user.hasPerk("teleport.noCooldown") || user.hasPerk("teleport.*");
  }

  private static void setTeleportTimer(EntityPlayer player) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player)[0];
      data.setTeleportTimer(System.currentTimeMillis());
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser data = (LocalUser) UserManager.getPlayerData(player)[1];
      data.setTeleportTimer(System.currentTimeMillis());
      DataHelper.forceSave(Keys.LOCAL_USER, data);
    }
  }

  public static boolean safeToTeleport(EntityPlayer player, LocationWrapper location) {
    World world = player.world;
    boolean suffCheck =
        world.getBlockState(location.getPos()) == Blocks.AIR.getDefaultState()
            && world.getBlockState(location.getPos().up()) == Blocks.AIR.getDefaultState();
    return suffCheck && !isFluidBelow(world, location);
  }

  private static boolean isFluidBelow(World world, LocationWrapper loc) {
    for (int y = 0; y < 10; y++) {
      if (world.getBlockState(loc.getPos()).getBlock() instanceof IFluidBlock) return true;
      else if (world.getBlockState(loc.getPos()).getBlock() != Blocks.AIR
          && !(world.getBlockState(loc.getPos()).getBlock() instanceof IFluidBlock)) {
        return false;
      }
      return true;
    }

    // TODO CC Support
    //    if (world.getBlockState(world.getTopSolidOrLiquidBlock(loc.getPos())).getBlock() != Blocks.AIR
    //        && !(world.getBlockState(world.getTopSolidOrLiquidBlock(loc.getPos())).getBlock()
    //            instanceof IFluidBlock)) {
    //      return true;
    //    }
    return false;
  }
}
