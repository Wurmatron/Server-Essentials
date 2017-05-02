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
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.*;

public class SeenCommand extends EssentialsCommand {

	private final static long ONE_SECOND = 1000;
	private final static long ONE_MINUTE = ONE_SECOND * 60;
	private final static long ONE_HOUR = ONE_MINUTE * 60;
	private final static long ONE_DAY = ONE_HOUR * 24;

	public SeenCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "seen";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Seen");
		aliases.add ("SEEN");
		return aliases;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/seen <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			PlayerList players = server.getServer ().getPlayerList ();
			boolean displayed = false;
			if (players.getPlayerList ().size () > 0)
				for (EntityPlayerMP user : players.getPlayerList ())
					if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[0]).getId ())) {
						sender.addChatMessage (new TextComponentString (Local.LAST_SEEN.replaceAll ("#","Online")));
						displayed = true;
					}
			if (!displayed && UsernameCache.getMap ().values ().contains (args[0])) {
				for (UUID username : UsernameCache.getMap ().keySet ())
					if (UsernameCache.getLastKnownUsername (username).equals (args[0])) {
						PlayerData data = DataHelper.loadPlayerData (username);
						if (data != null) {
							long timestamp = data.getLastseen ();
							sender.addChatMessage (new TextComponentString (Local.LAST_SEEN.replaceAll ("#",convert (timestamp))));
							displayed = true;
						}
						DataHelper.unloadPlayerData (username);
					}
			} else if (!displayed)
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

	private static String convert (long lastSeen) {
		return "" + convertToHumanReadable (new Date ().getTime () - lastSeen);
	}

	public static String convertToHumanReadable (long duration) {
		String readable = "";
		long temp = 0;
		if (duration >= ONE_SECOND) {
			temp = duration / ONE_DAY;
			if (temp > 0) {
				duration -= temp * ONE_DAY;
				readable = temp + " day";
				if (temp > 0)
					readable = readable + "s";
				if (duration >= ONE_MINUTE)
					readable = readable + ", ";
			}
			temp = duration / ONE_HOUR;
			if (temp > 0) {
				duration -= temp * ONE_HOUR;
				readable = readable + temp + " hour";
				if (temp > 1)
					readable = readable + "s";
				if (duration >= ONE_MINUTE)
					readable = readable + ", ";
			}
			temp = duration / ONE_MINUTE;
			if (temp > 0)
				duration -= temp * ONE_MINUTE;
			readable = readable + temp + " minute";
			if (temp > 1)
				readable = readable + "s";
			if (!readable.equals ("") && duration >= ONE_SECOND)
				readable = readable + " and ";
			temp = duration / ONE_SECOND;
			if (temp > 0)
				readable = readable + temp + " second";
			if (temp > 1)
				readable = readable + "s";
			return readable;
		} else
			return "Online";
	}
}
