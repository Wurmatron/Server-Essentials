package wurmcraft.serveressentials.common.commands.item;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.Vault;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendItemCommand extends EssentialsCommand {

	public SendItemCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "sendItem";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/sendItem <username>";
	}

	@Override
	public String getDescription () {
		return "Sends the item you are holding to another player";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("senditem");
		aliases.add ("SENDITEM");
		aliases.add ("SendItemCommand");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player.getHeldItemMainhand () != null) {
				boolean found = false;
				UUID uuid = UsernameResolver.getPlayerUUID (args[0]);
				Vault[] vaults = DataHelper.playerVaults.get (uuid);
				if (vaults != null)
					for (int index = 0; index < vaults[0].getItems ().length; index++) {
						if (vaults[0].getItems ()[index] == null) {
							ItemStack[] items = vaults[0].getItems ();
							items[index] = player.getHeldItemMainhand ();
							vaults[0].setItems (items);
							DataHelper.saveVault (uuid,vaults);
							ChatHelper.sendMessageTo (player,Local.ITEM_SENT.replaceAll ("#",player.getHeldItemMainhand ().getDisplayName ()).replaceAll ("@",UsernameCache.getLastKnownUsername (uuid)));
							player.inventory.deleteStack (player.getHeldItemMainhand ());
							found = true;
							break;
						}
					}
				if (!found)
					ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
			} else
				ChatHelper.sendMessageTo (player,Local.MISSING_STACK);
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
