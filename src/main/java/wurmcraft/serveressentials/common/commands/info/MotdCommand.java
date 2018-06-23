package wurmcraft.serveressentials.common.commands.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Global;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class MotdCommand extends SECommand {

  private static final int LIST_SIZE = 7;

  public MotdCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "motd";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "motd";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    Global globalSettings = DataHelper2.globalSettings;
    if (globalSettings.getMotd() != null && globalSettings.getMotd().length > 0) {
      if (globalSettings.getMotd().length > LIST_SIZE) {
        if (args.length == 0) {
          String[] temp = Arrays.copyOfRange(globalSettings.getMotd(), 0, LIST_SIZE);
          for (String rule : temp) {
            ChatHelper.sendMessageTo(sender, rule.replaceAll("&", "\u00A7"));
          }
        } else {
          int pageNo = Integer.valueOf(args[0]);
          if (pageNo <= (globalSettings.getMotd().length / LIST_SIZE)) {
            String[] temp = Arrays.copyOfRange(globalSettings.getMotd(), (pageNo * LIST_SIZE),
                (pageNo * LIST_SIZE) + LIST_SIZE);
            for (String rule : temp) {
              if (rule != null && rule.length() > 0) {
                ChatHelper.sendMessageTo(sender, rule.replaceAll("&", "\u00A7"));
              }
            }
          } else {
            ChatHelper.sendMessageTo(sender, Local.PAGE_NONE.replaceAll("#", args[0])
                .replaceAll("$", (globalSettings.getMotd().length / LIST_SIZE) + ""));
          }
        }
      } else {
        for (String rule : globalSettings.getMotd()) {
          ChatHelper.sendMessageTo(sender, rule.replaceAll("&", "\u00A7"));
        }
      }
    } else {
      ChatHelper.sendMessageTo(sender, Local.NO_MOTD);
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    List<String> list = new ArrayList<>();
    for (int no = 0; no < DataHelper2.globalSettings.getMotd().length / 7; no++) {
      list.add("" + no);
    }
    return list;
  }

  @Override
  public String getDescription() {
    return "Displays the server's MOTD";
  }
}
