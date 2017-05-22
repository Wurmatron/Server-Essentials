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
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Skull");
		aliases.add ("SKULL");
		return aliases;
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
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Creates a players head";
	}
}
