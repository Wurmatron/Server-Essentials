package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvseeCommand extends SECommand {

	public InvseeCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "invsee";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/invsee <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 0)
			ChatHelper.sendMessageTo (sender,getUsage (sender));
		if (args.length == 1) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			PlayerList players = server.getServer ().getPlayerList ();
			if (players.getPlayers ().size () > 0) {
				boolean open = false;
				EntityPlayer victim = UsernameResolver.getPlayer (args[0]);
				if (victim != null) {
					if (player.openContainer != player.inventoryContainer)
						player.closeScreen ();
					player.displayGUIChest (new PlayerInventory ((EntityPlayerMP) victim,player));
					ChatHelper.sendMessageTo (player,Local.PLAYER_INVENTORY.replaceAll ("#",victim.getDisplayName ().getUnformattedText ()));
					open = true;
				}
				if (!open)
					ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
			}
		}
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getOnlinePlayerNames ());
		return list;
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "View another players inventory";
	}
}
