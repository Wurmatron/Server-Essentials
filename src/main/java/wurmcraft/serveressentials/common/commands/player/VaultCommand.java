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
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
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
						DataHelper.saveVault (player.getGameProfile ().getId (),new Vault (args[0],null));
						DataHelper.loadVault (player.getGameProfile ().getId ());
						ChatHelper.sendMessageTo (player,Local.VAULT_CREATED.replaceAll ("#",args[0]));
					}
				}
			} else
				ChatHelper.sendMessageTo (player,Local.NO_VAULTS);
		} else
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
	}


}
