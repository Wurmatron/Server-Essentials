package wurmcraft.serveressentials.common.commands.info;


import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.AutoRank;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;

public class AutoRankCommand extends SECommand {

  public AutoRankCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "autoRank";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"aRank"};
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/autoRank | /autoRank <user>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      boolean hasNext = false;
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      PlayerData data = (PlayerData) DataHelper2
          .get(Keys.PLAYER_DATA, player.getGameProfile().getId().toString());
      for (IDataType f : DataHelper2.getData(Keys.AUTO_RANK)) {
        AutoRank autoRank = (AutoRank) f;
        if (autoRank.getRank().equalsIgnoreCase(data.getRank().getName())) {
          hasNext = true;
          ChatHelper.sendMessageTo(player, TextFormatting.AQUA + Local.SPACER);
          ChatHelper.sendMessageTo(player, Local.NEXT_RANK
              .replaceAll("#", RankManager.getRankFromName(autoRank.getNextRank()).getName()));
          ChatHelper.sendMessageTo(player, checkAndFormatOnlineTime(data, autoRank));
          ChatHelper.sendMessageTo(player, checkAndFormatExp(player, autoRank));
          ChatHelper.sendMessageTo(player, checkAndFormatBal(data, autoRank));
          ChatHelper.sendMessageTo(player, TextFormatting.AQUA + Local.SPACER);
        }
      }
      if (!hasNext) {
        ChatHelper.sendMessageTo(player, Local.RANK_MAX);
      }
    } else if (args.length == 1) {
      boolean hasNext = false;
      for (EntityPlayer player : server.getPlayerList().getPlayers()) {
        if (UsernameCache.getLastKnownUsername(player.getGameProfile().getId())
            .equalsIgnoreCase(args[0])) {
          PlayerData data = (PlayerData) DataHelper2
              .get(Keys.PLAYER_DATA, player.getGameProfile().getId().toString());
          for (IDataType f : DataHelper2.getData(Keys.AUTO_RANK)) {
            AutoRank autoRank = (AutoRank) f;
            if (autoRank.getRank().equalsIgnoreCase(data.getRank().getName())) {
              hasNext = true;
              ChatHelper.sendMessageTo(player, TextFormatting.AQUA + Local.SPACER);
              ChatHelper.sendMessageTo(player, Local.NEXT_RANK
                  .replaceAll("#", RankManager.getRankFromName(autoRank.getNextRank()).getName()));
              ChatHelper.sendMessageTo(player, checkAndFormatOnlineTime(data, autoRank));
              ChatHelper.sendMessageTo(player, checkAndFormatExp(player, autoRank));
              ChatHelper.sendMessageTo(player, checkAndFormatBal(data, autoRank));
              ChatHelper.sendMessageTo(player, TextFormatting.AQUA + Local.SPACER);
            }
            if (!hasNext) {
              ChatHelper.sendMessageTo(player, Local.RANK_MAX);
            }
          }
        }
      }
    }
  }

  @Override
  public String getDescription() {
    return "Checks the status of a player's Rank Up";
  }


  private String checkAndFormatOnlineTime(PlayerData data, AutoRank rank) {
    if (data.getOnlineTime() >= rank.getPlayTime()) {
      return TextFormatting.AQUA + Local.ONLINE_TIME
          .replaceAll("#", TextFormatting.GREEN + "" + rank.getPlayTime());
    } else {
      return TextFormatting.AQUA + Local.ONLINE_TIME
          .replaceAll("#", TextFormatting.RED + "" + rank.getPlayTime());
    }
  }

  private String checkAndFormatExp(EntityPlayer player, AutoRank rank) {
    if (player.experienceLevel >= rank.getExp()) {
      return TextFormatting.AQUA + Local.EXPERIENCE
          .replaceAll("#", TextFormatting.GREEN + "" + rank.getExp());
    } else {
      return TextFormatting.AQUA + Local.EXPERIENCE
          .replaceAll("#", TextFormatting.RED + "" + rank.getExp());
    }
  }

  private String checkAndFormatBal(PlayerData data, AutoRank rank) {
    if (data.getMoney() >= rank.getBalance()) {
      return TextFormatting.AQUA + Local.BALANCE
          .replaceAll("#", TextFormatting.GREEN + "" + rank.getBalance());
    } else {
      return TextFormatting.AQUA + Local.BALANCE
          .replaceAll("#", TextFormatting.RED + "" + rank.getBalance());
    }
  }


  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
