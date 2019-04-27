package com.wurmcraft.serveressentials.common;

import com.wurmcraft.serveressentials.common.reference.Global;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
  modid = Global.MODID,
  name = Global.NAME,
  version = Global.VERSION,
  serverSideOnly = true,
  acceptableRemoteVersions = "*"
)
public class ServerEssentialsServer {

  @Mod.Instance(Global.MODID)
  public static ServerEssentialsServer instance;

  @SidedProxy(serverSide = Global.COMMON_PROXY, clientSide = Global.CLIENT_PROXY)
  public static CommonProxy proxy;

  public static final Logger LOGGER = LogManager.getLogger(Global.NAME);

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    LOGGER.info("Starting PreInit");
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    LOGGER.info("Starting Init");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    LOGGER.info("Starting PostInit");
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    LOGGER.info("Server Starting");
  }

  @EventHandler
  public void serverStopping(FMLServerStoppingEvent e) {}
}
