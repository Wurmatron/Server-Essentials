package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Perm;

import javax.annotation.Nullable;
import java.util.List;

public class OnlineTimeCommand extends SECommand {

	public OnlineTimeCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "onlineTime";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"ot"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/onlineTime | /onlineTime <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		super.execute (server,sender,args);
		//		List <String> unknownPlayers = new ArrayList <> ();
		//		HashMap <UUID, PlayerData> dataMap = new HashMap <UUID, PlayerData> () {
		//			{
		//				if (args.length == 0) {
		//					UsernameCache.getMap ().forEach ((uuid,s) -> put (uuid,DataHelper.loadPlayerData (uuid)));
		//				} else {
		//					UsernameResolver.AbstractUsernameCollection <String> usernames = new UsernameResolver.AbstractUsernameCollection <String> (UsernameCache.getMap ().values ());
		//					for (String arg : args) {
		//						UsernameCache.getMap ().forEach ((uuid,s) -> {
		//							if (s.equalsIgnoreCase (arg))
		//								put (uuid,DataHelper.loadPlayerData (uuid));
		//							else if (!usernames.contains (s))
		//								unknownPlayers.add (s);
		//						});
		//					}
		//				}
		//			}
		//		};
		//		UUID[] keys = dataMap.keySet ().toArray (new UUID[0]);
		//		for (int i = 0; i < Settings.onlineTimeMaxPrint && i < keys.length; i++) {
		//			unknownPlayers.forEach (s -> ChatHelper.sendMessageTo (sender,TextFormatting.RED + "Unknown Player: '" + s + "'"));
		//			String formatted = DurationFormatUtils.formatDuration (dataMap.get (keys[i]).getOnlineTime () * 60000,"d%:H$:m#:s@").replace ('%','D').replace ('$','H').replace ('#','M').replace ('@','S').replaceAll (":",", ");
		//			ChatHelper.sendMessageTo (sender,TextFormatting.GREEN + UsernameCache.getLastKnownUsername (keys[i]) + TextFormatting.DARK_AQUA + " : " + formatted);
		//		}
	}


	@Override
	public boolean canConsoleRun () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Find out how long you have played on the server.";
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
