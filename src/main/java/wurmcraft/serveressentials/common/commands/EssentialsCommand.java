package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class EssentialsCommand extends CommandBase {

		public final String perm;

		public EssentialsCommand(String perm) {
				this.perm = perm;
		}

		@Override
		public String getCommandName() {
				return null;
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return null;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		}

		@Override
		public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
				if (sender instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) sender;
						IRank        rank   = DataHelper.getPlayerData(player.getGameProfile().getId()).getRank();
						if (rank.getPermissions().length > 0) for (String perm : rank.getPermissions())
								if (perm != null) if (perm.equalsIgnoreCase(this.perm)) {
										return true;
								} else if (perm.startsWith("*")) {
										return true;
								} else if (perm.endsWith("*") && this.perm.startsWith(perm.substring(0, perm.indexOf("*")))) return true;
				} return false;
		}
}
