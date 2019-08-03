package com.wurmcraft.serveressentials.common;

import static com.wurmcraft.serveressentials.api.ServerEssentialsAPI.commands;
import static com.wurmcraft.serveressentials.api.ServerEssentialsAPI.modules;
import static com.wurmcraft.serveressentials.api.ServerEssentialsAPI.storage;
import static com.wurmcraft.serveressentials.api.ServerEssentialsAPI.storageType;
import static com.wurmcraft.serveressentials.common.storage.StorageUtils.getActiveStorageType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.common.modules.track.TrackModule;
import com.wurmcraft.serveressentials.common.reference.Global;
import com.wurmcraft.serveressentials.common.storage.StorageUtils;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.utils.AnnotationLoader;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
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

  private List<Object> activeModules = new ArrayList<>();
  public ScheduledExecutorService executors;
  public final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    LOGGER.info("Starting PreInit");
    // Load Commands / Modules
    activeModules = AnnotationLoader.loadModules(e.getAsmData());
    modules = AnnotationLoader.moduleListToName(activeModules);
    commands = AnnotationLoader.loadCommands(e.getAsmData());
    executors = Executors.newScheduledThreadPool(ConfigHandler.maxProcessingThreads);
    // Setup Storage Type
    storageType = getActiveStorageType();
    storage = StorageUtils.setupStorage();
    storage.setup();
    // Init Modules
    AnnotationLoader.initModules(activeModules);
    if (ServerEssentialsAPI.isModuleLoaded("Track")) {
      RequestGenerator.Status.syncServer(TrackModule.createStatus("PreInit"));
    }
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    LOGGER.info("Starting Init");
    if (ServerEssentialsAPI.isModuleLoaded("Track")) {
      RequestGenerator.Status.syncServer(TrackModule.createStatus("Init"));
    }
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    LOGGER.info("Starting PostInit");
    if (ServerEssentialsAPI.isModuleLoaded("Track")) {
      RequestGenerator.Status.syncServer(TrackModule.createStatus("PostInit"));
    }
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    LOGGER.info("Server Starting");
    CommandUtils.generateListOfCommandWrappers(commands).forEach(e::registerServerCommand);
    if (ServerEssentialsAPI.isModuleLoaded("Track")) {
      RequestGenerator.Status.syncServer(TrackModule.createStatus("Server Starting"));
    }
  }

  @EventHandler
  public void serverStarted(FMLServerStartedEvent e) {
    if (ServerEssentialsAPI.isModuleLoaded("Track")) {
      RequestGenerator.Status.syncServer(TrackModule.createStatus("Online"));
      TrackModule.startStatusUpdater();
    }
  }

  @EventHandler
  public void serverStopping(FMLServerStoppingEvent e) {
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      player.connection.disconnect(
          new TextComponentString(ConfigHandler.shutdownMessage.replaceAll("&", "\u00A7")));
    }
    if (ServerEssentialsAPI.isModuleLoaded("Track")) {
      RequestGenerator.Status.syncServer(TrackModule.createStatus("Stopped"));
    }
  }
}
