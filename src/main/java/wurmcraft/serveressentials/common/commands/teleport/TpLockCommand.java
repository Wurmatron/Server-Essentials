package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class TpLockCommand extends SECommand {

	public TpLockCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "tplock";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/tplock";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Disable tpa requests";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
		boolean temp = data.isTpLock ();
		data.setTpLock (!temp);
		DataHelper2.forceSave (Keys.PLAYER_DATA,data);
		ChatHelper.sendMessageTo (player,Local.TPLOCK.replaceAll ("#",(temp ? "Disabled" : "Enabled")));
	}
}
