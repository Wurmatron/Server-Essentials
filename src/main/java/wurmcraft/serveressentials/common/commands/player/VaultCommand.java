package wurmcraft.serveressentials.common.commands.player;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.Vault;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.commands.utils.VaultInventory;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VaultCommand extends SECommand {

	public VaultCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "vault";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/vault <name> | /vault create <name>| /vault delete <name";
	}

//	@Override
//	public String[] getAliases () {
//		return new String[] {"v"};
//	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		int maxVaults = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getVaultSlots ();
		if (maxVaults > 0 && DataHelper.playerVaults.get (player.getGameProfile ().getId ()) == null)
			DataHelper.loadVault (player.getGameProfile ().getId ());
		if (maxVaults == 0)
			ChatHelper.sendMessageTo (player,Local.NO_VAULTS);
		if (args.length == 1 && !args[0].equalsIgnoreCase ("list") && !args[0].equalsIgnoreCase ("del") && !args[0].equalsIgnoreCase ("delete") && args[0].equalsIgnoreCase ("create")) {
			Vault[] vaults = DataHelper.playerVaults.get (player.getGameProfile ().getId ());
			Vault vault = findVault (vaults,args[0]);
			if (vault != null)
				player.displayGUIChest (new VaultInventory ((EntityPlayerMP) player,player.getGameProfile ().getId (),vault));
			else
				ChatHelper.sendMessageTo (player,Local.VAULT_NOT_FOUND.replaceAll ("#",args[0]));
		}
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Opens a inventory used for storing items without a chest";
	}

	@Override
	public List <String> getTabCompletions(MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			return autoCompleteVaults (args,DataHelper.playerVaults.get (player.getGameProfile ().getId ()));
		}
		return null;
	}

	public Vault findVault (Vault[] vaults,String name) {
		if (vaults.length > 0)
			for (Vault vault : vaults)
				if (vault != null && vault.getName ().equalsIgnoreCase (name))
					return vault;
		return null;
	}

	private boolean hasItems (Vault vault) {
		for (ItemStack stack : vault.getItems ())
			if (stack != null)
				return true;
		return false;
	}

	private int getSlots (EntityPlayer player,int maxSlots) {
		int max = maxSlots;
		if (DataHelper.playerVaults.get (player.getGameProfile ().getId ()) == null)
			return max;
		for (Vault vault : DataHelper.playerVaults.get (player.getGameProfile ().getId ()))
			if (vault != null)
				max--;
		return max;
	}

	@Override
	public boolean hasSubCommand () {
		return true;
	}

	@SubCommand
	public void list (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Vault[] vaults = DataHelper.playerVaults.get (player.getGameProfile ().getId ());
		List <String> vaultNames = new ArrayList <> ();
		for (Vault vault : vaults)
			if (vault != null)
				vaultNames.add (vault.getName ());
		ChatHelper.sendMessageTo (player,TextFormatting.AQUA + "Vaults: " + TextFormatting.GOLD + Strings.join (vaultNames,", "));
	}

	@SubCommand
	public void create (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		int maxVaults = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getVaultSlots ();
		if (getSlots (player,maxVaults) > 0) {
			if (args[0].equalsIgnoreCase ("list")) {
				ChatHelper.sendMessageTo (player,Local.VAULT_NAME.replaceAll ("#",args[0]));
				return;
			}
			DataHelper.saveVault (player.getGameProfile ().getId (),new Vault (args[0],null));
			DataHelper.loadVault (player.getGameProfile ().getId ());
			ChatHelper.sendMessageTo (player,Local.VAULT_CREATED.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (player,Local.VAULT_MAX_HIT);
	}

	@SubCommand
	public void delete (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		Vault[] vaults = DataHelper.playerVaults.get (player.getGameProfile ().getId ());
		for (int index = 0; index < vaults.length; index++)
			if (vaults[index] != null && vaults[index].getName ().equalsIgnoreCase (args[0]))
				if (!hasItems (vaults[index])) {
					vaults[index] = null;
					DataHelper.saveVault (player.getGameProfile ().getId (),vaults);
					DataHelper.loadVault (player.getGameProfile ().getId ());
					ChatHelper.sendMessageTo (player,Local.VAULT_DELETED.replaceAll ("#",args[0]));
				} else
					ChatHelper.sendMessageTo (player,Local.VAULT_ITEMS);
	}

	@SubCommand
	public void del (ICommandSender sender,String[] args) {
		delete (sender,args);
	}
}
