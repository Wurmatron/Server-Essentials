package com.wurmcraft.serveressentials.common.protect;

import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.protection.Town;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.util.math.BlockPos;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Protection")
public class ProtectionModule implements IModule {

  private static NonBlockingHashMap<String, TownCache> townNameCache = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<String, Town> townOwnerCache = new NonBlockingHashMap<>();

  @Override
  public void setup() {
    loadAllTowns();
  }

  private void loadAllTowns() {
    Arrays.stream(
            Objects.requireNonNull(
                new File(ConfigHandler.saveLocation + File.separator + Keys.TOWN.name())
                    .listFiles()))
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

  public static Town getTownForPos(BlockPos pos) {
    for (TownCache cache : townNameCache.values()) {
      if (cache.isAreaClaimed(pos)) {
        return cache;
      }
    }
    return null;
  }

  public static boolean createTown(Town town) {
    if (doesTownNameExist(town.getID())) {
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
}
