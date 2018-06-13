package wurmcraft.serveressentials.common.commands.admin;

import java.io.File;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.permissions.Rank;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class ReloadRankCommand extends SECommand {

  public ReloadRankCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "reloadRank";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"reloadRank", "rr"};
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/reloadRank <name>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length == 1 && args[0] != null) {
      IRank rank = RankManager.getRankFromName(args[0]);
      if (rank != null) {
        RankManager.removeRank(rank);
        File file = new File(
            ConfigHandler.saveLocation + File.separator + Keys.RANK.name() + File.separator + rank
                .getName() + ".json");
        Rank r = DataHelper2.load(file, Keys.TEAM, new Rank());
        RankManager.registerRank(r);
        ChatHelper
            .sendMessageTo(sender, Local.RANK_RELOAD.replaceAll("#", r.getName()));
      } else {
        ChatHelper.sendMessageTo(sender, Local.RANK_NOT_FOUND.replaceAll("#", args[0]));
      }
    } else {
      ChatHelper.sendMessageTo(sender, getUsage(sender));
    }
  }

  @Override
  public String getDescription() {
    return "Reloads a certain ranks's data";
  }
}
