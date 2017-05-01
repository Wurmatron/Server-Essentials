package wurmcraft.serveressentials.common.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BroadcastCommand extends EssentialsCommand {

		public BroadcastCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "broadcast";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/broadcast <message>";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length > 0)
						FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString(Strings.join(args, " ")));
				else sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
		}
}
