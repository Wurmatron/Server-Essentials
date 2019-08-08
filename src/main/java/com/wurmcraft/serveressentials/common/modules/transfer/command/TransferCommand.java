package com.wurmcraft.serveressentials.common.modules.transfer.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.user.transfer.ItemBin;
import com.wurmcraft.serveressentials.api.user.transfer.TransferBin;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import java.util.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Transfer")
public class TransferCommand extends Command {

  @Override
  public String getName() {
    return "Transfer";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/transfer <get | send> <all | hand | hotbar>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TRANSFER;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void get(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      TransferBin bin = RequestGenerator.Transfer.getTransfer(player.getGameProfile().getId());
      if (bin != null) {
        for (ItemBin transfer : bin.transfers) {
          if (transfer.transferID.equals(ConfigHandler.transferID)) {
            List<String> notRemoved = new ArrayList<>();
            for (int index = 0; index < transfer.items.length; index++) {
              if (!player.inventory.addItemStackToInventory(
                  StackConverter.getData(transfer.items[index]))) {
                notRemoved.add(transfer.items[index]);
              }
            }
            transfer.items = notRemoved.toArray(new String[0]);
          }
        }
        RequestGenerator.Transfer.overrideTransfer(bin);
        ChatHelper.sendMessage(sender, senderLang.local.TRANSFER_SENT);
      } else {
        ChatHelper.sendMessage(sender, senderLang.local.TRANSFER_EMPTY);
      }
    }
  }

  private static ItemBin getServerBin(TransferBin bin) {
    for (ItemBin transfer : bin.transfers) {
      if (transfer.transferID.equals(ConfigHandler.transferID)) {
        return transfer;
      }
    }
    return null;
  }

  @SubCommand
  public void send(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      TransferBin bin = RequestGenerator.Transfer.getTransfer(player.getGameProfile().getId());
      ItemBin transfer = null;
      if (bin == null) { // New Transfer User
        ItemBin newBin = new ItemBin(ConfigHandler.transferID, new String[0]);
        bin = new TransferBin(player.getGameProfile().getId().toString(), new ItemBin[]{newBin});
        transfer = newBin;
      } else {
        transfer = getServerBin(bin);
      }
      if (transfer != null) {
        String transferType = "All";
        if (args.length == 1) {
          transferType = args[0];
        }
        if (transferType.equalsIgnoreCase("All")) {
          List<String> items = new ArrayList<>();
          Collections.addAll(items, transfer.items);
          for (ItemStack stack : player.inventory.mainInventory) {
            if (!stack.isEmpty() || stack != ItemStack.EMPTY) {
              String item = StackConverter.toString(stack);
              if (!item.isEmpty() && !item.contains("minecraft:air")) {
                items.add(item);
                player.inventory.deleteStack(stack);
              }
            }
          }
          transfer.items = items.toArray(new String[0]);
        } else if (transferType.equalsIgnoreCase("Hand")) {
          List<String> items = new ArrayList<>();
          Collections.addAll(items, transfer.items);
          items.add(StackConverter.toString(player.inventory.getCurrentItem()));
          transfer.items = items.toArray(new String[0]);
        } else if (transferType.equalsIgnoreCase("Hotbar")) {
          List<String> items = new ArrayList<>();
          Collections.addAll(items, transfer.items);
          for (int index = 0; index < 9; index++) {
            items.add(StackConverter.toString(player.inventory.getStackInSlot(index)));
          }
          transfer.items = items.toArray(new String[0]);
        }
        RequestGenerator.Transfer.overrideTransfer(bin);
        ChatHelper.sendMessage(sender, senderLang.local.TRANSFER_TRANSFERRED);
      } else {
        ChatHelper.sendMessage(sender, senderLang.local.TRANSFER_EMPTY);
      }
    }
  }
}
