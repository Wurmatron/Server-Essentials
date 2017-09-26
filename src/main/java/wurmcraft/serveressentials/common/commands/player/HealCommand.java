package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HealCommand extends SECommand {

	public HealCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "heal";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"h"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/heal <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1) {
			EntityPlayer user = UsernameResolver.getPlayer (args[0]);
			if (user != null) {
				user.setHealth (user.getMaxHealth ());
				ChatHelper.sendMessageTo (user,Local.HEAL_OTHER);
				ChatHelper.sendMessageTo (sender,Local.HEAL_OTHER_SENDER.replaceAll ("#",user.getDisplayName ().getUnformattedText ()));
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			player.setHealth (player.getMaxHealth ());
			ChatHelper.sendMessageTo (player,Local.HEAL_SELF);
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getOnlinePlayerNames ());
		return list;
	}

	@Override
	public String getDescription () {
		return "Heal's a certain player";
	}
}
