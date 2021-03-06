package com.wurmcraft.serveressentials.forge.common;


import static com.wurmcraft.serveressentials.forge.modules.matterlink.event.MatterLinkTickEvent.DATE_FORMAT;

import com.wurmcraft.serveressentials.core.Global;
import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.track.TrackingStatus.Status;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.api.command.WrapperCommand;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.matterlink.MatterLinkModule;
import com.wurmcraft.serveressentials.forge.modules.matterlink.utils.MatterBridgeUtils;
import com.wurmcraft.serveressentials.forge.modules.matterlink.utils.json.RestMessage;
import com.wurmcraft.serveressentials.forge.modules.track.TrackUtils;
import com.wurmcraft.serveressentials.forge.modules.track.event.TrackEvents;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ServerEssentialsServer {

  @Instance(value = Global.MODID)
  public static ServerEssentialsServer INSTANCE;
  public static Logger logger;

  @EventHandler
  public void preInitialization(FMLPreInitializationEvent e) {
    logger = LogManager.getLogger(Global.NAME);
    logger.info("Starting FML-PreInitialization");
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    ServerEssentialsServer.logger.info("Starting FML-Initialization");
    SECore.setup();
    if (SERegistry.isModuleLoaded("Track") && SERegistry.globalConfig.dataStorgeType
        .equalsIgnoreCase("Rest")) {
      RestRequestGenerator.Track.updateTrack(TrackUtils.createStatus(Status.INIT));
    }
  }

  @EventHandler
  public void postInitialization(FMLPostInitializationEvent e) {
    logger.info("Starting FML-PostInitialization");
    if (SERegistry.isModuleLoaded("Track") && SERegistry.globalConfig.dataStorgeType
        .equalsIgnoreCase("Rest")) {
      RestRequestGenerator.Track.updateTrack(TrackUtils.createStatus(Status.POSTINIT));
    }
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    for(ICommand c : FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().getCommands().values()) {
      FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().getCommands().put(c.getName(),new WrapperCommand(c));
    }
    for (String c : SERegistry.getLoadedCommands()) {
      Object command = SERegistry.getCommand(c);
      e.registerServerCommand(new SECommand(command.getClass().getAnnotation(
          ModuleCommand.class), command));
    }
    if (SERegistry.isModuleLoaded("Track") && SERegistry.globalConfig.dataStorgeType
        .equalsIgnoreCase("Rest")) {
      RestRequestGenerator.Track.updateTrack(TrackUtils.createStatus(Status.STARTING));
    }
  }

  @EventHandler
  public void serverStarted(FMLServerStartedEvent e) {
    if (SERegistry.isModuleLoaded("Track") && SERegistry.globalConfig.dataStorgeType
        .equalsIgnoreCase("Rest")) {
      SECore.executors.scheduleAtFixedRate(() -> RestRequestGenerator.Track
              .updateTrack(TrackUtils.createStatus(Status.ONLINE)), 0, 90,
          TimeUnit.SECONDS);
    }
    if (SERegistry.isModuleLoaded("MatterLink")
        && MatterBridgeUtils.config.displayServerStatus) {
      RestMessage msg = new RestMessage("", "", MatterBridgeUtils.config.gateway,
          "Server has Started", "", MatterBridgeUtils.config.account, "", "", "",
          MatterBridgeUtils.config.protocol, DATE_FORMAT.format(new Date()), "", null);
      if (MatterBridgeUtils.sendMessage(msg) != 200) {
        ServerEssentialsServer.logger.warn("Failed to send message to bridge");
      }
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
    if (SERegistry.isModuleLoaded("Track") && SERegistry.globalConfig.dataStorgeType
        .equalsIgnoreCase("Rest")) {
      RestRequestGenerator.Track.updateTrack(TrackUtils.createStatus(Status.STOPPED));
    }
    if (SERegistry.isModuleLoaded("MatterLink")
        && MatterBridgeUtils.config.displayServerStatus) {
      RestMessage msg = new RestMessage("", "", MatterBridgeUtils.config.gateway,
          "Server has Stopped", "", MatterBridgeUtils.config.account, "", "", "",
          MatterBridgeUtils.config.protocol, DATE_FORMAT.format(new Date()), "", null);
      if (MatterBridgeUtils.sendMessage(msg) != 200) {
        ServerEssentialsServer.logger.warn("Failed to send message to bridge");
      }
    }
  }

}
