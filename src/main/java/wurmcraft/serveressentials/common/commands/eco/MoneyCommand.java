package wurmcraft.serveressentials.common.commands.eco;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class MoneyCommand extends SECommand {

  public MoneyCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "money";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/money <username>";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"m", "balance", "bal"};
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length == 0) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        PlayerData data = (PlayerData) DataHelper2
            .get(Keys.PLAYER_DATA, player.getGameProfile().getId().toString());
        ChatHelper.sendMessageTo(player, Local.CURRENT_MONEY.replaceAll("#", "" + data.getMoney()));
      }
    } else if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
			if (player != null) {
				PlayerData data = (PlayerData) DataHelper2
						.get(Keys.PLAYER_DATA, player.getGameProfile().getId().toString());
				ChatHelper.sendMessageTo(sender, Local.CURRENT_MONEY_OTHER.replaceAll("#",
						"" + UsernameCache.getLastKnownUsername(player.getGameProfile().getId()))
						.replaceAll("%", "" + data.getMoney()));
			} else {
				ChatHelper.sendMessageTo(sender, Local.PLAYER_NOT_FOUND.replaceAll("#", args[0]));
			}
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }

  @Override
  public String getDescription() {
    return "Checks a players balance";
  }
}
