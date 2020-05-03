package com.wurmcraft.serveressentials.forge.modules.general.command.player;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Hat")
public class HatCommand {

  @Command(inputArguments = {})
  public void setHat(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.hat") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (player.inventory.getCurrentItem() != ItemStack.EMPTY) {
          ItemStack head = player.inventory.armorInventory.get(3);
          player.inventory.armorInventory.set(3, player.inventory.getCurrentItem());
          player.inventory.deleteStack(player.inventory.getCurrentItem());
          player.inventory.addItemStackToInventory(head);
        }
        player.sendMessage(
            new TextComponentString(PlayerUtils.getUserLanguage(player).GENERAL_HAT));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
