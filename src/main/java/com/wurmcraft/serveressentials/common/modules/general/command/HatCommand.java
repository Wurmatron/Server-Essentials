package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class HatCommand extends Command {

  @Override
  public String getName() {
    return "Hat";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/hat";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_HAT;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (player.inventory.armorInventory.get(3) != ItemStack.EMPTY) {
      ItemStack currentStack = player.inventory.armorInventory.get(3);
      player.inventory.armorInventory.set(3, player.inventory.getCurrentItem());
      player.inventory.deleteStack(player.inventory.getCurrentItem());
      player.inventory.addItemStackToInventory(currentStack);
    } else {
      player.inventory.armorInventory.set(3, player.inventory.getCurrentItem());
      player.inventory.deleteStack(player.inventory.getCurrentItem());
    }
    ChatHelper.sendMessage(sender, senderLang.local.GENERAL_HAT);
  }
}
