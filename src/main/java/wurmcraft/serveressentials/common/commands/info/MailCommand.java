package wurmcraft.serveressentials.common.commands.info;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import wurmcraft.serveressentials.common.api.storage.Mail;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.commands.utils.SubCommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.CommandUtils;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailCommand extends SECommand {

	public static final String usage = "/mail send <name> <message> | /mail list | /mail read | /mail delete <#>";

	public MailCommand (Perm perm) {
		super (perm);
	}

	private static void printUsage (ICommandSender sender) {
		ChatHelper.sendMessageTo (sender,usage);
	}

	private static List <Integer> parseMailIndices (EntityPlayer player,String[] args,Mail[] playerMail) {
		return new ArrayList <Integer> () {
			{
				for (String num : args) {
					try {
						int parsedInt = Integer.parseInt (num) - 1;
						if (parsedInt >= 0 && playerMail[parsedInt] != null)
							add (parsedInt);
						else
							throw new NumberFormatException ();
					} catch (NumberFormatException | IndexOutOfBoundsException e) {
						ChatHelper.sendMessageTo (player,Local.MAIL_INVALID.replaceAll ("#",num));
						continue;
					}
				}
			}
		};
	}

	private static ClickEvent clickEvent (int index) {
		return new ClickEvent (ClickEvent.Action.RUN_COMMAND,"/mail read #".replaceAll ("#","" + index));
	}

	@Override
	public String getName () {
		return "mail";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return usage;
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Send and read messages from offline players";
	}

	@SubCommand
	public void list (ICommandSender sender,String[] args) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		List <Mail> playerMail = ((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ().toString ())).getMail ();
		if (playerMail.size () > 0) {
			ChatHelper.sendMessageTo (sender.getCommandSenderEntity (),Local.SPACER);
			for (int index = 0; index < playerMail.size (); index++)
				ChatHelper.sendMessageTo (player,TextFormatting.GREEN + "[" + (index + 1) + "]: " + StringUtils.replaceEach (ConfigHandler.mailFormat,new String[] {"%username%","%message%"},new String[] {TextFormatting.AQUA + UsernameResolver.getUsername (playerMail.get (index).getSender ()),TextFormatting.GOLD + playerMail.get (index).getMessage ().replaceAll ("&","\u00A7")}),clickEvent (index + 1),null);
			ChatHelper.sendMessageTo (player,Local.SPACER);
		} else
			ChatHelper.sendMessageTo (player,Local.NO_MAIL);
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,1);
	}

	@SubCommand
	public void delete (ICommandSender sender,String[] args) {
		String[] trailingArgs = CommandUtils.getArgsAfterCommand (0,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		trailingArgs = (trailingArgs == null || trailingArgs[0] == null) ? new String[0] : trailingArgs;
		if (trailingArgs.length == 0) {
			printUsage (sender);
			return;
		} else {
			String username = player.getGameProfile ().getName ();
			if (trailingArgs.length == 1 && trailingArgs[0].equalsIgnoreCase ("all")) {
				PlayerData playerData = UsernameResolver.getPlayerData (player.getGameProfile ().getId ());
				playerData.getMail ().clear ();
				DataHelper2.forceSave (Keys.PLAYER_DATA,playerData);
				ChatHelper.sendMessageTo (player,TextFormatting.DARK_AQUA + "All mail deleted!");
			} else {
				Mail[] playerMail = UsernameResolver.getPlayerData (username).getMail ().toArray (new Mail[0]);
				for (int index : parseMailIndices (player,trailingArgs,playerMail)) {
					UsernameResolver.getPlayerData (username).getMail ().remove (index);
					ChatHelper.sendMessageTo (player,TextFormatting.DARK_AQUA + "Mail [" + index + "] deleted!");
				}
				DataHelper2.forceSave (Keys.PLAYER_DATA,UsernameResolver.getPlayerData (username));
			}
		}
	}

	@SubCommand
	public void read (ICommandSender sender,String[] args) {
		String[] trailingArgs = CommandUtils.getArgsAfterCommand (0,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (trailingArgs.length == 0) {
			list (sender,new String[0]);
			return;
		} else {
			String username = player.getGameProfile ().getName ();
			Mail[] playerMail = UsernameResolver.getPlayerData (username).getMail ().toArray (new Mail[0]);
			for (int index : parseMailIndices (player,trailingArgs,playerMail))
				ChatHelper.sendMessageTo (player,TextFormatting.GREEN + "[" + (index + 1) + "]: " + StringUtils.replaceEach (ConfigHandler.mailFormat,new String[] {"%username%","%message%"},new String[] {TextFormatting.AQUA + UsernameResolver.getUsername (playerMail[index].getSender ()),TextFormatting.GOLD + playerMail[index].getMessage ().replaceAll ("&","\u00A7")}));
		}
	}

	@SubCommand
	public void send (ICommandSender sender,String[] args) {
		String[] trailingArgs = CommandUtils.getArgsAfterCommand (0,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (trailingArgs.length == 0) {
			printUsage (player);
			return;
		}
		String uReceiver = trailingArgs[0];
		String receiver = UsernameResolver.usernameFromNickname (uReceiver);
		if (receiver == null) {
			ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll (((uReceiver == null) ? "\"#\" " : "\"#\""),((uReceiver == null) ? "" : uReceiver)));
			return;
		}
		if (trailingArgs.length == 1) {
			ChatHelper.sendMessageTo (player,Local.MISSING_MESSAGE);
			return;
		}
		UUID uuidReceiver = UsernameResolver.getPlayerUUID (receiver);
		PlayerData playerData = UsernameResolver.getPlayerData (uuidReceiver);
		playerData.addMail (new Mail (player.getGameProfile ().getId (),uuidReceiver,Strings.join (CommandUtils.getArgsAfterCommand (1,trailingArgs)," ")));
		ChatHelper.sendMessageTo (player,Local.MAIL_SENT);
		ChatHelper.sendMessageTo (UsernameResolver.getPlayer (uuidReceiver),Local.HAS_MAIL);
	}

	@Override
	public boolean hasSubCommand () {
		return true;
	}
}
