package com.wurmcraft.serveressentials.common.general.commands.perk;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
@Command(moduleName = "General")
public class HatCommand extends SECommand {

  @Override
  public String getName() {
    return "hat";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
      ItemStack hatStack = player.getHeldItemMainhand();
      ItemStack headStack = player.inventory.armorInventory.get(3);
      player.inventory.deleteStack(headStack);
      player.inventory.deleteStack(hatStack);
      player.inventory.armorInventory.set(3, hatStack);
      player.inventory.addItemStackToInventory(headStack);
    } else {
      player.sendMessage(
          new TextComponentString(
              LanguageModule.getLangfromUUID(player.getGameProfile().getId()).NO_ITEM));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
