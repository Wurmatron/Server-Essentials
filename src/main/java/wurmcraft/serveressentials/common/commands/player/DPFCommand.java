package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.LogHandler;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public class DPFCommand extends SECommand {

	public DPFCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "deletePlayerFile";
	}

	@Override
	public String[] getAltNames () {
		return new String[] {"delPlayerFile","dPlayerFile","dpf"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/DeletePlayerFile <Username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1) {
			EntityPlayer player = UsernameResolver.getPlayer (args[0]);
			if (player != null) {
				PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
				data.setLastLocation (player.getPosition ());
				DataHelper2.forceSave (Keys.PLAYER_DATA,data);
				player.onKillCommand ();
				((EntityPlayerMP) player).connection.disconnect (new TextComponentString (Local.PLAYER_FILE_DELETE));
				File playerFile = new File (server.getDataDirectory (),File.separator + server.getFolderName () + File.separator + "playerdata" + File.separator + player.getGameProfile ().getId ().toString () + ".dat");
				LogHandler.info ("Deleting " + playerFile.getName ());
				ChatHelper.sendMessageTo (sender,Local.PLAYER_FILE_DELETE_OTHER.replaceAll ("#",UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ())));
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Deletes a certain players player file";
	}
}
