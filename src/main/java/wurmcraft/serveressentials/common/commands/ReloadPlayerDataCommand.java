package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadPlayerDataCommand extends EssentialsCommand {

	public ReloadPlayerDataCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "reloadPlayerData";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/reloadPlayerData <username>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("reloadplayerdata");
		aliases.add ("Reloadplayerdata");
		aliases.add ("ReloadPlayerdata");
		aliases.add ("ReloadPlayerData");
		aliases.add ("RELOADPLAYERDATA");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && args[0] != null) {
			boolean found = false;
			PlayerList players = server.getPlayerList ();
			if (players.getCurrentPlayerCount () > 0) {
				for (EntityPlayerMP player : players.getPlayerList ())
					if (UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()) != null && UsernameCache.getLastKnownUsername (player.getGameProfile ().getId ()).equalsIgnoreCase (args[0])) {
						DataHelper.unloadPlayerData (player.getGameProfile ().getId ());
						DataHelper.loadPlayerData (player.getGameProfile ().getId ());
						player.addChatComponentMessage (new TextComponentString (Local.DATA_RELOADED));
						sender.addChatMessage (new TextComponentString (Local.DATA_RELOADED_OTHER.replaceAll ("#",player.getDisplayNameString ())));
						found = true;
					}
			}
			if (!found)
				sender.addChatMessage (new TextComponentString (Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0])));
		} else
			sender.addChatMessage (new TextComponentString (getCommandUsage (sender)));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
