package com.wurmcraft.serveressentials.common.economy.commands;

import static com.wurmcraft.serveressentials.common.ConfigHandler.saveLocation;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.global.Reward;
import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Economy")
public class RewardCommand extends SECommand {

  @Override
  public String getName() {
    return "reward";
  }

  @Override
  public List<String> getAltNames() {
    List<String> altNames = new ArrayList<>();
    altNames.add("rewards");
    return altNames;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "&b/reward";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (DataHelper.getData(Keys.REWARD) == null || DataHelper.getData(Keys.REWARD).isEmpty()) {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).INVALID_REWARDS);
    } else {
      if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        ItemStack stack = ItemStack.EMPTY;
        while (stack.equals(ItemStack.EMPTY)) {
          double totalWeight = 0;
          for (DataType item : DataHelper.getData(Keys.REWARD)) {
            totalWeight += (((Reward) item).chance);
          }
          double chance = player.world.rand.nextInt((int) totalWeight);
          stack = getRandomItem(chance);
        }
        if (!player.inventory.addItemStackToInventory(stack)) {
          player.world.spawnEntity(
              new EntityItem(player.world, player.posX, player.posY, player.posZ, stack));
          ChatHelper.sendMessage(sender, "&c" + stack.getDisplayName());
        }
      } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
        UUID player = UsernameResolver.getUUIDFromName(args[1]);
        if (player != null) {
          DataHelper.globalSettings.addPlayerReward(player, 1);
          DataHelper.forceSave(
              new File(saveLocation + File.separator + "Global.json"), DataHelper.globalSettings);
          ChatHelper.sendMessage(sender, getCurrentLanguage(sender).REWARD_ADDED);
        } else {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[1]));
        }
      }
    }
    if (args.length == 2
        && args[0].equalsIgnoreCase("add")
        && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      try {
        int chance = Integer.parseInt(args[1]);
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        Reward reward = new Reward(chance, player.getHeldItemMainhand());
        DataHelper.forceSave(Keys.REWARD, reward);
        DataHelper.load(Keys.REWARD, reward);
        ChatHelper.sendMessage(sender, "Added!");
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[1]));
      }
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  private static ItemStack getRandomItem(double chance) {
    for (Reward item : DataHelper.getData(Keys.REWARD, new Reward())) {
      if (item.getChance() >= chance) {
        return item.getItem();
      }
    }
    return null;
  }
}
