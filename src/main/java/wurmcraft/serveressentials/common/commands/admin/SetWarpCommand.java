package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class SetWarpCommand extends EssentialsCommand {

	public SetWarpCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "setwarp";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/setwarp <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("SetWarp");
		aliases.add ("setWarp");
		aliases.add ("Setwarp");
		aliases.add ("SETWARP");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		if (args.length != 1)
			ChatHelper.sendMessageTo (sender,Local.WARP_NAME);
		else {
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
			Warp warp = new Warp (args[0],player.getPosition (),player.dimension,player.rotationYaw,player.rotationPitch);
			ChatHelper.sendMessageTo (player,DataHelper.createWarp (warp),hoverEvent (warp));
		}
	}

	public HoverEvent hoverEvent (Warp home) {
		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,DataHelper.displayLocation (home));
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Create a warp at the players location";
	}
}
