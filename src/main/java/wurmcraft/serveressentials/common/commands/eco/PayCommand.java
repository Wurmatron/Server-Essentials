package wurmcraft.serveressentials.common.commands.eco;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class PayCommand extends SECommand {

	public PayCommand (Perm perm) {
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
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerData playerData = DataHelper.getPlayerData (player.getGameProfile ().getId ());
		if (args.length == 2 && Integer.valueOf (args[1]) != null) {
			int money = Integer.valueOf (args[1]);
			if (money > 0) {
				if (playerData.getMoney () >= money) {
					EntityPlayer p = UsernameResolver.getPlayer (args[0]);
					if (p != null) {
						PlayerData receiverData = DataHelper.getPlayerData (p.getGameProfile ().getId ());
						ChatHelper.sendMessageTo (player,Local.MONEY_SENT.replaceAll ("#",UsernameCache.getLastKnownUsername (p.getGameProfile ().getId ())).replaceAll ("%","" + money));
						ChatHelper.sendMessageTo (p,Local.MONEY_SENT_RECEIVER.replaceAll ("#",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())).replaceAll ("%","" + money));
						DataHelper.setMoney (player.getGameProfile ().getId (),playerData.getMoney () - money);
						DataHelper.setMoney (p.getGameProfile ().getId (),receiverData.getMoney () + money);
					} else
						ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
				} else
					ChatHelper.sendMessageTo (player,Local.MISSING_MONEY.replaceAll ("#","" + money));
			} else
				ChatHelper.sendMessageTo (player,Local.NEGATIVE_MONEY);
		} else
			ChatHelper.sendMessageTo (player,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Send money to another player";
	}
}
