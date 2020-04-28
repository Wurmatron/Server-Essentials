package com.wurmcraft.serveressentials.forge.modules.economy.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Economy", name = "Pay")
public class PayCommand {

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.DOUBLE}, inputNames = {"Player", "Amount"})
  public void sendMoney(ICommandSender sender, EntityPlayer player, double amount) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "economy.pay") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() != null && sender
          .getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer sendingPlayer = (EntityPlayer) sender.getCommandSenderEntity();
        StoredPlayer sendingPlayerData = PlayerUtils.getPlayer(sendingPlayer);
        if (sendingPlayerData != null) {
          if (EcoUtils.hasTheMoney(sendingPlayerData.global.wallet, amount)) {
            if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
//              SECore.executors.schedule(() -> {
              // Sending Player
              GlobalPlayer globalPlayer = RestRequestGenerator.User
                  .getPlayer(sendingPlayer.getGameProfile().getId().toString());
              sendingPlayerData.global.wallet = EcoUtils
                  .setCurrency(globalPlayer.wallet,
                      EcoUtils.getCurrency(globalPlayer.wallet) - amount);
              RestRequestGenerator.User
                  .overridePlayer(sendingPlayer.getGameProfile().getId().toString(),
                      globalPlayer);
              // Receiving Player
              StoredPlayer receivingPlayerData = PlayerUtils.getPlayer(sendingPlayer);
              GlobalPlayer receivingGlobalPlayer = RestRequestGenerator.User
                  .getPlayer(player.getGameProfile().getId().toString());
              if (receivingPlayerData != null) {
                receivingPlayerData.global.wallet = EcoUtils
                    .setCurrency(receivingGlobalPlayer.wallet,
                        EcoUtils.getCurrency(receivingGlobalPlayer.wallet) + amount);
              }
//              }, 0, TimeUnit.SECONDS);
//
            } else {
              StoredPlayer receivingPlayerData = PlayerUtils.getPlayer(sendingPlayer);
              if (receivingPlayerData != null) {
                EcoUtils.setCurrency(sendingPlayerData.global.wallet,
                    EcoUtils.getCurrency(sendingPlayerData.global.wallet) - amount);
                EcoUtils.setCurrency(receivingPlayerData.global.wallet,
                    EcoUtils.getCurrency(receivingPlayerData.global.wallet) + amount);
              }
            }
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).ECO_PAY_OTHER
                    .replaceAll("%AMOUNT%", "" + amount)
                    .replaceAll("%PLAYER%", player.getDisplayNameString())));
            player.sendMessage(new TextComponentString( COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).ECO_PAY_EARN
                    .replaceAll("%AMOUNT%", "" + amount)
                    .replaceAll("%PLAYER%", sendingPlayer.getDisplayNameString())));
          } else {
            sender.sendMessage(new TextComponentString(ERROR_COLOR +
                PlayerUtils.getUserLanguage(sender).ERROR_INSUFFICENT_FUNDS
                    .replaceAll("%AMOUNT%",
                        "" + EcoUtils.getCurrency(sendingPlayerData.global.wallet))));
          }
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.INTEGER}, inputNames = {"Player", "Amount"})
  public void sendMoney(ICommandSender sender, EntityPlayer player, int amount) {
    sendMoney(sender, player, (double) amount);
  }
}
