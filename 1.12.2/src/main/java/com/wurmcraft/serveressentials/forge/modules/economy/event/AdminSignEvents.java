package com.wurmcraft.serveressentials.forge.modules.economy.event;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.modules.economy.utils.SignUtils.handleInvalidSign;
import static com.wurmcraft.serveressentials.forge.modules.economy.utils.SignUtils.isValidSign;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.StackConverter;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.utils.SignUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.utils.SignUtils.SignType;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AdminSignEvents {

  public static final String SIGN_DATA = "SE_DATA";

  @SubscribeEvent
  public void onPlayerInteract(PlayerInteractEvent.RightClickBlock rc) {
    if (isValidSign(rc.getWorld(), rc.getPos())) {
      handleSignAction(rc.getEntityPlayer(),
          (TileEntitySign) rc.getWorld().getTileEntity(rc.getPos()));
    }
  }

  @SubscribeEvent
  public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock lc) {
    if (isValidSign(lc.getWorld(), lc.getPos())) {
      SignType type = SignUtils
          .getType((TileEntitySign) lc.getWorld().getTileEntity(lc.getPos()));
      if (type != null && RankUtils.hasPermission(RankUtils.getRank(lc.getEntityPlayer()),
          "eco.adminsign.create")) {
        setupSignActions(lc.getEntityPlayer(),
            (TileEntitySign) lc.getWorld().getTileEntity(lc.getPos()));
      }
    }
  }

  // Handles the usage of the signs after they have already been setup
  public void handleSignAction(EntityPlayer player, TileEntitySign sign) {
    SignType type = SignUtils.getType(sign);
    if (type != null) {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      if (type == SignType.BUY) {
        if (RankUtils.hasPermission(RankUtils.getRank(player), "eco.adminsign.buy")) {
          ItemStack stack = StackConverter
              .getData(sign.getTileData().getString(SIGN_DATA));
          if (stack != ItemStack.EMPTY) {
            double cost = SignUtils.getCost(player, sign);
            if (EcoUtils.hasTheMoney(playerData.global.wallet, cost)) {
              EcoUtils.consumeCurrency(player, cost);
              player.inventory.addItemStackToInventory(stack);
              player.sendMessage(new TextComponentString(
                  PlayerUtils.getUserLanguage(player).ECO_SIGN_ADMIN_BUY
                      .replaceAll("%COST%", COMMAND_INFO_COLOR + cost + COMMAND_COLOR)
                      .replaceAll("%NAME%",
                          COMMAND_INFO_COLOR + stack.getCount() + "x " + stack
                              .getDisplayName() + COMMAND_COLOR)));
            } else {
              player.sendMessage(new TextComponentString(COMMAND_INFO_COLOR + PlayerUtils
                  .getUserLanguage(player).ERROR_INSUFFICENT_FUNDS.replaceAll("%AMOUNT%",
                      COMMAND_INFO_COLOR + EcoUtils.getCurrency(playerData.global.wallet)
                          + COMMAND_COLOR)));
            }
          } else {
            handleInvalidSign(player, sign);
          }
        } else {
          player.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ERROR_NO_PERMS));
        }
      } else if (type == SignType.SELL) {
        if (RankUtils.hasPermission(RankUtils.getRank(player), "eco.adminsign.sell")) {
          ItemStack stack = StackConverter
              .getData(sign.getTileData().getString(SIGN_DATA));
          if (stack != null) {
            double cost = SignUtils.getCost(player, sign);
            if (SignUtils.hasStack(player, stack) && SignUtils
                .consumeItem(player, stack)) {
              EcoUtils.addCurrency(player, cost);
              player.sendMessage(new TextComponentString(
                  COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ECO_SIGN_ADMIN_SELL
                      .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + cost + COMMAND_COLOR)
                      .replaceAll("%NAME%",
                          COMMAND_INFO_COLOR + stack.getCount() + "x " + stack
                              .getDisplayName() + COMMAND_COLOR)));
            } else {
              player.sendMessage(new TextComponentString(
                  PlayerUtils.getUserLanguage(player).ECO_SIGN_ADMIN_SELL_ITEM
                      .replaceAll("%NAME%",
                          COMMAND_INFO_COLOR + stack.getCount() + "x " + stack
                              .getDisplayName() + COMMAND_COLOR)));
            }
          } else {
            handleInvalidSign(player, sign);
          }
        } else {
          player.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ERROR_NO_PERMS));
        }
      } else if (type == SignType.COMMAND) {
        if (RankUtils.hasPermission(RankUtils.getRank(player), "eco.adminsign.sell")) {
          String command = sign.getTileData().getString(SIGN_DATA);
          double cost = SignUtils.getCost(player, sign);
          if (EcoUtils.hasTheMoney(playerData.global.wallet, cost)) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()
                .executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),
                    command);
            player.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ECO_SIGN_ADMIN_COMMAND
                    .replaceAll("%COST%", COMMAND_INFO_COLOR + cost + COMMAND_COLOR)));
          } else {
            player.sendMessage(new TextComponentString(COMMAND_INFO_COLOR + PlayerUtils
                .getUserLanguage(player).ERROR_INSUFFICENT_FUNDS.replaceAll("%AMOUNT%",
                    COMMAND_INFO_COLOR + EcoUtils.getCurrency(playerData.global.wallet)
                        + COMMAND_COLOR)));
          }
        } else {
          player.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).ERROR_NO_PERMS));
        }
      }
    }
  }

  public void setupSignActions(EntityPlayer player, TileEntitySign sign) {
    SignType type = SignUtils.getType(sign);
    if (!sign.getTileData().hasKey(SIGN_DATA)) {
      if (type == SignType.BUY) {
        try {
          Double.parseDouble(sign.signText[3].getUnformattedText());
          sign.signText[0].getStyle().setColor(TextFormatting.RED);
          sign.signText[1].getStyle().setColor(TextFormatting.LIGHT_PURPLE);
          sign.signText[2].getStyle().setColor(TextFormatting.LIGHT_PURPLE);
          sign.signText[3].getStyle().setColor(TextFormatting.GOLD);
          sign.getTileData().setString(SIGN_DATA,
              StackConverter.toString(player.getHeldItemMainhand()));
          sign.markDirty();
        } catch (NumberFormatException e) {
          handleInvalidSign(player, sign);
        }
      } else if (type == SignType.SELL) {
        try {
          Double.parseDouble(sign.signText[3].getUnformattedText());
          sign.signText[0].getStyle().setColor(TextFormatting.AQUA);
          sign.signText[1].getStyle().setColor(TextFormatting.LIGHT_PURPLE);
          sign.signText[2].getStyle().setColor(TextFormatting.LIGHT_PURPLE);
          sign.signText[3].getStyle().setColor(TextFormatting.GOLD);
          sign.getTileData().setString(SIGN_DATA,
              StackConverter.toString(player.getHeldItemMainhand()));
          sign.markDirty();
        } catch (NumberFormatException e) {
          handleInvalidSign(player, sign);
        }
      }
    }
  }

}
