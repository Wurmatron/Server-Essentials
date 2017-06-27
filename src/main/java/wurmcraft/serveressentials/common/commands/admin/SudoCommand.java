package wurmcraft.serveressentials.common.commands.admin;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudoCommand extends EssentialsCommand {

	public SudoCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "sudo";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/sudo <name> <command>";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("su");
		aliases.add ("SU");
		aliases.add ("Sudo");
		aliases.add ("SUDO");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer victim = UsernameResolver.getPlayer (args[0]);
			if (victim != null) {
				if (args.length >= 2) {
					victim.addChatComponentMessage (new TextComponentString (Local.COMMAND_FORCED));
					String command = Strings.join (Arrays.copyOfRange (args,1,args.length)," ");
					FMLCommonHandler.instance ().getMinecraftServerInstance ().getCommandManager ().executeCommand (victim,command);
					ChatHelper.sendMessageTo (sender,Local.COMMAND_SENDER_FORCED.replaceAll ("#",victim.getDisplayName ().getUnformattedText ()) + "/" + command);
				} else
					ChatHelper.sendMessageTo (sender,Local.COMMAND_NOT_FOUND);
			} else
				ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll ("#",args[0]));
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}

	@Override
	public String getDescription () {
		return "Runs a command as someone else";
	}

	@Override
	public boolean checkPermission (MinecraftServer server,ICommandSender sender) {
		if (Settings.securityModule && sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			return super.checkPermission (server,sender) && SecurityUtils.isTrustedMember (player);
		}
		return super.checkPermission (server,sender);
	}
}
