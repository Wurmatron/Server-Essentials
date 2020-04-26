package com.wurmcraft.serveressentials.forge.common;


import com.wurmcraft.serveressentials.core.Global;
import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.track.event.TrackEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ServerEssentialsServer {

  @EventHandler
  public void preInitialization(FMLPreInitializationEvent e) {
    SECore.logger.info("Starting FML-PreInitialization");
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    SECore.logger.info("Starting FML-Initialization");
    SECore.setup();
  }

  @EventHandler
  public void postInitialization(FMLPostInitializationEvent e) {
    SECore.logger.info("Starting FML-PostInitialization");
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    for (String c : SERegistry.getLoadedCommands()) {
      Object command = SERegistry.getCommand(c);
      e.registerServerCommand(new SECommand(command.getClass().getAnnotation(
          ModuleCommand.class), command));
    }
  }

  @EventHandler
  public void serverStopping(FMLServerStoppingEvent e) {
    for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getPlayerList().getPlayers()) {
      PlayerDataEvents.savePlayer(p);
      if (SERegistry.isModuleLoaded("Track")) {
        TrackEvents.updatePlayerTracking(p);
      }
    }
  }
}
