package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.SpawnPoint;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand extends EssentialsCommand {

	public SetSpawnCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "setSpawn";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/setSpawn";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("SetSpawn");
		aliases.add ("setspawn");
		aliases.add ("Setspawn");
		aliases.add ("SETSPAWN");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			DataHelper.globalSettings.setSpawn (new SpawnPoint (player.getPosition (),player.rotationYaw,player.rotationPitch));
			player.worldObj.setSpawnPoint (player.getPosition ());
			TextComponentString text = new TextComponentString (Local.SPAWN_SET.replaceAll ("@","" + DataHelper.globalSettings.getSpawn ().dimension));
			text.getStyle ().setHoverEvent (hoverEvent (DataHelper.globalSettings.getSpawn ()));
			player.addChatComponentMessage (text);
		} else
			sender.addChatMessage (new TextComponentString ("Command can only be run by players!"));
	}

	public HoverEvent hoverEvent (SpawnPoint home) {
		return new HoverEvent (HoverEvent.Action.SHOW_TEXT,DataHelper.displayLocation (home));
	}
}
