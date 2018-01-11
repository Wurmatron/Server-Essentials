package wurmcraft.serveressentials.common.commands.admin;


import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SetGroup extends SECommand {

	public SetGroup (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "setGroup";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"setRank"};
	}


	@Override
	public String getUsage (ICommandSender sender) {
		return "/setGroup <username> <group>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length > 1) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
			boolean found = false;
			if (player != null && RankManager.getRankFromName (args[1]) != null) {
				found = true;
				playerData.setRank (RankManager.getRankFromName (args[1]));
				DataHelper2.forceSave (Keys.PLAYER_DATA,playerData);
				String name = playerData.getNickname () != null ? "*" + TextFormatting.RESET + playerData.getNickname ().replaceAll ("&","\u00A7") : player.getDisplayNameString ();
				ChatHelper.sendMessageTo (sender,Local.RANK_CHANGED.replaceAll ("Your",name).replaceAll ("#",RankManager.getRankFromName (args[1]).getName ()));
			}
			if (!found)
				for (UUID id : UsernameCache.getMap ().keySet ())
					if (UsernameCache.getMap ().get (id).equalsIgnoreCase (args[0]) && RankManager.getRankFromName (args[1]) != null) {
						found = true;
						PlayerData data = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,id.toString ());
						data.setRank (RankManager.getRankFromName (args[1]));
						DataHelper2.forceSave (Keys.PLAYER_DATA,data);
						String name = playerData.getNickname () != null ? "*" + TextFormatting.RESET + playerData.getNickname ().replaceAll ("&","\u00A7") : UsernameCache.getLastKnownUsername (id);
						ChatHelper.sendMessageTo (sender,Local.RANK_CHANGED.replaceAll ("Your",name).replaceAll ("#",RankManager.getRankFromName (args[1]).getName ()));
					}
			if (!found)
				ChatHelper.sendMessageTo (sender,Local.RANK_NOT_FOUND.replaceAll ("#",args[1]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public String getDescription () {
		return "Set a player's group";
	}

	@Override
	public boolean canConsoleRun () {
		return !Settings.securityModule;
	}

	//	@Override
	//	public String[] getAliases () {
	//		return new String[] {"setrank"};
	//	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public boolean requiresTrusted () {
		return true;
	}
}
