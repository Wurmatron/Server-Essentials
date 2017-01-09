package wurmcraft.serveressentials.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.ServerEssentialsServer;
import wurmcraft.serveressentials.common.proxy.CommonProxy;
import wurmcraft.serveressentials.common.reference.Global;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, clientSideOnly = true)
public class ServerEssentialsClient {

    @Mod.Instance(Global.MODID)
    public static ServerEssentialsServer instance;

    @SidedProxy(serverSide = Global.COMMON_PROXY, clientSide = Global.CLIENT_PROXY)
    public static CommonProxy proxy;

    @SubscribeEvent
    public void init(FMLInitializationEvent e) {
    }
}
