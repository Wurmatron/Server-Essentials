package com.wurmcraft.serveressentials.common.protect;

import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.protection.Town;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.protect.events.PlayerTrackerEvents;
import com.wurmcraft.serveressentials.common.protect.events.ProtectionEvents;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import java.io.File;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Protection")
public class ProtectionModule implements IModule {

  private static NonBlockingHashMap<String, TownCache> townNameCache = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<String, Town> townOwnerCache = new NonBlockingHashMap<>();

  @Override
  public void setup() {
    loadAllTowns();
    MinecraftForge.EVENT_BUS.register(new ProtectionEvents());
    MinecraftForge.EVENT_BUS.register(new PlayerTrackerEvents());
  }

  private void loadAllTowns() {
    if (!new File(ConfigHandler.saveLocation + File.separator + Keys.TOWN.name()).exists()) {
      new File(ConfigHandler.saveLocation + File.separator + Keys.TOWN.name()).mkdirs();
    }
    Arrays.stream(
            new File(ConfigHandler.saveLocation + File.separator + Keys.TOWN.name()).listFiles())
        .filter(file -> file.getName().endsWith(".json"))
        .forEach(
            file -> {
              Town town = DataHelper.load(file, Keys.TOWN, new Town());
              townNameCache.put(town.getID(), new TownCache(town));
              townOwnerCache.put(town.getOwnerID(), town);
            });
  }

  public static boolean isAreaClaimed(BlockPos pos) {
    return getTownForPos(pos) != null;
  }

  public static boolean hasPermission(EntityPlayer player, BlockPos pos) {
    Town town = getTownForPos(pos);
    if (town != null && town.getOwnerID().equals(player.getGameProfile().getId().toString()))
      return true;
    return false;
  }

  public static Town getTownForPos(BlockPos pos) {
    for (TownCache cache : townNameCache.values()) {
      if (cache.isAreaClaimed(pos)) {
        return cache.town;
      }
    }
    return null;
  }

  public static boolean createTown(Town town) {
    if (!doesTownNameExist(town.getID())) {
      DataHelper.forceSave(Keys.TOWN, town);
      townNameCache.putIfAbsent(town.getID(), new TownCache(town));
      townOwnerCache.putIfAbsent(town.getOwnerID(), town);
      return true;
    }
    return false;
  }

  public static boolean doesTownNameExist(String name) {
    return townOwnerCache.keySet().contains(name);
  }

  public static TownCache getTownFromName(String name) {
    return townNameCache.get(name);
  }

  public static Town getTownFromOwner(String ownerID) {
    return townOwnerCache.get(ownerID);
  }

  public static void updateTown(Town town) {
    DataHelper.forceSave(Keys.TOWN, town);
    townNameCache.remove(town.getID());
    townNameCache.put(town.getID(), new TownCache(town));
    townOwnerCache.remove(town.getOwnerID());
    townOwnerCache.put(town.getOwnerID(), town);
  }
}
