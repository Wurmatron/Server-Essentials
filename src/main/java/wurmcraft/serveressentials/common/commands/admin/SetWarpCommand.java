package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class SetWarpCommand extends SECommand {

	public SetWarpCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "setwarp";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/setwarp <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length == 1) {
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
			Warp warp = new Warp (args[0],player.getPosition (),player.dimension,player.rotationYaw,player.rotationPitch);
			DataHelper2.forceSave (Keys.WARP,warp);
			ChatHelper.sendMessageTo (player,Local.WARP_CREATED.replaceAll ("#",warp.getName ()),hoverEvent (warp));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	public HoverEvent hoverEvent (Warp home) {
		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,ChatHelper.displayLocation (home));
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Create a warp at the players location";
	}
}
