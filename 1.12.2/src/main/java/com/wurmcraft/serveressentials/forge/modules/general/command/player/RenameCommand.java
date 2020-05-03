package com.wurmcraft.serveressentials.forge.modules.general.command.player;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Rename", name = "Rename", aliases = {"Name"})
public class RenameCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Item-Name"})
  public void renameItem(ICommandSender sender, String name) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.rename") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (player.inventory.getCurrentItem() != ItemStack.EMPTY) {
          player.inventory.getCurrentItem()
              .setStackDisplayName(name.replaceAll("&", "\u00a7"));
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_RENAME
                  .replaceAll("%NAME%",
                      COMMAND_INFO_COLOR + name.replaceAll("&", "\u00a7"))));
        } else {
          sender.sendMessage(new TextComponentString(
              PlayerUtils.getUserLanguage(player).ERROR_NO_HELD_ITEM));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
