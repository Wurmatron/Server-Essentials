package com.wurmcraft.serveressentials.forge.common;


import com.wurmcraft.serveressentials.core.Global;
import com.wurmcraft.serveressentials.core.SECore;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
}
