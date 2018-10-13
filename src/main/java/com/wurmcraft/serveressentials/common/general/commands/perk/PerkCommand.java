package com.wurmcraft.serveressentials.common.general.commands.perk;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.IPerk;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.commands.utils.Perk;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Command(moduleName = "General")
public class PerkCommand extends SECommand {

  public static NonBlockingHashMap<String, IPerk> perks = new NonBlockingHashMap<>();

  @Override
  public String getName() {
    return "perk";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/perk <list|buy> <name>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @SubCommand
  public void list(ICommandSender sender, String[] args) {
    ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    for (String name : perks.keySet()) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        GlobalUser user =
            (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
        ChatHelper.sendMessage(
            sender, TextFormatting.LIGHT_PURPLE + name + " " + (perks.get(name).getCost(user)));
      } else {
        ChatHelper.sendMessage(sender, TextFormatting.LIGHT_PURPLE + name);
      }
    }
    ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
  }

  @SubCommand
  public void buy(ICommandSender sender, String[] args) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      Perk perk = getPerk(args[0]);
      if (perk != null) {
        double cost = perk.getCost(user);
        double amt = user.getBank().getCurrency(ConfigHandler.globalCurrency);
        if (perk.getCost(user) <= user.getBank().getCurrency(ConfigHandler.globalCurrency)) {
          int currentLvl = getLevel(user, perk);
          user.delPerk(perk.perk);
          currentLvl++;
          user.addPerk(perk.getPerk().replace("%LEVEL%", currentLvl + ""));
          RequestHelper.UserResponses.overridePlayerData(user);
          ChatHelper.sendMessage(
              sender,
              getCurrentLanguage(sender)
                  .BUY_PERK
                  .replaceAll("%PERK%", perk.name)
                  .replaceAll("%COST%", "" + perk.getCost(user)));
        } else {
          ChatHelper.sendMessage(sender, getCurrentLanguage(sender).NO_MONEY);
        }
      }
    } else {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).PLAYER_ONLY);
    }
  }

  public Perk getPerk(String key) {
    for (String name : perks.keySet()) {
      if (name.equalsIgnoreCase(key)) {
        return (Perk) perks.get(name);
      }
    }
    return null;
  }

  private int getLevel(GlobalUser user, Perk perk) {
    for (String perm : user.getPerks()) {
      String a = perm.substring(0, perm.lastIndexOf("."));
      String b = perk.getPerk().substring(0, perk.getPerk().lastIndexOf("."));
      if (a.equalsIgnoreCase(b)) {
        return Integer.parseInt(perm.substring(perm.lastIndexOf(".") + 1));
      }
    }
    return 0;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    List<String> possible = new ArrayList<>();
    if (args.length >= 1 && args[0].equalsIgnoreCase("buy")) {
      for (String name : perks.keySet()) possible.add(name);
    }
    return possible;
  }
}
