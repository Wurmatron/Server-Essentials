package com.wurmcraft.serveressentials.forge.modules.economy.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils.setPlayerPerk;

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
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Economy", name = "Perk")
public class PerkCommand {

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.PERK}, inputNames = {"Buy", "Name"})
  public void buyPerk(ICommandSender sender, String commandType, Perk perk) {
    if (commandType.equalsIgnoreCase("buy")) {
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(sender), "economy.perk.buy") || !SERegistry
          .isModuleLoaded("Rank")) {
        if (sender != null && sender.getCommandSenderEntity() != null && sender
            .getCommandSenderEntity() instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
          int perkLvl = EcoUtils.getPerkLevel(sender, perk);
          if (perk.maxLevel != -1 && perkLvl >= perk.maxLevel) {
            sender.sendMessage(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).ECO_PERK_MAX_LEVEL
                    .replaceAll("%PERK%", COMMAND_INFO_COLOR + perk.name())));
            return;
          }
          StoredPlayer playerData = PlayerUtils.getPlayer(player);
          if (playerData != null) {
            if (EcoUtils.hasTheMoney(playerData.global.wallet,
                EcoUtils.calculateCostInGlobalPerPerk(perk, perkLvl + 1))) {
              double cost = EcoUtils.calculateCostInGlobalPerPerk(perk, perkLvl + 1);
              if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
                GlobalPlayer globalData = RestRequestGenerator.User
                    .getPlayer(player.getGameProfile().getId().toString());
                playerData.global.wallet = EcoUtils.setCurrency(globalData.wallet,
                    EcoUtils.getCurrency(globalData.wallet) - cost);
                setPlayerPerk(globalData, perk, perkLvl + 1);
                playerData.global = globalData;
                RestRequestGenerator.User.overridePlayer(globalData.uuid, globalData);
              } else {
                EcoUtils.setCurrency(playerData.global.wallet,
                    EcoUtils.getCurrency(playerData.global.wallet) - cost);
                setPlayerPerk(playerData.global, perk, perkLvl + 1);
              }
              player.sendMessage(new TextComponentString(
                  COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ECO_PERK_BUY
                      .replaceAll("%LEVEL%",
                          COMMAND_INFO_COLOR + "" + (perkLvl + 1) + COMMAND_COLOR)
                      .replaceAll("%PERK%",
                          COMMAND_INFO_COLOR + perk.name() + COMMAND_COLOR)));
            } else {
              sender.sendMessage(new TextComponentString(
                  ERROR_COLOR + PlayerUtils
                      .getUserLanguage(sender).ERROR_INSUFFICENT_FUNDS
                      .replaceAll("%AMOUNT%",
                          "" + EcoUtils.getCurrency(playerData.global.wallet))));
            }
          }
        }
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = "List")
  public void perkList(ICommandSender sender, String name) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "economy.perk.list") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (name.equalsIgnoreCase("list")) {
        for (Perk p : Perk.values()) {
          double cost = EcoUtils
              .calculateCostInGlobalPerPerk(p, EcoUtils.getPerkLevel(sender, p) + 1);
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
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  public enum Perk {
    Home(-1),
    //    Vault,
    ClaimBlocks(-1),
    ENDERCHEST(1);

//    ChunkLoading;

    private int maxLevel;

    Perk(int maxLevel) {
      this.maxLevel = maxLevel;
    }
  }

}
