package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.modules.general.utils.GeneralUtils;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import java.util.Arrays;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "General")
public class RulesCommand extends Command {

  private static final List<String> emptyAutoCompletion = Arrays.asList("set");

  @Override
  public String getName() {
    return "rules";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/rules <set> <lineNo.> <msg>...";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_RULES;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      if (GeneralModule.config != null
          && GeneralModule.config.rules != null
          && GeneralModule.config.rules.length > 0) {
        Arrays.stream(GeneralModule.config.rules)
            .forEach(msg -> ChatHelper.sendMessage(sender, msg));
      }
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void set(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 1) {
      try {
        int lineNo = Integer.parseInt(args[0]);
        String rules = Strings.join(Arrays.copyOfRange(args, 1, args.length), " ");
        GeneralUtils.setRulesLine(lineNo, rules);
        ChatHelper.sendMessage(
            sender,
            senderLang
                .local
                .GENERAL_RULES_SET
                .replaceAll(Replacment.LINE, lineNo + "")
                .replaceAll(Replacment.DATA, rules));
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0 || args.length == 1) {
      return emptyAutoCompletion;
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }
}
