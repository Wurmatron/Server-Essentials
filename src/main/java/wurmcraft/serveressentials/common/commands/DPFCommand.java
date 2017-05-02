package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DPFCommand extends EssentialsCommand {

	public DPFCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "deleteplayerfile";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/DeletePlayerFile <Username>";
	}

	@Override
	public List <String> getCommandAliases () {
		ArrayList <String> aliases = new ArrayList <> ();
		aliases.add ("deleteplayerfile");
		aliases.add ("Deleteplayerfile");
		aliases.add ("DeletePlayerfile");
		aliases.add ("DELETEPLAYERFILE");
		aliases.add ("dpf");
		aliases.add ("DPF");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1) {
			PlayerList players = FMLCommonHandler.instance ().getMinecraftServerInstance ().getPlayerList ();
			if (players.getCurrentPlayerCount () > 0) {
				for (EntityPlayerMP player : players.getPlayerList ()) {
					if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
						DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
						player.onKillCommand ();
						player.connection.kickPlayerFromServer (Local.PLAYER_FILE_DELETE);
						File playerFile = new File (server.getDataDirectory (),File.separator + server.getFolderName () + File.separator + "playerdata" + File.separator + player.getGameProfile ().getId ().toString () + ".dat");
						LogHandler.info ("Deleting " + playerFile.getName ());
					}
				}
			}
		}
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
