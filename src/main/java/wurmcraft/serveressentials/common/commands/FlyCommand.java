package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;

public class FlyCommand extends EssentialsCommand {

	public FlyCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "fly";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/fly <username>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (args.length > 0) {
				PlayerList players = server.getServer ().getPlayerList ();
				if (players.getPlayerList ().size () > 0)
					for (EntityPlayerMP user : players.getPlayerList ())
						if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[0]).getId ())) {
							if (!user.capabilities.allowFlying) {
								user.capabilities.allowFlying = true;
								user.capabilities.isFlying = true;
								user.addChatComponentMessage (new TextComponentString ("Fly mode Activated"));
								player.addChatComponentMessage (new TextComponentString ("Fly mode Activated for #".replaceFirst ("#",user.getDisplayName ().getUnformattedText ())));
								user.sendPlayerAbilities ();
							} else {
								user.capabilities.allowFlying = false;
								user.addChatComponentMessage (new TextComponentString ("Fly mode Deactivated"));
								player.addChatComponentMessage (new TextComponentString ("Fly mode Deactivated for #".replaceFirst ("#",user.getDisplayName ().getUnformattedText ())));
								user.sendPlayerAbilities ();
							}
						}
			} else {
				if (!player.capabilities.allowFlying) {
					player.capabilities.allowFlying = true;
					player.addChatComponentMessage (new TextComponentString ("Fly mode Activated"));
					player.sendPlayerAbilities ();
				} else {
					player.capabilities.allowFlying = false;
					player.addChatComponentMessage (new TextComponentString ("Fly mode Deactivated"));
					player.sendPlayerAbilities ();
				}
			}
		}
	}
}
