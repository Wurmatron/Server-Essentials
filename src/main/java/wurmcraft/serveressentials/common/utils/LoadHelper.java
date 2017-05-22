package wurmcraft.serveressentials.common.utils;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import wurmcraft.serveressentials.common.api.storage.ShopData;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.admin.*;
import wurmcraft.serveressentials.common.commands.chat.*;
import wurmcraft.serveressentials.common.commands.claim.ClaimCommand;
import wurmcraft.serveressentials.common.commands.claim.RemoveClaimCommand;
import wurmcraft.serveressentials.common.commands.eco.MarketCommand;
import wurmcraft.serveressentials.common.commands.eco.MoneyCommand;
import wurmcraft.serveressentials.common.commands.eco.PayCommand;
import wurmcraft.serveressentials.common.commands.info.*;
import wurmcraft.serveressentials.common.commands.item.RenameCommand;
import wurmcraft.serveressentials.common.commands.item.SendItem;
import wurmcraft.serveressentials.common.commands.item.SkullCommand;
import wurmcraft.serveressentials.common.commands.player.*;
import wurmcraft.serveressentials.common.commands.teleport.*;
import wurmcraft.serveressentials.common.reference.Perm;

import java.util.UUID;

public class LoadHelper {

	public static void registerCommands (FMLServerStartingEvent e) {
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
		e.registerServerCommand (new SuicideCommand (Perm.COMMAND_SUICIDE));
		e.registerServerCommand (new ListCommand (Perm.COMMAND_LIST));
		e.registerServerCommand (new RenameCommand (Perm.COMMAND_RENAME));
		e.registerServerCommand (new ChannelCommand (Perm.COMMAND_CHANNEL));
		e.registerServerCommand (new MuteCommand (Perm.COMMAND_MUTE));
		e.registerServerCommand (new MoneyCommand (Perm.COMMAND_MONEY));
		e.registerServerCommand (new PayCommand (Perm.COMMAND_PAY));
		e.registerServerCommand (new MarketCommand (Perm.COMMAND_MARKET));
		e.registerServerCommand (new VaultCommand (Perm.COMMAND_VAULT));
		e.registerServerCommand (new HelpCommand (Perm.COMMAND_HELP));
		e.registerServerCommand (new WebsiteCommand (Perm.COMMAND_WEBSITE));
		e.registerServerCommand (new SpeedCommand (Perm.COMMAND_SPEED));
		e.registerServerCommand (new MsgCommand (Perm.COMMAND_MESSAGE));
		e.registerServerCommand (new MailCommand (Perm.COMMAND_MAIL));
		e.registerServerCommand (new SendItem (Perm.COMMAND_ITEMSEND));
	}

	public static void loadData () {
		DataHelper.createDefaultRank ();
		DataHelper.loadWarps ();
		DataHelper.loadGlobal ();
		DataHelper.loadRanks ();
		DataHelper.loadAllTeams ();
		ChunkManager.loadAllClaims ();
		DataHelper.createDefaultChannels ();
		DataHelper.loadAllChannels ();

		ShopData data = new ShopData (new ItemStack[] {new ItemStack (Items.DIAMOND),new ItemStack (Items.IRON_INGOT,16)},new int[] {512,64},new boolean[] {true,false});
		DataHelper.createMarket (UUID.randomUUID (),data);
	}
}
