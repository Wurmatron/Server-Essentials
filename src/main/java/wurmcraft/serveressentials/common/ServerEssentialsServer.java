package wurmcraft.serveressentials.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import wurmcraft.serveressentials.common.claim.ClaimEvent;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.event.*;
import wurmcraft.serveressentials.common.proxy.CommonProxy;
import wurmcraft.serveressentials.common.reference.Global;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.LoadHelper;

@Mod (modid = Global.MODID, name = Global.NAME, version = Global.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ServerEssentialsServer {

	@Mod.Instance (Global.MODID)
	public static ServerEssentialsServer instance;

	@SidedProxy (serverSide = Global.COMMON_PROXY, clientSide = Global.CLIENT_PROXY)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit (FMLPreInitializationEvent e) {
		ConfigHandler.preInit (e);
	}

	@Mod.EventHandler
	public void init (FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register (new PlayerJoinEvent ());
		MinecraftForge.EVENT_BUS.register (new PlayerQuitEvent ());
		MinecraftForge.EVENT_BUS.register (new PlayerTickEvent ());
		MinecraftForge.EVENT_BUS.register (new PlayerRespawnEvent ());
		MinecraftForge.EVENT_BUS.register (new PlayerChatEvent ());
		MinecraftForge.EVENT_BUS.register (new ClaimEvent ());
		MinecraftForge.EVENT_BUS.register (new PlayerDeathEvent ());
		MinecraftForge.EVENT_BUS.register (new MarketEvent ());
		MinecraftForge.EVENT_BUS.register (new WorldEvent ());
	}

	@Mod.EventHandler
	public void onServerLoading (FMLServerStartingEvent e) {
		SecurityUtils.loadTrustedStaff ();
		LoadHelper.registerCommands (e);
		LoadHelper.loadData ();
	}
}
