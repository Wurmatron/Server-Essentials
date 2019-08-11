package com.wurmcraft.serveressentials.common.modules.transfer.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Transfer")
public class ConvertCommand extends Command {

  @Override
  public String getName() {
    return "Convert";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/convert <player> <item>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_CONVERT;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = (EntityPlayer) CommandUtils.getPlayerForName(args[0]);
    if (player != null) {
      String jsonItem = args[1];
      ItemStack stack = StackConverter.getData(jsonItem);
      player.inventory.addItemStackToInventory(stack);
    } else {
      ChatHelper.sendMessage(
          sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
