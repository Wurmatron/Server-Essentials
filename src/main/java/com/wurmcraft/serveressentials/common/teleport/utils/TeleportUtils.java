package com.wurmcraft.serveressentials.common.teleport.utils;

import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.api.module.DelayedTeleport;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.teleport.events.TeleportEvents;
import com.wurmcraft.serveressentials.common.utils.RankUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.block.BlockAir;
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

  private TeleportUtils() {}

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
              location.getY() + 1d,
              location.getZ() + 0.5,
              player.rotationYaw,
              player.rotationPitch);
          newWorld.updateEntityWithOptionalForce(player, false);
          player.setWorld(newWorld);
        }
        player.mcServer.getPlayerList().preparePlayer(player, oldWorld);
        player.connection.setPlayerLocation(
            location.getX() + 0.5,
            location.getY() + 1d,
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
    if (!isWithinBorder(location)) {
      ChatHelper.sendMessage(
          player, LanguageModule.getLangfromUUID(player.getGameProfile().getId()).TP_WORLDBORDER);
    }
    return true;
  }

  public static boolean teleportTo(EntityPlayer teleporter, EntityPlayer toPlayer) {
    return teleportTo(teleporter, toPlayer, false);
  }

  public static boolean teleportTo(
      EntityPlayer teleporter, EntityPlayer toPlayer, boolean bypassDelay) {
    if (ConfigHandler.teleportDelay > 0 && !bypassTeleportCooldown(teleporter) && !bypassDelay) {
      TeleportationModule.teleportDelay.put(
          System.currentTimeMillis(),
          new DelayedTeleport(
              System.currentTimeMillis(), ConfigHandler.teleportDelay, teleporter, toPlayer));
      TeleportEvents.playerPositionTracker.put(teleporter, teleporter.getPosition());
      return true;
    } else {
      return teleportTo(
          (EntityPlayerMP) teleporter,
          new LocationWrapper(toPlayer.posX, toPlayer.posY, toPlayer.posZ, toPlayer.dimension),
          true);
    }
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
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      return user.hasPerk("teleport.noCooldown") || user.hasPerk("teleport.*");
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData user = (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      return RankUtils.hasPerk("teleport.noCooldown", user.getCustomData())
          || RankUtils.hasPerk("teleport.*", user.getCustomData());
    }
    return false;
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
    if (ConfigHandler.safeTeleport) {
      return true;
    }
    World world = player.world;
    if (world.getBlockState(location.getPos().up()).getBlock() instanceof BlockAir
        && world.getBlockState(location.getPos()).getBlock() instanceof BlockAir) {
      return !(world.getBlockState(location.getPos().down()).getBlock() instanceof BlockAir)
          && !(world.getBlockState(location.getPos().down()).getBlock() instanceof IFluidBlock);
    }
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

  public static boolean handleDelayedTeleport(DelayedTeleport tp) {
    if ((tp.startTime + tp.time) <= System.currentTimeMillis()) {
      return teleportTo(tp.A, tp.B, true);
    }
    return false;
  }
}
