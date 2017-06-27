package wurmcraft.serveressentials.common.commands.chat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static wurmcraft.serveressentials.common.utils.CommandUtils.getArgsAfterCommand;

public class NickCommand extends EssentialsCommand {

	public static final String usage = "/nick | /nick help | /nick set <nick> | /nick set <user> <nick> | /nick del " + "| /nick del <user> | /nick show | /nick show <user>";

	public NickCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "nick";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return usage;
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Nick");
		aliases.add ("NICK");
		return aliases;
	}

	@Override
	public String getDescription () {
		return "Changes a players name";
	}

	private static void printUsage (ICommandSender sender) {
		ChatHelper.sendMessageTo (sender,usage);
	}

	public static boolean printIsCorrectNicknameFormatting (EntityPlayer player,String nick) {
		if (nick.matches ("[^\\W.&\\-()]+")) {
			if (UsernameResolver.isValidNickname (nick)) {
				ChatHelper.sendMessageTo (player,TextFormatting.RED + "Nickname: '" + TextFormatting.AQUA + nick + TextFormatting.RED + "' already taken!");
				return false;
			}
			ChatHelper.sendMessageTo (player,TextFormatting.RED + "Incorrect nickname formatting! Allowed " + "characters: [A-Aa-z0-9_.&-()]");
			return false;
		}
		return true;
	}

	//TODO filter non: A-z0-9\-\_\+\=\&
	//TODO config for max length
	//TODO config for perm level required for: (nick set), (nick set user), (nick unset), (nick unset user), (nick show), (nick show user)
	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		String[] trailingArgs = getArgsAfterCommand (0,args);
		if (args.length > 0) {
			switch (args[0]) {
				case "help": {
					printUsage (sender);
					break;
				}
				case "set": {
					if (trailingArgs.length >= 2 && DataHelper.loadPlayerData (player.getGameProfile ().getId ()).getRank ().hasPermission (Perm.NICK)) {
						String rUser = trailingArgs[0];
						System.out.println (rUser);
						System.out.println (trailingArgs[1]);
						if (UsernameResolver.printIsValidPlayer (player,rUser) && printIsCorrectNicknameFormatting (player,trailingArgs[1])) {
							UsernameResolver.getPlayerData (rUser).setNickname (trailingArgs[1]);
							ChatHelper.sendMessageTo (sender,Local.NICKNAME_SET.replaceAll ("#",rUser));
							ChatHelper.sendMessageTo (server.getPlayerList ().getPlayerByUsername (rUser),TextFormatting.RED + "Your nickname has been set by: " + TextFormatting.DARK_RED + DataHelper.getPlayerData (player.getGameProfile ().getId ()).getNickname () + TextFormatting.RED + "!");
						} else
							break;
					} else if (trailingArgs.length == 1) {

						if (printIsCorrectNicknameFormatting (player,trailingArgs[0])) {
							UsernameResolver.getPlayerData (player.getGameProfile ().getId ()).setNickname (trailingArgs[0]);
							ChatHelper.sendMessageTo (sender,Local.NICKNAME_SET.replaceAll ("#'s n","N"));
						} else
							break;
					} else {
						printUsage (sender);
						break;
					}
				}
				case "unset": {
					if (trailingArgs.length >= 2 && DataHelper.loadPlayerData (player.getGameProfile ().getId ()).getRank ().hasPermission (Perm.NICK)) {
						String rUser = trailingArgs[0];
						if (UsernameResolver.printIsValidPlayer (player,rUser)) {
							UsernameResolver.getPlayerData (rUser).setNickname ("");
							ChatHelper.sendMessageTo (sender,TextFormatting.GREEN + rUser + "'s nickname has been cleared!");
							ChatHelper.sendMessageTo (server.getPlayerList ().getPlayerByUsername (rUser),TextFormatting.RED + "Your nickname has been cleared by: " + TextFormatting.DARK_RED + DataHelper.getPlayerData (player.getGameProfile ().getId ()).getNickname () + TextFormatting.RED + "!");
						} else
							break;
					} else if (trailingArgs.length == 1) {
						UsernameResolver.getPlayerData (player.getGameProfile ().getId ()).setNickname ("");
						ChatHelper.sendMessageTo (player,TextFormatting.GREEN + "Nickname cleared!");
					} else {
						printUsage (sender);
						break;
					}
				}
				case "show": {
					if (trailingArgs.length >= 1) {
						String user = trailingArgs[0];
						if (UsernameResolver.printIsValidPlayer (player,user)) {
							ChatHelper.sendMessageTo (sender,TextFormatting.GREEN + user + TextFormatting.AQUA + "'s nickname is: " + UsernameResolver.getPlayerData (user).getNickname ());
						} else
							break;
					} else {
						UsernameCache.getMap ().forEach ((uid,usern) -> {
							ChatHelper.sendMessageTo (sender,TextFormatting.GREEN + usern + TextFormatting.AQUA + "'s nickname is: " + UsernameResolver.getPlayerData (uid).getNickname ());
						});
					}
				}
				default: {
					printUsage (sender);
					break;
				}
			}
		} else {
			printUsage (sender);
		}
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoCompleteUsername (args,0);
	}
}
