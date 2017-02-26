package wurmcraft.serveressentials.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import wurmcraft.serveressentials.common.commands.*;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.event.PlayerJoinEvent;
import wurmcraft.serveressentials.common.event.PlayerQuitEvent;
import wurmcraft.serveressentials.common.event.PlayerRespawnEvent;
import wurmcraft.serveressentials.common.event.PlayerTickEvent;
import wurmcraft.serveressentials.common.proxy.CommonProxy;
import wurmcraft.serveressentials.common.reference.Global;
import wurmcraft.serveressentials.common.utils.DataHelper;

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
        MinecraftForge.EVENT_BUS.register(new PlayerQuitEvent());
        MinecraftForge.EVENT_BUS.register(new PlayerTickEvent());
        MinecraftForge.EVENT_BUS.register(new PlayerRespawnEvent());
    }

    @Mod.EventHandler
    public void onServerLoading(FMLServerStartingEvent e) {
        e.registerServerCommand(new SetHomeCommand());
        e.registerServerCommand(new HomeCommand());
        e.registerServerCommand(new DelHome());
        e.registerServerCommand(new SetWarpCommand());
        e.registerServerCommand(new WarpCommand());
        e.registerServerCommand(new DelWarp());
        e.registerServerCommand(new SetSpawnCommand());
        e.registerServerCommand(new SpawnCommand());
        e.registerServerCommand(new InvseeCommand());
        e.registerServerCommand(new EnderChestCommand());
        e.registerServerCommand(new SudoCommand());
        e.registerServerCommand(new SeenCommand());
        DataHelper.loadWarps();
        DataHelper.loadGlobal();
    }
}
