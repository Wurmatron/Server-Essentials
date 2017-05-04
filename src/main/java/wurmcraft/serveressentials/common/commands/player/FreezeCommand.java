package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.event.PlayerTickEvent;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreezeCommand extends EssentialsCommand {

	public FreezeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "freeze";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/freeze <username>";
	}

	@Override
	public List <String> getCommandAliases () {
		ArrayList <String> aliases = new ArrayList <> ();
		aliases.add ("Freeze");
		aliases.add ("F");
		aliases.add ("f");
		aliases.add ("FREEZE");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer player = TeleportUtils.getPlayerFromUsername (server,args[0]);
			if (player != null) {
				PlayerTickEvent.toggleFrozen (player,player.getPosition ());
				if (PlayerTickEvent.isFrozen (player)) {
					ChatHelper.sendMessageTo (sender,Local.FROZEN_OTHER.replaceAll ("#",player.getDisplayNameString ()));
					ChatHelper.sendMessageTo (player,Local.FROZEN);
				} else {
					ChatHelper.sendMessageTo (sender,Local.UNFROZEN_OTHER.replaceAll ("#",player.getDisplayNameString ()));
					ChatHelper.sendMessageTo (player,Local.UNFROZEN);
				}
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
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
}
