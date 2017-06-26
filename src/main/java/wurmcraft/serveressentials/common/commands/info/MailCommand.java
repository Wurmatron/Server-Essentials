package wurmcraft.serveressentials.common.commands.info;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import wurmcraft.serveressentials.common.api.storage.Mail;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ArrayUtils;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailCommand extends EssentialsCommand {

	public static final String usage = "/mail send <name> <message> | /mail list | /mail read | /mail delete <#>";

	public MailCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "mail";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/mail send <name> <message> | /mail list | /mail read | /mail delete <# OR all>";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Send and read messages from offline players";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Mail");
		aliases.add ("MAIL");
		return aliases;
	}

	private static void printUsage (ICommandSender sender) {
		ChatHelper.sendMessageTo (sender,usage);
	}

	private static String[] getArgsAfterCommand (int argPos,String[] args) {
		return ArrayUtils.splice (args,argPos,args.length - 1);
	}

	private static void listMail (ICommandSender sender) {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		List <Mail> playerMail = DataHelper.getPlayerData (((EntityPlayer) sender.getCommandSenderEntity ()).getGameProfile ().getId ()).getMail ();
		if (playerMail.size () > 0) {
			ChatHelper.sendMessageTo (sender.getCommandSenderEntity (),Local.SPACER);
			for (int index = 0; index < playerMail.size (); index++)
				ChatHelper.sendMessageTo (player,TextFormatting.GREEN + "[" + (index + 1) + "]: " + StringUtils.replaceEach (Settings.mailFormat,new String[] {"%username%","%message%"},new String[] {TextFormatting.AQUA + UsernameResolver.getUsername (playerMail.get (index).getSender ()),TextFormatting.GOLD + playerMail.get (index).getMessage ().replaceAll ("&","\u00A7")}));
			ChatHelper.sendMessageTo (player,Local.SPACER);
		} else
			ChatHelper.sendMessageTo (player,Local.NO_MAIL);
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

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length > 0) {
			String[] trailingArgs = getArgsAfterCommand (1,args);
			trailingArgs = (trailingArgs == null || trailingArgs[0] == null) ? new String[0] : trailingArgs;
			switch (args[0]) {
				case "send": {
					if (trailingArgs.length == 0) {
						printUsage (player);
						break;
					}
					String uReceiver = trailingArgs[0];
					String receiver = UsernameResolver.usernameFromNickname (uReceiver);
					if (receiver == null) {
						ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll (((uReceiver == null) ? "\"#\" " : "\"#\""),((uReceiver == null) ? "" : uReceiver)));
						break;
					}
					if (trailingArgs.length == 1) {
						ChatHelper.sendMessageTo (player,Local.MISSING_MESSAGE);
						break;
					}
					UUID uuidReceiver = UsernameResolver.getPlayerUUID (receiver);
					DataHelper.addMail (new Mail (player.getGameProfile ().getId (),uuidReceiver,Strings.join (getArgsAfterCommand (1,trailingArgs)," ")));
					ChatHelper.sendMessageTo (player,Local.MAIL_SENT);
					ChatHelper.sendMessageTo (UsernameResolver.getPlayer (uuidReceiver),Local.HAS_MAIL);
					break;
				}
				case "read": {
					if (trailingArgs.length == 0) {
						listMail (sender);
						break;
					} else {
						String username = player.getGameProfile ().getName ();
						Mail[] playerMail = UsernameResolver.getPlayerData (username).getMail ().toArray (new Mail[0]);
						for (int index : parseMailIndices (player,trailingArgs,playerMail))
							ChatHelper.sendMessageTo (player,TextFormatting.GREEN + "[" + (index + 1) + "]: " + StringUtils.replaceEach (Settings.mailFormat,new String[] {"%username%","%message%"},new String[] {TextFormatting.AQUA + UsernameResolver.getUsername (playerMail[index].getSender ()),TextFormatting.GOLD + playerMail[index].getMessage ().replaceAll ("&","\u00A7")}));
					}
					break;
				}
				case "list":
					listMail (sender);
					break;
				case "delete": {
					if (trailingArgs.length == 0) {
						printUsage (sender);
						break;
					} else {
						String username = player.getGameProfile ().getName ();
						if (trailingArgs.length == 1 && trailingArgs[0].equalsIgnoreCase ("all")) {
							DataHelper.loadPlayerData (player.getGameProfile ().getId ()).getMail ().clear ();
							ChatHelper.sendMessageTo (player,TextFormatting.DARK_AQUA + "All mail deleted!");
						} else {
							Mail[] playerMail = UsernameResolver.getPlayerData (username).getMail ().toArray (new Mail[0]);
							for (int index : parseMailIndices (player,trailingArgs,playerMail)) {
								DataHelper.loadPlayerData (player.getGameProfile ().getId ()).removeMail (index);
								ChatHelper.sendMessageTo (player,TextFormatting.DARK_AQUA + "Mail [" + index + "] deleted!");
							}
						}
					}
					break;
				}
				default: {
					printUsage (sender);
					break;
				}
			}
		} else
			printUsage (sender);
	}


	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,1);
	}
}
