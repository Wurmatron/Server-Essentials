package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class DelWarp extends EssentialsCommand {

		public DelWarp(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "delWarp";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/delwarp <name>";
		}

		@Override
		public List<String> getCommandAliases() {
				ArrayList<String> aliases = new ArrayList<>(); aliases.add("deletewarp"); aliases.add("dwarp"); return aliases;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length > 0) {
						if (DataHelper.getWarp(args[0]) != null) {
								Warp warp = DataHelper.getWarp(args[0]); DataHelper.deleteWarp(DataHelper.getWarp(args[0]));
								sender.addChatMessage(new TextComponentString(Local.WARP_DELETE.replaceAll("#", warp.getName())));
						}
				} else sender.addChatMessage(new TextComponentString(TextFormatting.RED + Local.WARP_NONE));
		}
}
