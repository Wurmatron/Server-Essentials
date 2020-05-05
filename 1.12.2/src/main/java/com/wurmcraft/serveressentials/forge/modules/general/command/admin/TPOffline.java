package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "TPOffline", aliases = {"TPOff"})
public class TPOffline {

  @Command(inputArguments = {CommandArguments.STRING})
  public void teleportOffline(ICommandSender sender, String player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tpoffline") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        UUID uuid = PlayerUtils.getPlayer(player);
        if (uuid != null) {
          StoredPlayer playerData = (StoredPlayer) SECore.dataHandler
              .getData(DataKey.PLAYER, uuid.toString());
          TeleportUtils.teleportTo((EntityPlayer) sender.getCommandSenderEntity(),
              playerData.server.lastLocation);
          sender.sendMessage(
              TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
                      PlayerUtils.getUserLanguage(sender).GENERAL_TP_CORDS.replaceAll("%POS%",
                          COMMAND_INFO_COLOR + +playerData.server.lastLocation.x + ", "
                              + playerData.server.lastLocation.y + ", "
                              + playerData.server.lastLocation.z + ", "
                              + playerData.server.lastLocation.dim)),
                  playerData.server.lastLocation));
        } else {
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_TPOFFLINE
                  .replaceAll("%PLAYER%", COMMAND_INFO_COLOR + player)));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
