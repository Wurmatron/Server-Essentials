package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.Vault;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.commands.utils.VaultInventory;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class VaultCommand extends EssentialsCommand {

	public VaultCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "vault";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/vault <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Vault");
		aliases.add ("VAULT");
		aliases.add ("V");
		aliases.add ("v");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		int maxVaults = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getVaultSlots ();
		if (maxVaults > 0) {
			if (DataHelper.playerVaults.get (player.getGameProfile ().getId ()) == null)
				DataHelper.loadVault (player.getGameProfile ().getId ());
			Vault[] vaults = DataHelper.playerVaults.get (player.getGameProfile ().getId ());
			if (vaults != null && vaults.length > 0 || args.length == 2) {
				if (args.length == 1) {
					boolean found = false;
					for (Vault vault : vaults)
						if (vault.getName ().equalsIgnoreCase (args[0])) {
							player.displayGUIChest (new VaultInventory ((EntityPlayerMP) player,player.getGameProfile ().getId (),vault));
							found = true;
						}
					if (!found)
						ChatHelper.sendMessageTo (player,Local.VAULT_NOT_FOUND.replaceAll ("#",args[0]));
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase ("new") || args[0].equalsIgnoreCase ("create")) {
						if (vaults != null && vaults.length > 0)
							maxVaults -= vaults.length;
						if (maxVaults > 0) {
							DataHelper.saveVault (player.getGameProfile ().getId (),new Vault (args[1],null));
							DataHelper.loadVault (player.getGameProfile ().getId ());
							ChatHelper.sendMessageTo (player,Local.VAULT_CREATED.replaceAll ("#",args[1]));
						} else
							ChatHelper.sendMessageTo (player,Local.VAULT_MAX_HIT);
					}
				} else
					ChatHelper.sendMessageTo (player,getCommandUsage (sender));
			} else
				ChatHelper.sendMessageTo (player,Local.NO_VAULTS);
		} else
			ChatHelper.sendMessageTo (player,Local.NO_VAULTS);
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Opens a inventory used for storing items without a chest";
	}
}
