package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class AfkCommand extends EssentialsCommand {

		public AfkCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "afk";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/afk";
		}


		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
						if (!DataHelper.isAfk(player.getGameProfile().getId())) {
								FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString("# is now afk!".replaceAll("#", player.getDisplayNameString())));
								DataHelper.addAfkPlayer(player);
						} else {
								FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString("# is no longer afk!".replaceAll("#", player.getDisplayNameString())));
								DataHelper.addAfkPlayer(player); DataHelper.removeAfkPlayer(player);
						}
				}
		}
}
