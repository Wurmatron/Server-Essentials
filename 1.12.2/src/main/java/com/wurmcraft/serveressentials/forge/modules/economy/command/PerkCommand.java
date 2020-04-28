package com.wurmcraft.serveressentials.forge.modules.economy.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils.setPlayerPerk;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Economy", name = "Perk")
public class PerkCommand {

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.PERK}, inputNames = {"Buy", "Name"})
  public void buyPerk(ICommandSender sender, String commandType, Perk perk) {
    if (sender != null && sender.getCommandSenderEntity() != null && sender
        .getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      int perkLvl = EcoUtils.getPerkLevel(sender, perk);
      StoredPlayer playerData = PlayerUtils.getPlayer(player);
      if (playerData != null) {
        if (EcoUtils.hasTheMoney(playerData.global.wallet,
            EcoUtils.calculateCostInGlobalPerPerk(perk, perkLvl + 1))) {
          double cost = EcoUtils.calculateCostInGlobalPerPerk(perk, perkLvl + 1);
          if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
            SECore.executors.schedule(() -> {
              GlobalPlayer globalData = RestRequestGenerator.User
                  .getPlayer(player.getGameProfile().getId().toString());
              playerData.global.wallet = EcoUtils.setCurrency(globalData.wallet,
                  EcoUtils.getCurrency(globalData.wallet) - cost);
              setPlayerPerk(globalData, perk, perkLvl + 1);
              playerData.global = globalData;
            }, 0, TimeUnit.SECONDS);
          } else {
            EcoUtils.setCurrency(playerData.global.wallet,
                EcoUtils.getCurrency(playerData.global.wallet) - cost);
            setPlayerPerk(playerData.global, perk, perkLvl + 1);
          }
          player.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ECO_PERK_BUY
                  .replaceAll("%LEVEL%", COMMAND_INFO_COLOR + "" + (perkLvl + 1))
                  .replaceAll("%PERK%", COMMAND_INFO_COLOR + perk.name())));
        } else {
          sender.sendMessage(new TextComponentString(
              ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_INSUFFICENT_FUNDS
                  .replaceAll("%AMOUNT%",
                      "" + EcoUtils.getCurrency(playerData.global.wallet))));
        }
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = "List, PerkName")
  public void perkList(ICommandSender sender, String name) {
    if (name.equalsIgnoreCase("list")) {
      for (Perk p : Perk.values()) {
        double cost = EcoUtils
            .calculateCostInGlobalPerPerk(p, EcoUtils.getPerkLevel(sender, p));
        sender.sendMessage(
            TextComponentUtils.addTextHover(new TextComponentString(COMMAND_COLOR +
                    PlayerUtils.getUserLanguage(sender).ECO_PERK_COST
                        .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + "" + cost)
                        .replaceAll("%PERK%", COMMAND_INFO_COLOR + p.name())),
                new TextComponentString(COMMAND_INFO_COLOR + p.name() + ": " + cost)));
      }
    } else {
      for (Perk p : Perk.values()) {
        if (p.name().equalsIgnoreCase(name)) {
          double cost = EcoUtils
              .calculateCostInGlobalPerPerk(p, EcoUtils.getPerkLevel(sender, p));
          sender.sendMessage(TextComponentUtils.addTextHover(new TextComponentString(
                  COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).ECO_PERK_COST
                      .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + cost)
                      .replaceAll("%PERK%", COMMAND_INFO_COLOR + p.name())),
              new TextComponentString(COMMAND_INFO_COLOR + p.name() + ": " + cost)));
        }
      }
    }
  }

  public enum Perk {
    Home, Vault, ClaimBlocks, ChunkLoading;
  }

}
