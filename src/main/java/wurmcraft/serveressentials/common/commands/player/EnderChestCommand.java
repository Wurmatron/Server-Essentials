package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class EnderChestCommand extends SECommand {

	public EnderChestCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "echest";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/echest <name>";
	}

//	@Override
//	public String[] getAliases () {
//		return new String[] {"enderChest"};
//	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 0)
			((EntityPlayer) sender).sendMessage (new TextComponentString (getUsage (sender)));
		if (args.length == 1) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			EntityPlayer victim = UsernameResolver.getPlayer (args[0]);
			if (victim != null) {
				if (player.openContainer != player.inventoryContainer)
					player.closeScreen ();
				player.displayGUIChest (new PlayerInventory ((EntityPlayerMP) victim,player,true));
				ChatHelper.sendMessageTo (player,Local.PLAYER_INVENTORY_ENDER.replaceAll ("#",victim.getDisplayName ().getUnformattedText ()));
			} else
				ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		}
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Opens the Ender Chest GUI";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}
}
