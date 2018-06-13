package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.LocationWrapper;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.event.PlayerRespawnEvent;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class SuicideCommand extends SECommand {

	public SuicideCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "suicide";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/suicide";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
		data.setLastLocation(new LocationWrapper((int) player.posX, (int)player.posY,(int) player.posZ, player.dimension));
		DataHelper2.forceSave (Keys.PLAYER_DATA,data);
		player.onKillCommand ();
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Kill yourself";
	}
}
