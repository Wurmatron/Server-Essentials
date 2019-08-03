package com.wurmcraft.serveressentials.common.modules.economy.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.api.storage.json.Reward;
import com.wurmcraft.serveressentials.common.modules.economy.utils.RewardUtils;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Economy")
public class RewardCommand extends Command {

  @Override
  public String getName() {
    return "Reward";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/Reward <admin, list> <add | remove> <chance> <tier>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_REWARD;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 || args.length == 1) {
      if (UserManager.getRewards((EntityPlayer) sender.getCommandSenderEntity()) > 0) {
        Reward reward;
        try {
          reward =
              RewardUtils.getRandomReward(
                  sender.getEntityWorld(), args.length == 1 ? Integer.parseInt(args[0]) : 0);
        } catch (NumberFormatException e) {
          ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
          return;
        }
        ((EntityPlayer) sender.getCommandSenderEntity())
            .inventory.addItemStackToInventory(reward.getStack());
        ChatHelper.sendMessage(
            sender, senderLang.local.ECO_REWARD_GIVE.replaceAll(Replacment.TIER, "" + reward.tier));
        UserManager.consumeReward(((EntityPlayer) sender.getCommandSenderEntity()), 1);
      } else {
        ChatHelper.sendMessage(sender, senderLang.local.ECO_REWARD_POINTS_EMPTY);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void list(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    for (FileType reward : DataHelper.getData(Storage.REWARD)) {
      ChatHelper.sendMessage(
          sender,
          ((Reward) reward).getStack().getDisplayName()
              + " "
              + ((Reward) reward).chance
              + " "
              + ((Reward) reward).tier);
    }
    ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
  }

  @SubCommand
  public void admin(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 3
        && sender.getCommandSenderEntity() instanceof EntityPlayer
        && ((EntityPlayer) sender.getCommandSenderEntity()).getHeldItemMainhand()
            != ItemStack.EMPTY) {
      if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {
        String item =
            StackConverter.toString(
                ((EntityPlayer) sender.getCommandSenderEntity()).getHeldItemMainhand());
        try {
          int chance = Integer.parseInt(args[1]);
          int tier = Integer.parseInt(args[2]);
          if (chance > 0 && tier + 1 > 0) {
            Reward reward = new Reward(tier, item, chance);
            DataHelper.save(Storage.REWARD, reward);
            DataHelper.load(Storage.REWARD, reward);
            ChatHelper.sendMessage(sender, senderLang.local.ECO_REWARD_CREATED);
          } else {
            ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
          }
        } catch (NumberFormatException e) {
          ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
        }
      } else if (args[0].equalsIgnoreCase("remove")
          || args[0].equalsIgnoreCase("rem")
          || args[0].equalsIgnoreCase("delete")
          || args[0].equalsIgnoreCase("del")) {
        Reward reward =
            (Reward)
                DataHelper.get(
                    Storage.REWARD,
                    StackConverter.toString(
                        ((EntityPlayer) sender.getCommandSenderEntity()).getHeldItemMainhand()));
        if (reward != null) {
          DataHelper.remove(Storage.REWARD, reward);
          ChatHelper.sendMessage(sender, senderLang.local.ECO_REWARD_REMOVED);
        } else {
          ChatHelper.sendMessage(sender, senderLang.local.ECO_REWARD_INVALID);
        }
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }
}
