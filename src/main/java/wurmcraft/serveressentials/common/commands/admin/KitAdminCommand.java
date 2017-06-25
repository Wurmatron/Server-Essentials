package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Kit;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitAdminCommand extends EssentialsCommand {

	public KitAdminCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "kitAdmin";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/kitAdmin create <name> <time> | /kitAdmin remove <name>";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Used to easily create or remove a kit";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("kitadmin");
		aliases.add ("Kitadmin");
		aliases.add ("KITADMIN");
		aliases.add ("KitAdmin");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase ("create") || args[0].equalsIgnoreCase ("new")) {
				if (!args[1].equalsIgnoreCase ("list")) {
					List <ItemStack> kitItems = new ArrayList <> ();
					for (ItemStack stack : player.inventory.mainInventory)
						if (stack != null)
							kitItems.add (stack);
					if (kitItems.size () > 0) {
						int time = Integer.valueOf (args[2]);
						if (time > 0 && args.length == 3) {
							Kit kit = new Kit (args[1],kitItems.toArray (new ItemStack[0]),time);
							DataHelper.saveKit (kit);
							ChatHelper.sendMessageTo (player,Local.KIT_CREATED.replaceAll ("#",kit.getName ()));
						} else
							ChatHelper.sendMessageTo (player,getCommandUsage (player));
					}
				} else
					ChatHelper.sendMessageTo (player,Local.INVALID_KIT_NAME.replaceAll ("#",args[1]));
			} else if (args[0].equalsIgnoreCase ("remove") || args[0].equalsIgnoreCase ("rem") || args[0].equalsIgnoreCase ("delete") || args[0].equalsIgnoreCase ("del")) {
				boolean found = false;
				for (Kit kit : DataHelper.loadedKits)
					if (kit.getName ().equalsIgnoreCase (args[1])) {
						found = true;
						File kitFile = new File (DataHelper.kitLocation + File.separator + kit.getName () + ".json");
						if (kitFile.exists ())
							kitFile.delete ();
						DataHelper.loadedKits.remove (kit);
						ChatHelper.sendMessageTo (player,Local.KIT_REMOVED.replaceAll ("#",kit.getName ()));
						break;
					}
				if (!found)
					ChatHelper.sendMessageTo (player,Local.KIT_NOTFOUND.replaceAll ("#",args[1]));
			} else
				ChatHelper.sendMessageTo (sender,getCommandUsage (player));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (player));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteKits (args,DataHelper.loadedKits,1);
	}
}
