package com.wurmcraft.serveressentials.common;

import static com.wurmcraft.serveressentials.api.ServerEssentialsAPI.modules;

import com.wurmcraft.serveressentials.api.json.global.Global;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.utils.CommandLoader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
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

  public static Logger logger = LogManager.getLogger(Global.NAME);

  private static List<IModule> loadModules(ASMDataTable asmData) {
    logger.info("Loading Modules");
    List<IModule> activeModules = new ArrayList<>();
    for (ASMData data : asmData.getAll(Module.class.getName())) {
      try {
        Class<?> asmClass = Class.forName(data.getClassName());
        Class<? extends IModule> moduleClass = asmClass.asSubclass(IModule.class);
        IModule module = moduleClass.newInstance();
        String name = asmClass.getAnnotation(Module.class).name();
        for (String activeModule : ConfigHandler.modules) {
          if (name.equalsIgnoreCase(activeModule)) {
            activeModules.add(module);
            logger.info("Loading Module '" + name + "'");
          } else {
            logger.debug("Prevented module '" + name + "' from loading due to config");
          }
        }
      } catch (Exception e) {
        logger.error(e.getLocalizedMessage());
      }
    }
    return activeModules;
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    logger.info("Starting PreInit");
    modules = loadModules(e.getAsmData());
    CommandLoader.locateCommands(e.getAsmData());
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    logger.info("Starting Init");
    for (IModule module : modules) {
      module.setup();
    }
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    logger.info("Starting PostInit");
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    logger.info("Server Starting");
    CommandLoader.registerCommands(e);
  }

  @EventHandler
  public void serverStopping(FMLServerStoppingEvent e) {
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      player.connection.disconnect(
          new TextComponentString(ConfigHandler.shutdownMessage.replaceAll("&", "\u00A7")));
    }
  }
}
