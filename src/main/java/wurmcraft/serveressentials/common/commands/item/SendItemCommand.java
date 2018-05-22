package wurmcraft.serveressentials.common.commands.item;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

import javax.annotation.Nullable;
import java.util.List;

public class SendItemCommand extends SECommand {

	public SendItemCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "sendItem";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"sItem"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/sendItem <username>";
	}

	@Override
	public String getDescription () {
		return "Sends the item you are holding to another player";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		super.execute (server,sender,args);
		//		if (args.length == 1) {
		//			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		//			if (player.getHeldItemMainhand () != null) {
		//				boolean found = false;
		//				UUID uuid = UsernameResolver.getPlayerUUID (args[0]);
		//				Vault[] vaults = DataHelper.playerVaults.get (uuid);
		//				if (vaults != null)
		//					for (int index = 0; index < vaults[0].getItems ().length; index++) {
		//						if (vaults[0].getItems ()[index] == null) {
		//							ItemStack[] items = vaults[0].getItems ();
		//							items[index] = player.getHeldItemMainhand ();
		//							vaults[0].setItems (items);
		//							DataHelper.saveVault (uuid,vaults);
		//							ChatHelper.sendMessageTo (player,Local.ITEM_SENT.replaceAll ("#",player.getHeldItemMainhand ().getDisplayName ()).replaceAll ("@",UsernameCache.getLastKnownUsername (uuid)));
		//							player.inventory.deleteStack (player.getHeldItemMainhand ());
		//							found = true;
		//							break;
		//						}
		//					}
		//				if (!found)
		//					ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		//			} else
		//				ChatHelper.sendMessageTo (player,Local.MISSING_STACK);
		//		} else
		//			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
