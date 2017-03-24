package wurmcraft.serveressentials.common;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.Location;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.commands.*;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.event.*;
import wurmcraft.serveressentials.common.proxy.CommonProxy;
import wurmcraft.serveressentials.common.reference.Global;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.ClaimManager;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.RegionClaim;

import java.util.UUID;

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
				MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent()); MinecraftForge.EVENT_BUS.register(new PlayerQuitEvent());
				MinecraftForge.EVENT_BUS.register(new PlayerTickEvent());
				MinecraftForge.EVENT_BUS.register(new PlayerRespawnEvent());
				MinecraftForge.EVENT_BUS.register(new PlayerChatEvent());
				MinecraftForge.EVENT_BUS.register(new BreakBlockEvent());
		}

		@Mod.EventHandler
		public void onServerLoading(FMLServerStartingEvent e) {
				e.registerServerCommand(new SetHomeCommand(Perm.COMMAND_SET_HOME));
				e.registerServerCommand(new HomeCommand(Perm.COMMAND_HOME));
				e.registerServerCommand(new DelHome(Perm.COMMAND_DELETE_HOME));
				e.registerServerCommand(new SetWarpCommand(Perm.COMMAND_SET_WARP));
				e.registerServerCommand(new WarpCommand(Perm.COMMAND_WARP));
				e.registerServerCommand(new DelWarp(Perm.COMMAND_DELETE_WARP));
				e.registerServerCommand(new SetSpawnCommand(Perm.COMMAND_SETSPAWN));
				e.registerServerCommand(new SpawnCommand(Perm.COMMAND_SPAWN));
				e.registerServerCommand(new InvseeCommand(Perm.COMMAND_INVSEE));
				e.registerServerCommand(new EnderChestCommand(Perm.COMMAND_ENDER_CHEST));
				e.registerServerCommand(new SudoCommand(Perm.COMMAND_SUDO));
				e.registerServerCommand(new SeenCommand(Perm.COMMAND_SEEN));
				e.registerServerCommand(new HealCommand(Perm.COMMAND_HEAL));
				e.registerServerCommand(new GameModeCommand(Perm.COMMAND_GAMEMODE));
				e.registerServerCommand(new RulesCommand(Perm.COMMAND_RULES));
				e.registerServerCommand(new AddRuleCommand(Perm.COMMAND_ADD_RULES));
				e.registerServerCommand(new DeleteRuleCommand(Perm.COMMAND_DELETE_RULE));
				e.registerServerCommand(new MotdCommand(Perm.COMMAND_MOTD));
				e.registerServerCommand(new AddMotdCommand(Perm.COMMAND_ADD_MOTD));
				e.registerServerCommand(new DeleteMotdCommand(Perm.COMMAND_DELETE_MOTD));
				e.registerServerCommand(new TpaCommand(Perm.COMMAND_TPA));
				e.registerServerCommand(new TpacceptCommand(Perm.COMMAND_TPA_ACCEPT));
				e.registerServerCommand(new TpdenyCommand(Perm.COMMAND_TPA_DENY));
				e.registerServerCommand(new TeamCommand(Perm.COMMAND_TEAM));
				e.registerServerCommand(new TeamAdminCommand(Perm.COMMAND_TEAMADMIN)); DataHelper.createDefaultRank();
				DataHelper.loadWarps(); DataHelper.loadGlobal(); DataHelper.loadRanks(); DataHelper.loadAllTeams();
				// TODO Remember to remove when done testing
				RegionClaim claim = new RegionClaim();
				for (int x = 0; x < 32; x++)
						for (int z = 0; z < 32; z++)
								claim.addClaim(new Location(x, z), new Claim(new Team("test", UUID.randomUUID(), false), UUID.randomUUID()));
				ClaimManager.saveRegionClaim((byte) 0, new BlockPos(0, 200, 0), claim);
				ClaimManager.loadedClaims.put(new Location(0,0), claim);
		}
}
