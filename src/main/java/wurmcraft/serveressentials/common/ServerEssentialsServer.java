package wurmcraft.serveressentials.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;
import wurmcraft.serveressentials.common.commands.DelHome;
import wurmcraft.serveressentials.common.commands.HomeCommand;
import wurmcraft.serveressentials.common.commands.SetHomeCommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.event.PlayerJoinEvent;
import wurmcraft.serveressentials.common.proxy.CommonProxy;
import wurmcraft.serveressentials.common.reference.Global;
import wurmcraft.serveressentials.common.utils.LogHandler;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ServerEssentialsServer {

    @Mod.Instance(Global.MODID)
    public static ServerEssentialsServer instance;

    @SidedProxy(serverSide = Global.COMMON_PROXY, clientSide = Global.CLIENT_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        ConfigHandler.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent());
    }

    @Mod.EventHandler
    public void onServerLoading(FMLServerStartingEvent e) {
        e.registerServerCommand(new SetHomeCommand());
        e.registerServerCommand(new HomeCommand());
        e.registerServerCommand(new DelHome());
    }
}
