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

public class PayCommand extends EssentialsCommand {

	public PayCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "pay";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/pay <username> <amount>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Pay");
		aliases.add ("PAY");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			PlayerData playerData = DataHelper.getPlayerData (player.getGameProfile ().getId ());
			if (args.length == 2 && Integer.valueOf (args[1]) != null) {
				int money = Integer.valueOf (args[1]);
				if (money > 0) {
					if (playerData.getMoney () >= money) {
						PlayerList players = server.getPlayerList ();
						if (players.getCurrentPlayerCount () > 0) {
							boolean found = false;
							for (EntityPlayerMP p : players.getPlayerList ())
								if (UsernameCache.getLastKnownUsername (p.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
									found = true;
									PlayerData receiverData = DataHelper.getPlayerData (p.getGameProfile ().getId ());
									ChatHelper.sendMessageTo (player,Local.MONEY_SENT.replaceAll ("#",UsernameCache.getLastKnownUsername (p.getGameProfile ().getId ())).replaceAll ("%","" + money));
									ChatHelper.sendMessageTo (p,Local.MONEY_SENT_RECEIVER.replaceAll ("#",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())).replaceAll ("%","" + money));
									DataHelper.setMoney (player.getGameProfile ().getId (),playerData.getMoney () - money);
									DataHelper.setMoney (p.getGameProfile ().getId (),receiverData.getMoney () + money);
								}
							if (!found)
								ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
						} else
							ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
					} else
						ChatHelper.sendMessageTo (player,Local.MISSING_MONEY.replaceAll ("#","" + money));
				} else
					ChatHelper.sendMessageTo (player,Local.NEGATIVE_MONEY);
			} else
				ChatHelper.sendMessageTo (player,getCommandUsage (sender));
		} else
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
			if (sender instanceof EntityPlayer)
				Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
