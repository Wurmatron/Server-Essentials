package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class SkullCommand extends EssentialsCommand {

	public SkullCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "skull";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/skull <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender instanceof EntityPlayer && args.length == 1) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player != null) {
				ItemStack stack = new ItemStack (Items.SKULL,1,3);
				stack.setTagCompound (new NBTTagCompound ());
				stack.getTagCompound ().setTag ("SkullOwner",new NBTTagString (args[0]));
				player.inventory.addItemStackToInventory (stack);
				player.addChatComponentMessage (new TextComponentString ("Player \"#\" head created!".replaceAll ("#",args[0])));
			}
		}
	}
}
