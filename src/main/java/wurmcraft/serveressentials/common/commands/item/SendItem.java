package wurmcraft.serveressentials.common.commands.item;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.Vault;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendItem extends EssentialsCommand {

	public SendItem (String perm) {
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
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player.getHeldItemMainhand () != null) {
				for (UUID uuid : UsernameCache.getMap ().keySet ())
					if (UsernameCache.getLastKnownUsername (uuid).equalsIgnoreCase (args[0])) {
						Vault[] vaults = DataHelper.playerVaults.get (uuid);
						LogHandler.info ("Player Found");
						if (vaults != null)
							for (int index = 0; index < vaults[0].getItems ().length; index++) {
								if (vaults[0].getItems ()[index] == null) {
									ItemStack[] items = vaults[0].getItems ();
									items[index] = player.getHeldItemMainhand ();
									vaults[0].setItems (items);
									DataHelper.saveVault (uuid,vaults[0]);
									player.inventory.deleteStack (player.getHeldItemMainhand ());
									ChatHelper.sendMessageTo (player,Local.ITEM_SENT.replaceAll ("#",player.getHeldItemMainhand ().getDisplayName ()).replaceAll ("@",UsernameCache.getLastKnownUsername (uuid)));
									break;
								}
							}
					}
			} else
				ChatHelper.sendMessageTo (player,Local.MISSING_STACK);
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List<String> getCommandAliases () {
		List<String> aliases = new ArrayList <> ();
		aliases.add ("senditem");
		aliases.add ("SENDITEM");
		aliases.add ("SendItem");
		return aliases;
	}
}
