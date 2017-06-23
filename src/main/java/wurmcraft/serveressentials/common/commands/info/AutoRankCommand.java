package wurmcraft.serveressentials.common.commands.info;


import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.AutoRank;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.RankManager;

import java.util.ArrayList;
import java.util.List;

public class AutoRankCommand extends EssentialsCommand {

	public AutoRankCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "autoRank";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/autoRank | /autoRank <user>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 0 && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			boolean hasNext = false;
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
			for (AutoRank autoRank : DataHelper.loadedAutoRanks)
				if (autoRank.getRank ().equalsIgnoreCase (data.getRank ().getName ())) {
					hasNext = true;
					ChatHelper.sendMessageTo (player,TextFormatting.AQUA + Local.SPACER);
					ChatHelper.sendMessageTo (player,Local.NEXT_RANK.replaceAll ("#",RankManager.getRankFromName (autoRank.getNextRank ()).getName ()));
					ChatHelper.sendMessageTo (player,checkAndFormatOnlineTime (data,autoRank));
					ChatHelper.sendMessageTo (player,checkAndFormatExp (player,autoRank));
					ChatHelper.sendMessageTo (player,checkAndFormatBal (data,autoRank));
					ChatHelper.sendMessageTo (player,TextFormatting.AQUA + Local.SPACER);
				}
			if (!hasNext)
				ChatHelper.sendMessageTo (player,Local.RANK_MAX);
		} else if (args.length == 1) {
			boolean hasNext = false;
			for (EntityPlayer player : server.getPlayerList ().getPlayerList ())
				if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
					PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
					for (AutoRank autoRank : DataHelper.loadedAutoRanks)
						if (autoRank.getRank ().equalsIgnoreCase (data.getRank ().getName ())) {
							hasNext = true;
							ChatHelper.sendMessageTo (player,TextFormatting.AQUA + Local.SPACER);
							ChatHelper.sendMessageTo (player,Local.NEXT_RANK.replaceAll ("#",RankManager.getRankFromName (autoRank.getNextRank ()).getName ()));
							ChatHelper.sendMessageTo (player,checkAndFormatOnlineTime (data,autoRank));
							ChatHelper.sendMessageTo (player,checkAndFormatExp (player,autoRank));
							ChatHelper.sendMessageTo (player,checkAndFormatBal (data,autoRank));
							ChatHelper.sendMessageTo (player,TextFormatting.AQUA + Local.SPACER);
						}
					if (!hasNext)
						ChatHelper.sendMessageTo (player,Local.RANK_MAX);
				}
		} else
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
	}

	@Override
	public String getDescription () {
		return "Checks the status of a player's Rank Up";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("autorank");
		aliases.add ("AutoRank");
		aliases.add ("AR");
		aliases.add ("ar");
		aliases.add ("Ar");
		return aliases;
	}

	private String checkAndFormatOnlineTime (PlayerData data,AutoRank rank) {
		if (data.getOnlineTime () >= rank.getPlayTime ())
			return TextFormatting.AQUA + "Online Time: " + TextFormatting.GREEN + rank.getPlayTime ();
		else
			return TextFormatting.AQUA + "Online Time: " + TextFormatting.RED + rank.getPlayTime ();
	}

	private String checkAndFormatExp (EntityPlayer player,AutoRank rank) {
		if (player.experienceLevel >= rank.getExp ())
			return TextFormatting.AQUA + "EXP: " + TextFormatting.GREEN + rank.getExp ();
		else
			return TextFormatting.AQUA + "EXP: " + TextFormatting.RED + rank.getExp ();
	}

	private String checkAndFormatBal (PlayerData data,AutoRank rank) {
		if (data.getMoney () >= rank.getBalance ())
			return TextFormatting.AQUA + "Balance: " + TextFormatting.GREEN + rank.getBalance ();
		else
			return TextFormatting.AQUA + "Balance: " + TextFormatting.RED + rank.getBalance ();
	}
}
