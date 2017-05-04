package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
			boolean playerFound = false;
			if (args.length > 0) {
				PlayerList players = server.getServer ().getPlayerList ();
				if (players.getPlayerList ().size () > 0)
					for (EntityPlayerMP user : players.getPlayerList ())
						if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[0]).getId ())) {
							playerFound = true;
							if (!user.capabilities.allowFlying) {
								user.capabilities.allowFlying = true;
								user.capabilities.isFlying = true;
								ChatHelper.sendMessageTo (user,Local.FLY_ENABLED);
								ChatHelper.sendMessageTo (player,Local.FLY_ENABLED_OTHER.replaceFirst ("#",user.getDisplayName ().getUnformattedText ()));
								user.sendPlayerAbilities ();
							} else {
								user.capabilities.allowFlying = false;
								ChatHelper.sendMessageTo (user,Local.FLY_DISABLED);
								ChatHelper.sendMessageTo (player,Local.FLY_DISABLED_OTHER.replaceFirst ("#",user.getDisplayName ().getUnformattedText ()));
								user.sendPlayerAbilities ();
							}
						}
				if (!playerFound)
					ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
			} else {
				if (!player.capabilities.allowFlying) {
					player.capabilities.allowFlying = true;
					ChatHelper.sendMessageTo (player,Local.FLY_ENABLED);
					player.sendPlayerAbilities ();
				} else {
					player.capabilities.allowFlying = false;
					ChatHelper.sendMessageTo (player,Local.FLY_DISABLED);
					player.sendPlayerAbilities ();
				}
			}
		} else
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
