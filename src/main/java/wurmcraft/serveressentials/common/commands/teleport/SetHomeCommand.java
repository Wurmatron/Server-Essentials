package wurmcraft.serveressentials.common.commands.teleport;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SetHomeCommand extends EssentialsCommand {

	public SetHomeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "setHome";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/sethome <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("SetHome");
		aliases.add ("setHome");
		aliases.add ("SETHOME");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (args != null && args.length > 0) {
				Home home = new Home (args[0],player.getPosition (),player.dimension,player.rotationYaw,player.rotationPitch);
				TextComponentString defaultHome = new TextComponentString (DataHelper.addPlayerHome (player.getGameProfile ().getId (),home));
				defaultHome.getStyle ().setHoverEvent (hoverEvent (home));
				sender.addChatMessage (defaultHome);
			} else {
				Home home = new Home (Settings.home_name,player.getPosition (),player.dimension,player.rotationYaw,player.rotationPitch);
				TextComponentString nameHome = new TextComponentString (DataHelper.addPlayerHome (player.getGameProfile ().getId (),home));
				nameHome.getStyle ().setHoverEvent (hoverEvent (home));
				sender.addChatMessage (nameHome);
			}
		} else
			sender.addChatMessage (new TextComponentString ("Command can only be run by players!"));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (args.length == 0)
			list.add (Settings.home_name);
		return list;
	}

	public HoverEvent hoverEvent (Home home) {
		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,DataHelper.displayLocation (home));
	}
}
