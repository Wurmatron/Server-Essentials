package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.CommandUtils;
import java.util.ArrayList;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class SetMOTDCommand extends SECommand {

  @Override
  public String getName() {
    return "setMOTD";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length > 0) {
      if (CommandUtils.isNumberAtPos(args, 0)) {
        int index = Integer.parseInt(args[0]);
        if (multiLineChange(args)) {
          List<String> lines = new ArrayList<>();
          StringBuilder l = new StringBuilder();
          for (String line : args) {
            if (line.contains("\n")) {
              l.append(line, 0, line.indexOf('\n'));
              if (!line.endsWith("\n")) {
                lines.add(l.toString());
                changeMOTDLine(l.toString(), index);
                l.setLength(0);
                index++;
                l.append(line.substring(line.indexOf('\n') + 1));
              } else {
                lines.add(l.toString());
                changeMOTDLine(l.toString(), index);
                l.setLength(0);
                index++;
              }
            } else {
              l.append(line);
            }
          }
        } else {
          changeMOTDLine(Strings.join(args, " "), index);
        }
      } else {
        DataHelper.globalSettings.addMotd(Strings.join(args, " "));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  private static void changeMOTDLine(String line, int index) {
    String[] motd = DataHelper.globalSettings.getMotd();
    motd[index] = line;
    DataHelper.globalSettings.setMotd(motd);
  }

  private static boolean multiLineChange(String[] args) {
    for (String arg : args) {
      if (arg.contains("\n")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
