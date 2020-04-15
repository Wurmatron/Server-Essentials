package com.wurmcraft.serveressentials.forge.common;


import com.wurmcraft.serveressentials.core.Global;
import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, serverSideOnly = true)
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
    SECore.logger.info("Starting FML-ServerStarting");
    for (String n : SERegistry.getLoadedCommands()) {
      Object commandInstance = SERegistry.getCommand(n);
      e.registerServerCommand(new SECommand(commandInstance.getClass().getAnnotation(
          ModuleCommand.class), commandInstance));
    }
  }
}
