package wurmcraft.serveressentials.common.commands.eco;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoneyCommand extends EssentialsCommand {

	public MoneyCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "money";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/money <username>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Money");
		aliases.add ("MONEY");
		aliases.add ("M");
		aliases.add ("m");
		aliases.add ("bal");
		aliases.add ("Bal");
		aliases.add ("BAL");
		aliases.add ("balance");
		aliases.add ("Balance");
		aliases.add ("BALANCE");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 0) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
				ChatHelper.sendMessageTo (player,Local.CURRENT_MONEY.replaceAll ("#","" + data.getMoney ()));
			}
		} else if (args.length == 1) {
			PlayerList players = server.getPlayerList ();
			if (players.getCurrentPlayerCount () > 0) {
				boolean found = false;
				for (EntityPlayerMP player : players.getPlayerList ())
					if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
						found = true;
						PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
						ChatHelper.sendMessageTo (sender,Local.CURRENT_MONEY_OTHER.replaceAll ("#","" + UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())).replaceAll ("%", "" + data.getMoney ()));
					}
				if (!found)
					ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		}
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
