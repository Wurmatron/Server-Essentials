package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.RankManager;

public class EssentialsCommand extends CommandBase {

	public final String perm;

	public EssentialsCommand (String perm) {
		this.perm = perm;
	}

	@Override
	public String getCommandName () {
		return null;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return null;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (isPlayerOnly () && sender.getCommandSenderEntity () instanceof EntityPlayer) {

		} else if (!isPlayerOnly () && !(sender.getCommandSenderEntity () instanceof EntityPlayer))
			ChatHelper.sendMessageTo (sender,Local.PLAYER_ONLY);
	}

	private static boolean checkPerms(IRank rank, String perm) {
		if (rank.getPermissions ().length > 0)
			for (String rPerm : rank.getPermissions ())
				if (rPerm != null)
					if (rPerm.equalsIgnoreCase (perm)) {
						return true;
					} else if (rPerm.startsWith ("*")) {
						return true;
					} else if (rPerm.endsWith ("*") && perm.startsWith (rPerm.substring (0,rPerm.indexOf ("*"))))
						return true;
		return false;
	}

	@Override
	public boolean checkPermission (MinecraftServer server,ICommandSender sender) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			IRank rank = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getRank ();
			if (checkPerms(rank, this.perm)) return true;
			if (rank.getInheritance () != null && rank.getInheritance ().length > 0) {
				for (String preRank : rank.getInheritance ())
					if (RankManager.getRankFromName (preRank) != null) {
						IRank tempRank = RankManager.getRankFromName (preRank);
						if (checkPerms(tempRank, this.perm)) return true;
					}
			}
		} else if (sender.getCommandSenderEntity () == null)
			return true;
		return false;
	}

	public String getDescription () {
		return getCommandUsage (null);
	}

	public Boolean isPlayerOnly () {
		return null;
	}
}
