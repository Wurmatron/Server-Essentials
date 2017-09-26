package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class GameModeCommand extends SECommand {

	private static final String[] CREATIVE = new String[] {"Creative","c","1"};
	private static final String[] SURVIVAL = new String[] {"Survival","s","0"};
	private static final String[] ADVENTURE = new String[] {"Adventure","a","2"};
	private static final String[] SPECTATOR = new String[] {"Spectator","sp","3"};

	public GameModeCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "gamemode";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"gm"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/gamemode <type> <user>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			int mode = getMode (args[0]);
			if (mode != -1) {
				if (args.length > 1) {
					EntityPlayer user = UsernameResolver.getPlayer (args[1]);
					if (user != null) {
						user.setGameType (GameType.getByID (mode));
						ChatHelper.sendMessageTo (user,Local.MODE_CHANGED.replaceAll ("#",GameType.getByID (mode).getName ()));
						ChatHelper.sendMessageTo (sender,Local.MODE_CHANGED_OTHER.replaceAll ("#",user.getDisplayName ().getUnformattedText ()).replaceAll ("\\$",GameType.getByID (mode).getName ()));
					} else
						ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[1]));
				} else if (sender instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) sender;
					player.setGameType (GameType.getByID (mode));
					ChatHelper.sendMessageTo (player,Local.MODE_CHANGED.replaceAll ("#",GameType.getByID (mode).getName ()));
				} else
					ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
			} else
				ChatHelper.sendMessageTo (sender,Local.MODE_INVALID.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getUsage (sender));
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,1);
	}

	private int getMode (String arg) {
		for (String creative : CREATIVE)
			if (arg.equalsIgnoreCase (creative))
				return 1;
		for (String survival : SURVIVAL)
			if (arg.equalsIgnoreCase (survival))
				return 0;
		for (String adventure : ADVENTURE)
			if (arg.equalsIgnoreCase (adventure))
				return 2;
		for (String spectator : SPECTATOR)
			if (arg.equalsIgnoreCase (spectator))
				return 3;
		return -1;
	}

	@Override
	public String getDescription () {
		return "Changes a player's GameMode";
	}
}
