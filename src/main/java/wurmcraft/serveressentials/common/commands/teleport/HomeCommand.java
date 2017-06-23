package wurmcraft.serveressentials.common.commands.teleport;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.TeleportUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HomeCommand extends EssentialsCommand {

	public HomeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "home";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/home <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Home");
		aliases.add ("HOME");
		aliases.add ("h");
		aliases.add ("H");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity ();
		if (args.length == 0) {
			Home home = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getHome (Settings.home_name);
			long teleport_timer = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeleportTimer ();
			if (home != null && (teleport_timer + (Settings.teleport_cooldown * 1000)) <= System.currentTimeMillis ()) {
				DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
				player.setLocationAndAngles (home.getPos ().getX (),home.getPos ().getY (),home.getPos ().getZ (),home.getYaw (),home.getPitch ());
				TeleportUtils.teleportTo (player,home.getPos (),home.getDimension (),true);
				ChatHelper.sendMessageTo (player,TextFormatting.AQUA + Local.HOME_TELEPORTED.replace ("#",home.getName ()),hoverEvent (home));
			} else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis ())
				ChatHelper.sendMessageTo (player,Local.TELEPORT_COOLDOWN.replace ("#",TeleportUtils.getRemainingCooldown (player.getGameProfile ().getId ())));
			else
				ChatHelper.sendMessageTo (sender,Local.HOME_NONE);
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase ("list")) {
				PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
				if (data == null)
					DataHelper.reloadPlayerData (player.getGameProfile ().getId ());
				if (data.getHomes ().length > 0) {
					ArrayList <String> homes = new ArrayList <> ();
					for (Home h : data.getHomes ())
						if (h != null)
							homes.add (h.getName ());
					if (homes.size () > 0)
						ChatHelper.sendMessageTo (sender,TextFormatting.DARK_AQUA + "Homes: " + TextFormatting.AQUA + Strings.join (homes.toArray (new String[0]),", "));
					else
						ChatHelper.sendMessageTo (sender,Local.HOME_NONEXISTENT);
				} else
					ChatHelper.sendMessageTo (sender,Local.HOME_NONEXISTENT);
			} else {
				Home home = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getHome (args[0]);
				long teleport_timer = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getTeleportTimer ();
				if (home != null && (teleport_timer + (Settings.teleport_cooldown * 1000)) <= System.currentTimeMillis ()) {
					DataHelper.setLastLocation (player.getGameProfile ().getId (),player.getPosition ());
					player.setLocationAndAngles (home.getPos ().getX (),home.getPos ().getY (),home.getPos ().getZ (),home.getYaw (),home.getPitch ());
					TeleportUtils.teleportTo (player,home.getPos (),home.getDimension (),true);
					ChatHelper.sendMessageTo (player,TextFormatting.AQUA + Local.HOME_TELEPORTED.replace ("#",home.getName ()),hoverEvent (home));
				} else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis ())
					ChatHelper.sendMessageTo (player,Local.TELEPORT_COOLDOWN.replace ("#",TeleportUtils.getRemainingCooldown (player.getGameProfile ().getId ())));
				else
					ChatHelper.sendMessageTo (sender,Local.HOME_INVALID.replaceAll ("#",args[0]));
			}
		}
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			Home[] homes = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getHomes ();
			if (homes.length > 0)
				for (Home home : homes)
					if (home != null)
						list.add (home.getName ());
		}
		return list;
	}

	private HoverEvent hoverEvent (Home home) {
		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,DataHelper.displayLocation (home));
	}

	@Override
	public String getDescription () {
		return "Allows you to set a \"home\" and teleport to it later.";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}
}
