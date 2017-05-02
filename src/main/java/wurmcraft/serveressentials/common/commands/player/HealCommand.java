package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HealCommand extends EssentialsCommand {

	public HealCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "heal";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/heal <name>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Heal");
		aliases.add ("HEAL");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1) {
			PlayerList players = server.getServer ().getPlayerList ();
			if (players.getPlayerList ().size () > 0)
				for (EntityPlayerMP user : players.getPlayerList ())
					if (user.getGameProfile ().getId ().equals (server.getServer ().getPlayerProfileCache ().getGameProfileForUsername (args[0]).getId ())) {
						user.setHealth (user.getMaxHealth ());
						user.addChatComponentMessage (new TextComponentString (Local.HEAL_OTHER));
						sender.addChatMessage (new TextComponentString (Local.HEAL_OTHER_SENDER.replaceAll ("#",user.getDisplayName ().getUnformattedText ())));
					}
		} else if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			player.setHealth (player.getMaxHealth ());
			player.addChatComponentMessage (new TextComponentString (Local.HEAL_SELF));
		} else
			sender.addChatMessage (new TextComponentString (getCommandUsage (sender)));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
