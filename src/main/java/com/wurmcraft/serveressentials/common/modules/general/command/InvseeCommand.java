package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.general.utils.wrapper.PlayerInventory;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "General")
public class InvseeCommand extends Command {

  @Override
  public String getName() {
    return "InvSee";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/invsee <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_INVSEE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      boolean open = false;
      EntityPlayer victim = CommandUtils.getPlayerForName(args[0]);
      EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
      if (victim != null) {
        if (victim != null) {
          if (player.openContainer != player.inventoryContainer) {
            player.closeScreen();
          }
          player.displayGUIChest(new PlayerInventory((EntityPlayerMP) victim, player));
          open = true;
        }
        if (!open) {
          ChatHelper.sendMessage(
              sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
        }
      }

    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("See");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }
}
