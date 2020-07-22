package com.wurmcraft.serveressentials.forge.modules.chunkloading;

import static com.wurmcraft.serveressentials.forge.modules.chunkloading.ChunkLoadingUtils.EXECUTOR_SERVICE;
import static com.wurmcraft.serveressentials.forge.modules.chunkloading.ChunkLoadingUtils.updateTickets;

import com.wurmcraft.serveressentials.core.api.module.Module;
import java.util.concurrent.TimeUnit;

@Module(name = "ChunkLoading", moduleDependencies = "Economy")
public class ChunkLoadingModule {

  public void initSetup() {

  }

  public void finalizeModule() {
    try {
      EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
        try {
          updateTickets(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }, 1, 5, TimeUnit.MINUTES);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
