package wurmcraft.serveressentials.common.utils;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.*;
import wurmcraft.serveressentials.common.commands.claim.ClaimCommand;
import wurmcraft.serveressentials.common.commands.claim.RemoveClaimCommand;
import wurmcraft.serveressentials.common.reference.Perm;

public class LoadHelper {

	public static void registerCommands(FMLServerStartingEvent e) {
		e.registerServerCommand (new SetHomeCommand (Perm.COMMAND_SET_HOME));
		e.registerServerCommand (new HomeCommand (Perm.COMMAND_HOME));
		e.registerServerCommand (new DelHome (Perm.COMMAND_DELETE_HOME));
		e.registerServerCommand (new SetWarpCommand (Perm.COMMAND_SET_WARP));
		e.registerServerCommand (new WarpCommand (Perm.COMMAND_WARP));
		e.registerServerCommand (new DelWarp (Perm.COMMAND_DELETE_WARP));
		e.registerServerCommand (new SetSpawnCommand (Perm.COMMAND_SETSPAWN));
		e.registerServerCommand (new SpawnCommand (Perm.COMMAND_SPAWN));
		e.registerServerCommand (new InvseeCommand (Perm.COMMAND_INVSEE));
		e.registerServerCommand (new EnderChestCommand (Perm.COMMAND_ENDER_CHEST));
		e.registerServerCommand (new SudoCommand (Perm.COMMAND_SUDO));
		e.registerServerCommand (new SeenCommand (Perm.COMMAND_SEEN));
		e.registerServerCommand (new HealCommand (Perm.COMMAND_HEAL));
		e.registerServerCommand (new GameModeCommand (Perm.COMMAND_GAMEMODE));
		e.registerServerCommand (new RulesCommand (Perm.COMMAND_RULES));
		e.registerServerCommand (new AddRuleCommand (Perm.COMMAND_ADD_RULES));
		e.registerServerCommand (new DeleteRuleCommand (Perm.COMMAND_DELETE_RULE));
		e.registerServerCommand (new MotdCommand (Perm.COMMAND_MOTD));
		e.registerServerCommand (new AddMotdCommand (Perm.COMMAND_ADD_MOTD));
		e.registerServerCommand (new DeleteMotdCommand (Perm.COMMAND_DELETE_MOTD));
		e.registerServerCommand (new TpaCommand (Perm.COMMAND_TPA));
		e.registerServerCommand (new TpacceptCommand (Perm.COMMAND_TPA_ACCEPT));
		e.registerServerCommand (new TpdenyCommand (Perm.COMMAND_TPA_DENY));
		e.registerServerCommand (new TeamCommand (Perm.COMMAND_TEAM));
		e.registerServerCommand (new TeamAdminCommand (Perm.COMMAND_TEAMADMIN));
		e.registerServerCommand (new ClaimCommand (Perm.COMMAND_CLAIM));
		e.registerServerCommand (new RemoveClaimCommand (Perm.COMMAND_REM_CLAIM));
		e.registerServerCommand (new FlyCommand (Perm.COMMAND_FLY));
		e.registerServerCommand (new AfkCommand (Perm.COMMAND_AFK));
		e.registerServerCommand (new BroadcastCommand (Perm.COMMAND_BROADCAST));
		e.registerServerCommand (new PingCommand (Perm.COMMAND_PING));
		e.registerServerCommand (new SkullCommand (Perm.COMMAND_SKULL));
		e.registerServerCommand (new BackCommand (Perm.COMMAND_BACK));
		e.registerServerCommand (new DPFCommand (Perm.COMMAND_DELETE_PLAYER_DATA));
		e.registerServerCommand (new ReloadPlayerDataCommand (Perm.COMMAND_RELOAD_PLAYER_DATA));
		e.registerServerCommand (new TpCommand (Perm.COMMAND_TELEPORT));
		e.registerServerCommand (new FreezeCommand (Perm.COMMAND_FREEZE));
		e.registerServerCommand (new TopCommand (Perm.COMMAND_TOP));
	}

	public static void loadData() {
		DataHelper.createDefaultRank ();
		DataHelper.loadWarps ();
		DataHelper.loadGlobal ();
		DataHelper.loadRanks ();
		DataHelper.loadAllTeams ();
		ChunkManager.loadAllClaims ();
	}
}