package com.wurmcraft.serveressentials.common.modules.general.event;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.utils.wrapper.PlayerInventory;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GeneralEvents {

  private static HashMap<EntityPlayer, PlayerInventory> openInv = new HashMap<>();
  private static HashMap<EntityPlayer, BlockPos> frozenPlayers = new HashMap<>();

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    if (ConfigHandler.motdOnJoin) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getCommandManager()
          .executeCommand(e.player, "/motd");
    }
  }

  public static void register(PlayerInventory inv) {
    openInv.put(inv.owner, inv);
  }

  public static void remove(PlayerInventory inv) {
    openInv.remove(inv.owner, inv);
  }

  public static void addFrozen(EntityPlayer player, BlockPos pos) {
    if (!frozenPlayers.keySet().contains(player)) {
      player.capabilities.disableDamage = true;
      frozenPlayers.put(player, pos);
      setFrozen(player.getGameProfile().getId(), true);
    }
  }

  public static void removeFrozen(EntityPlayer player) {
    if (frozenPlayers.size() > 0 && frozenPlayers.keySet().contains(player)) {
      frozenPlayers.remove(player);
      player.capabilities.disableDamage = false;
      setFrozen(player.getGameProfile().getId(), false);
    }
  }

  private static void setFrozen(UUID uuid, boolean frozen) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser data = (FileUser) UserManager.getUserData(uuid)[0];
      data.setFrozen(frozen);
      DataHelper.save(Storage.USER, data);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      LocalRestUser local = (LocalRestUser) UserManager.getUserData(uuid)[1];
      local.setFrozen(frozen);
      DataHelper.save(Storage.LOCAL_USER, local);
    }
  }

  public static void toggleFrozen(EntityPlayer player, BlockPos pos) {
    if (frozenPlayers.keySet().contains(player)) {
      removeFrozen(player);
    } else {
      addFrozen(player, pos);
    }
  }

  public static boolean isFrozen(EntityPlayer player) {
    return frozenPlayers.keySet().contains(player);
  }

  @SubscribeEvent
  public void tickStart(TickEvent.PlayerTickEvent e) {
    if (openInv.size() > 0 && openInv.containsKey(e.player)) {
      openInv.get(e.player).update();
    }
    if (frozenPlayers.size() > 0 && frozenPlayers.keySet().contains(e.player)) {
      BlockPos lockedPos = frozenPlayers.get(e.player);
      if (e.player.getPosition() != lockedPos) {
        e.player.setPositionAndUpdate(lockedPos.getX(), lockedPos.getY(), lockedPos.getZ());
      }
    }
  }
}
