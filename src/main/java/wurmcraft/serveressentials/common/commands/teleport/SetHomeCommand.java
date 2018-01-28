package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SetHomeCommand extends SECommand {

	public SetHomeCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "setHome";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"sHome"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/sethome <name>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args != null && args.length > 0) {
			if (!args[0].equalsIgnoreCase ("list")) {
				Home home = new Home (args[0],player.getPosition (),player.dimension,player.rotationYaw,player.rotationPitch);
				PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
				data.addHome (home);
				DataHelper2.forceSave (Keys.PLAYER_DATA,data);
				ChatHelper.sendMessageTo (player,Local.HOME_SET.replaceAll ("#",home.getName ()));
			} else
				ChatHelper.sendMessageTo (player,Local.INVALID_HOME_NAME.replaceAll ("#",args[0]));
		} else {
			Home home = new Home (Settings.home_name,player.getPosition (),player.dimension,player.rotationYaw,player.rotationPitch);
			PlayerData data = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
			data.addHome (home);
			DataHelper2.forceSave (Keys.PLAYER_DATA,data);
			ChatHelper.sendMessageTo (player,Local.HOME_SET.replaceAll ("#",home.getName ()));
		}
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (args.length == 0)
			list.add (Settings.home_name);
		return list;
	}

	private HoverEvent hoverEvent (Home home) {
		//		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,DataHelper.displayLocation (home));
		return null;
	}

	@Override
	public String getDescription () {
		return "Allows you to set a home to be used via /home <name>";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}
}
