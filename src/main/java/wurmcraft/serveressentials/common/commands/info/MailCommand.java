package wurmcraft.serveressentials.common.commands.info;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.Mail;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.ArrayUtils;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.*;

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
		return "/mail send <name> <message> | /mail list | /mail read | /mail delete <#>";
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

	private static void printUsage(ICommandSender sender) {
		ChatHelper.sendMessageTo (sender, usage);
	}

	private static String[] getArgsAfterCommand(int argPos, String[] args) {
		System.out.println("argPos: " + argPos + "; args.length: " + args.length);
		String[] toReturn = ArrayUtils.splice(args, argPos, args.length-1);
		System.out.println("toReturn.length: " + toReturn.length);
		return toReturn;
	}

	private static void listMail(ICommandSender sender) {
		EntityPlayer player=(EntityPlayer)sender.getCommandSenderEntity();
		List<Mail> playerMail = DataHelper.getPlayerData(((EntityPlayer)sender.getCommandSenderEntity()).getGameProfile().getId()).getMail();
		if (playerMail.size () > 0) {
			ChatHelper.sendMessageTo(sender.getCommandSenderEntity(), Local.SPACER);
			for (int index = 0; index < playerMail.size(); index++)
				ChatHelper.sendMessageTo(player, "["+(index+1)+"]: "+UsernameResolver.getUsername(playerMail.get(index).getSender()) +
						TextFormatting.DARK_GREEN+" -> " + playerMail.get(index).getMessage().replaceAll("&", "\u00A7"));
			ChatHelper.sendMessageTo(player, Local.SPACER);
		} else ChatHelper.sendMessageTo (player,Local.NO_MAIL);
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
		if (args.length > 0) {
			String[] trailingArgs = getArgsAfterCommand(1, args);
			trailingArgs=(trailingArgs==null||trailingArgs[0]==null)?new String[0]:trailingArgs;
			switch (args[0]) {
				case "send": {
					if (trailingArgs.length==0) {printUsage(player); break;}
					String uReceiver=trailingArgs[0];
					System.out.println(uReceiver);
					String receiver=UsernameResolver.usernameFromNickname(uReceiver);
					System.out.println(receiver);
					if (receiver==null) {ChatHelper.sendMessageTo(player,Local.PLAYER_NOT_FOUND.replaceAll(((uReceiver==null)?"\"#\" ":"\"#\""), ((uReceiver==null)?"":uReceiver))); break;}
					if (trailingArgs.length==1) {ChatHelper.sendMessageTo(player,Local.MISSING_MESSAGE); break;}
					UUID uuidReceiver = UsernameResolver.getPlayerUUID(receiver);
					DataHelper
						.addMail(new Mail(player.getGameProfile().getId(), uuidReceiver ,
								 Strings.join(getArgsAfterCommand(1, trailingArgs), " ")));
					ChatHelper.sendMessageTo (player,Local.MAIL_SENT);
					ChatHelper.sendMessageTo (UsernameResolver.getPlayer(uuidReceiver), Local.HAS_MAIL);
					break;
				}
				case "read": {
					System.out.println(trailingArgs.length);
					if (trailingArgs.length==0) {listMail(sender); break;}
					else {
						List<Integer> mailIndices = new LinkedList<>();
						String username=player.getGameProfile().getName();
						Mail[] playerMail=UsernameResolver.getPlayerData(username).getMail().toArray(new Mail[0]);
						for (String num : trailingArgs) {
							try {
								System.out.println(num);
								int parsedInt=Integer.parseInt(num)-1;
								if (parsedInt>=0&&parsedInt<playerMail.length) mailIndices.add(parsedInt);
								else throw new NumberFormatException();
							} catch(NumberFormatException e) {
								ChatHelper.sendMessageTo(player, Local.MAIL_INVALID.replaceAll("#", num));
								continue;
							}
						}
						for (int index : mailIndices)
							ChatHelper.sendMessageTo(player,  "["+(index+1)+"]: "+UsernameResolver.getUsername(playerMail[index].getSender())+
									TextFormatting.DARK_GREEN+" -> "+playerMail[index].getMessage().replaceAll("&","\u00A7"));
					}
					break;
				}
				case "list": listMail(sender); break;
				case "delete": {break;}
				default: {printUsage(sender); break;}
			}
		} else printUsage(sender);

//		if (args.length > 0) {

//			} else if (args[0].equalsIgnoreCase ("read") || args[0].equalsIgnoreCase ("list")) {
//				List <Mail> playerMail = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getMail ();
//				if (playerMail.size () > 0) {
//					ChatHelper.sendMessageTo (player,Local.SPACER);
//					for (int index = 0; index < playerMail.size (); index++)
//						ChatHelper.sendMessageTo (player,"[" + (index  + 1)+ "] " + UsernameCache.getLastKnownUsername (playerMail.get (index).getSender ()) + " " + playerMail.get (index).getMessage ().replaceAll ("&","\u00A7"));
//					ChatHelper.sendMessageTo (player,Local.SPACER);
//				} else
//					ChatHelper.sendMessageTo (player,Local.NO_MAIL);
//			} else if (args[0].equalsIgnoreCase ("delete") || args[0].equalsIgnoreCase ("del")) {
//				if (args.length == 2) {
//					Integer mailNo = Integer.valueOf (args[1]) - 1;
//					if (DataHelper.getPlayerData (player.getGameProfile ().getId ()).getMail ().size () >= mailNo) {
//						DataHelper.removeMail (player.getGameProfile ().getId (),mailNo);
//						ChatHelper.sendMessageTo (player,Local.MAIL_REMOVED);
//					} else
//						ChatHelper.sendMessageTo (player,Local.MAIL_INVALID.replaceAll ("#",args[1]));
//				} else if (args.length > 2) {
//					for (int a = 1; a < args.length; a++) {
//						String arg = args[a];
//						try {
//							int index = Integer.parseInt (arg) - 1;
//							if (index < DataHelper.getPlayerData (player.getGameProfile ().getId ()).getMail ().size ()) {
//								DataHelper.removeMail (player.getGameProfile ().getId (), index);
//							} else
//								ChatHelper.sendMessageTo (player,Local.MAIL_INVALID.replaceAll ("#",arg));
//						} catch (NumberFormatException e) {
//							ChatHelper.sendMessageTo (player,Local.INVALID_NUMBER.replaceAll ("#",arg));
//						}
//					}
//					ChatHelper.sendMessageTo (player,Local.MAIL_REMOVED);
//				} else
//					ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
//			} else if (args[0].equalsIgnoreCase ("deleteAll") || args[0].equalsIgnoreCase ("delAll")) {
//				for (int index = 0; index < DataHelper.getPlayerData (player.getGameProfile ().getId ()).getMail ().size (); index++)
//					DataHelper.removeMail (player.getGameProfile ().getId (),index-1);
//				ChatHelper.sendMessageTo (player,Local.MAIL_REMOVED_ALL);
//			} else
//				ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
//		} else
//			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}


	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> list = new ArrayList <> ();
		if (sender instanceof EntityPlayer)
			Collections.addAll (list,FMLCommonHandler.instance ().getMinecraftServerInstance ().getAllUsernames ());
		return list;
	}
}
