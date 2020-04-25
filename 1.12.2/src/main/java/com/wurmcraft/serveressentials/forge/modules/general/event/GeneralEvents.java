package com.wurmcraft.serveressentials.forge.modules.general.event;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GeneralEvents {

  @SubscribeEvent
  public void onDeath(LivingDeathEvent e) {
    if (e.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) e.getEntityLiving();
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(player), "general.back.death") || !SERegistry
          .isModuleLoaded("Rank")) {
        try {
          StoredPlayer playerData = (StoredPlayer) SERegistry
              .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
          playerData.server.lastLocation = new LocationWrapper(player.posX, player.posY,
              player.posZ, player.dimension);
          PlayerDataEvents.savePlayer(player);
        } catch (NoSuchElementException f) {

        }
      }
    }
  }

}
