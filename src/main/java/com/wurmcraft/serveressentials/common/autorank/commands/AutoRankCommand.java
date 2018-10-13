package com.wurmcraft.serveressentials.common.autorank.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.file.AutoRank;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.autorank.AutoRankModule;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@Command(moduleName = "AutoRank")
public class AutoRankCommand extends SECommand {

  @Override
  public String getName() {
    return "autorank";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A7b/ar check";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      check(sender, args);
    }
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  // TODO Non Rest Support
  @SubCommand
  public void check(ICommandSender sender, String[] args) {
    if (args.length == 0) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        AutoRank auto =
            AutoRankModule.getAutorankFromRank(
                UserManager.getPlayerRank(player.getGameProfile().getId()));
        displayAutoRank(sender, auto);
      }
    } else if (args.length == 1) {
      GlobalUser user = forceUserFromUUID(UsernameResolver.getUUIDFromName(args[0]));
      if (user != null) {
        AutoRank auto = AutoRankModule.getAutorankFromRank(user.getRank());
        displayAutoRank(sender, auto);
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    }
  }

  private void displayAutoRank(ICommandSender sender, AutoRank auto) {
    if (auto != null) {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
      ChatHelper.sendMessage(
          sender, getCurrentLanguage(sender).CHAT_RANK + ": " + auto.getNextRank());
      ChatHelper.sendMessage(
          sender, getCurrentLanguage(sender).CHAT_TIME + ": " + auto.getPlayTime());
      ChatHelper.sendMessage(sender, auto.getBalance() + " " + ConfigHandler.globalCurrency);
      ChatHelper.sendMessage(sender, auto.getExp() + " EXP");
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).NO_RANKUP);
    }
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("ar");
    return alts;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_AUTORANK;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return autoCompleteUsername(args, 0);
  }
}
