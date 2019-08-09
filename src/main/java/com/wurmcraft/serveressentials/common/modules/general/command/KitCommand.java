package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.Kit;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

@ModuleCommand
public class KitCommand extends Command {

  private static List<String> kits;

  @Override
  public String getName() {
    return "kit";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/kit <name | create | list> <name>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_KIT;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1
        && !args[0].equalsIgnoreCase("list")
        && !args[0].equalsIgnoreCase("create")
        && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      Kit kit = DataHelper.get(Storage.KIT, args[0], new Kit());
      if (kit != null && kit.items != null) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (UserManager.canUseKit(player, kit)) {
          if (UserManager.hasPerm(player, "general.kit." + kit.name)) {
            addKitToPlayer(player, kit);
            ChatHelper.sendMessage(
                sender, senderLang.local.GENERAL_KIT_GIVEN.replaceAll(Replacment.KIT, kit.name));
          } else {
            ChatHelper.sendMessage(sender, senderLang.local.CHAT_NO_PERMS);
          }
        } else {
          ChatHelper.sendMessage(
              sender,
              senderLang.local.GENERAL_KIT_TIMER.replaceAll(
                  Replacment.MINUTES, "" + ((int) (UserManager.lastKitUse(player, kit) / 60000))));
        }
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.GENERAL_KIT_INVALID.replaceAll(Replacment.KIT, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void list(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      for (Kit kit : DataHelper.getData(Storage.KIT, new Kit())) {
        ChatHelper.sendMessage(
            sender, TextFormatting.AQUA + kit.name + " | " + ((kit.timer) + "m"));
      }
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void create(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 2) {
      String name = args[0];
      try {
        int timer = Integer.parseInt(args[1]);
        if (!name.equalsIgnoreCase("list") && !name.equalsIgnoreCase("create")) {
          List<String> items = new ArrayList<>();
          for (ItemStack stack :
              ((EntityPlayer) sender.getCommandSenderEntity()).inventory.mainInventory) {
            if (stack != ItemStack.EMPTY && !stack.isEmpty()) {
              items.add(StackConverter.toString(stack));
            }
          }
          Kit kit = new Kit(items.toArray(new String[0]), timer, name);
          DataHelper.save(Storage.KIT, kit);
          DataHelper.load(Storage.KIT, kit);
          ChatHelper.sendMessage(
              sender, senderLang.local.GENERAL_KIT_CREATED.replaceAll(Replacment.KIT, kit.name));
        } else {
          ChatHelper.sendMessage(sender, senderLang.local.GENERAL_KIT_INVALID_NAME);
        }
      } catch (NumberFormatException e) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0) {
      if (kits == null) {
        List<String> loadedKits = new ArrayList<>();
        for (Kit kit : DataHelper.getData(Storage.KIT, new Kit())) {
          loadedKits.add(kit.name);
        }
        kits = loadedKits;
      } else {
        return kits;
      }
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }

  public static void addKitToPlayer(EntityPlayer player, Kit kit) {
    for (String item : kit.items) {
      if (!player.inventory.addItemStackToInventory(StackConverter.getData(item))) {
        player.world.spawnEntity(
            new EntityItem(
                player.world, player.posX, player.posY, player.posZ, StackConverter.getData(item)));
      }
    }
  }
}
