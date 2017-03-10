package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DelHome extends EssentialsCommand {

		public DelHome(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "delHome";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/delhome <name>";
		}

		@Override
		public List<String> getCommandAliases() {
				ArrayList<String> aliases = new ArrayList<>(); aliases.add("delhome"); aliases.add("dhome"); aliases.add("dHome");
				aliases.add("DHome"); return aliases;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (sender.getEntityWorld().isRemote) return; if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
						PlayerData   data   = DataHelper.getPlayerData(player.getGameProfile().getId());
						if (data == null) DataHelper.reloadPlayerData(player.getGameProfile().getId()); if (args.length == 0)
								sender.addChatMessage(new TextComponentString(DataHelper.deleteHome(player.getGameProfile().getId(), Settings.home_name)));
						else if (args.length == 1)
								sender.addChatMessage(new TextComponentString(DataHelper.deleteHome(player.getGameProfile().getId(), args[0])));
				} else sender.addChatMessage(new TextComponentString("Command can only be run by players!"));
		}

		@Override
		public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
				List<String> list = new ArrayList<>(); if (sender instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) sender;
						Home[]       homes  = DataHelper.getPlayerData(player.getGameProfile().getId()).getHomes();
						if (homes.length > 0) for (Home home : homes)
								if (home != null) list.add(home.getName());
				} return list;
		}
}
