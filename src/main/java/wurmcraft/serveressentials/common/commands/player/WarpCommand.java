package wurmcraft.serveressentials.common.commands.player;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends SECommand {

	public WarpCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "warp";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/warp <name>";
	}

	@Override
	public String[] getAliases () {
		return new String[] {"warps"};
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 0)
			list (sender,args);
		if (!args[0].equalsIgnoreCase ("list") && DataHelper.getWarp (args[0]) != null) {
			Warp warp = DataHelper.getWarp (args[0]);
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
			if (TeleportUtils.canTeleport (player.getGameProfile ().getId ())) {
				player.setPositionAndRotation (warp.getPos ().getX (),warp.getPos ().getY (),warp.getPos ().getZ (),warp.getYaw (),warp.getPitch ());
				DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
				TeleportUtils.teleportTo (player,warp.getPos (),warp.getDimension (),true);
				ChatHelper.sendMessageTo (player,Local.WARP_TELEPORT.replaceAll ("#",warp.getName ()),hoverEvent (warp));
			} else if (!TeleportUtils.canTeleport (player.getGameProfile ().getId ()))
				ChatHelper.sendMessageTo (player,Local.TELEPORT_COOLDOWN.replaceAll ("#",TeleportUtils.getRemainingCooldown (player.getGameProfile ().getId ())));
		} else
			ChatHelper.sendMessageTo (sender,Local.WARP_NONE.replaceAll ("#",args[0]));
	}

	@SubCommand
	public void list (ICommandSender sender,String[] args) {
		List <String> warps = new ArrayList <> ();
		for (Warp warp : DataHelper.getWarps ())
			warps.add (warp.getName ());
		if (warps.size () > 0)
			ChatHelper.sendMessageTo (sender,TextFormatting.DARK_AQUA + "Warps: " + TextFormatting.AQUA + Strings.join (warps,", "));
		else
			ChatHelper.sendMessageTo (sender,Local.WARPS_NONE);
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Teleport to a specified \"Warp\" location";
	}

	private HoverEvent hoverEvent (Warp warp) {
		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,DataHelper.displayLocation (warp));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteWarps (args,DataHelper.getWarps ());
	}
}
