package wurmcraft.serveressentials.common.commands.item;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;

import javax.annotation.Nullable;
import java.util.List;

public class SkullCommand extends SECommand {

	public SkullCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "skull";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/skull <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player != null) {
				ItemStack stack = new ItemStack (Items.SKULL,1,3);
				stack.setTagCompound (new NBTTagCompound ());
				stack.getTagCompound ().setTag ("SkullOwner",new NBTTagString (args[0]));
				player.inventory.addItemStackToInventory (stack);
				ChatHelper.sendMessageTo (player,Local.SKULL.replaceAll ("#",args[0]));
			}
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Creates a players head";
	}
}
