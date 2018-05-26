package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class GodCommand extends SECommand {

	public GodCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "god";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Disabled Damage";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/god <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length == 0) {
			player.capabilities.disableDamage = !player.capabilities.disableDamage;
			player.sendMessage (new TextComponentString (player.capabilities.disableDamage ? Local.GOD_ENABLED : Local.GOD_DISABLED));
		} else if (args.length == 1) {
			EntityPlayer target = UsernameResolver.getPlayer (args[0]);
			target.capabilities.disableDamage = !target.capabilities.disableDamage;
			target.sendMessage (new TextComponentString (target.capabilities.disableDamage ? Local.GOD_ENABLED : Local.GOD_DISABLED));
		} else
			sender.sendMessage (new TextComponentString (getUsage (sender)));
	}
}
