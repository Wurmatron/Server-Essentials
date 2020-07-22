package com.wurmcraft.serveressentials.forge.modules.rank.command;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Rank", name = "Rank")
public class RankCommand {

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.STRING}, inputNames = {"{Player}, {Rank}"})
  public void changeRank(ICommandSender sender, EntityPlayer player, String rank) {
    try {
      Rank nextRank = (Rank) SERegistry.getStoredData(DataKey.RANK, rank);
      StoredPlayer playerData = (StoredPlayer) SERegistry.getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      if(SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {

      } else {
        playerData.global.rank = rank;
        SERegistry.register(DataKey.PLAYER, playerData);
        sender.sendMessage(new TextComponentString("Rank set to " + rank));
      }
    } catch (NoSuchElementException e) {
      sender.sendMessage(new TextComponentString(PlayerUtils.getUserLanguage(sender).ERROR_RANK_NOT_FOUND.replaceAll("%RANK%", rank)));
    }
  }

}
