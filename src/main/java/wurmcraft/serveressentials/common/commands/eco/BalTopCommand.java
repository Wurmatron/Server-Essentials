package wurmcraft.serveressentials.common.commands.eco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class BalTopCommand extends SECommand {

  public BalTopCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "baltop";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"mtop"};
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/baltop";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    List<String> unknownPlayers = new ArrayList<>();
    HashMap<UUID, PlayerData> dataMap = new HashMap<UUID, PlayerData>() {
      {
        if (args.length == 0) {
          UsernameCache.getMap().forEach((uuid, s) -> put(uuid,
              ((PlayerData) DataHelper2.get(Keys.PLAYER_DATA, uuid.toString()))));
        } else {
          UsernameResolver.AbstractUsernameCollection<String> usernames = new UsernameResolver.AbstractUsernameCollection<String>(
              UsernameCache.getMap().values());
          for (String arg : args) {
            UsernameCache.getMap().forEach((uuid, s) -> {
							if (s.equalsIgnoreCase(arg)) {
								put(uuid, (((PlayerData) DataHelper2.get(Keys.PLAYER_DATA, uuid.toString()))));
							} else if (!usernames.contains(s)) {
								unknownPlayers.add(s);
							}
            });
          }
        }
      }
    };
    UUID[] keys = dataMap.keySet().toArray(new UUID[0]);
    for (int i = 0; i < ConfigHandler.topMaxDisplay && i < keys.length; i++) {
      unknownPlayers.forEach(s -> ChatHelper
          .sendMessageTo(sender, TextFormatting.RED + "Unknown Player: '" + s + "'"));
      String formatted = ConfigHandler.currencySymbol + dataMap.get(keys[i]).getMoney();
      ChatHelper.sendMessageTo(sender,
          TextFormatting.GREEN + UsernameCache.getLastKnownUsername(keys[i])
              + TextFormatting.DARK_AQUA + " : " + formatted);
    }
  }


  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription() {
    return "Find out how long you have played on the server.";
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }
}
