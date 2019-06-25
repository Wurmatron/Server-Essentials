package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "Teleportation")
public class TopCommand extends Command {

  @Override
  public String getName() {
    return "Top";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/top";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TOP;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    BlockPos topPos = player.getEntityWorld().getTopSolidOrLiquidBlock(player.getPosition());
    TeleportUtils.teleportTo(
        (EntityPlayerMP) player, new LocationWrapper(topPos, player.dimension), true, false);
    ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TOP);
  }
}
