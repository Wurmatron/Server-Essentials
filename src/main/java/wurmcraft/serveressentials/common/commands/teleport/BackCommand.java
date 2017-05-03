package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class BackCommand extends EssentialsCommand {

	public BackCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "back";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/back";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Back");
		aliases.add ("BACK");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
			if (data != null && data.getLastLocation () != null) {
				BlockPos lastLocation = data.getLastLocation ();
				player.setPosition (lastLocation.getX (),lastLocation.getY (),lastLocation.getZ ());
				DataHelper.updateTeleportTimer (player.getGameProfile ().getId ());
				ChatHelper.sendMessageTo (player,Local.TELEPORT_BACK);
			}
		}
	}
}
