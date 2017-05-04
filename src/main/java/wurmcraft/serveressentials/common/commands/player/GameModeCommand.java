package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameModeCommand extends EssentialsCommand {

	private static final String[] CREATIVE = new String[] {"creative","c","1"};
	private static final String[] SURVIVAL = new String[] {"survival","s","0"};
	private static final String[] ADVENTURE = new String[] {"adventure","a","2"};
	private static final String[] SPECTATOR = new String[] {"spectator","sp","3"};

	public GameModeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "gamemode";
	}

	@Override
	public List <String> getCommandAliases () {
		ArrayList <String> aliases = new ArrayList <> ();
		aliases.add ("gm");
		aliases.add ("GM");
		aliases.add ("mode");
		aliases.add ("Mode");
		aliases.add ("MODE");
		aliases.add ("GameMode");
		aliases.add ("gameMode");
		aliases.add ("Gamemode");
		return aliases;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/gamemode <type> <user>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			int mode = getMode (args[0]);
			if (mode != -1) {
				if (args.length > 1) {
					PlayerList players = server.getServer ().getPlayerList ();
					if (players.getPlayerList ().size () > 0)
						for (EntityPlayerMP user : players.getPlayerList ())
							if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[1]).getId ())) {
								user.setGameType (GameType.getByID (mode));
								ChatHelper.sendMessageTo (user,Local.MODE_CHANGED.replaceAll ("#",GameType.getByID (mode).getName ()));
								ChatHelper.sendMessageTo (sender,Local.MODE_CHANGED_OTHER.replaceAll ("#",user.getDisplayName ().getUnformattedText ()).replaceAll ("$",GameType.getByID (mode).getName ()));
							}
				} else if (sender instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) sender;
					player.setGameType (GameType.getByID (mode));
					ChatHelper.sendMessageTo (player,Local.MODE_CHANGED.replaceAll ("#",GameType.getByID (mode).getName ()));
				} else
					ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
			} else
				ChatHelper.sendMessageTo (sender,Local.MODE_INVALID.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (args.length == 2)
			if (sender instanceof EntityPlayer)
				Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
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
}
