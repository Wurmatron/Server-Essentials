package com.wurmcraft.serveressentials.common.modules.economy.utils;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.NBT;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextFormatting;

public class SignShopHandler {

  private static boolean handleABuySign(TileEntitySign sign, EntityPlayer player) {
    if (sign.signText[0].getUnformattedComponentText().contains("[IBuy]")
        || sign.signText[0].getUnformattedComponentText().contains("[ABuy]")) {
      ItemStack item = StackConverter.getData(sign.getTileData().getString(NBT.SHOP_DATA));
      if (item != null) {
        try {
          double cost =
              Double.parseDouble(
                  TextFormatting.getTextWithoutFormattingCodes(
                      sign.signText[3].getFormattedText()));
          if (cost > 0) {
            if (UserManager.canBuy(player, ConfigHandler.serverCurrency, cost)) {
              if (player.inventory.addItemStackToInventory(item)) {
                UserManager.spendCurrency(player, ConfigHandler.serverCurrency, cost);
                return true;
              } else {
                ChatHelper.sendMessage(
                    player, LanguageModule.getUserLanguage(player).local.CHAT_INVENTORY_FULL);
              }
            } else {
              ChatHelper.sendMessage(
                  player,
                  LanguageModule.getUserLanguage(player)
                      .local
                      .ECO_NEED
                      .replaceAll(
                          Replacment.COIN,
                          ""
                              + (cost
                                  - UserManager.getServerCurrency(
                                      player.getGameProfile().getId()))));
            }
          }
        } catch (NumberFormatException e) {
          // Invalid Sign so return false;
        }
      }
    }
    return false;
  }

  private static boolean handleASellSign(TileEntitySign sign, EntityPlayer player) {
    if (sign.signText[0].getUnformattedComponentText().contains("[IBuy]")
        || sign.signText[0].getUnformattedComponentText().contains("[ABuy]")) {
      ItemStack item = StackConverter.getData(sign.getTileData().getString(NBT.SHOP_DATA));
      if (item != null) {
        try {
          double cost =
              Double.parseDouble(
                  TextFormatting.getTextWithoutFormattingCodes(
                      sign.signText[3].getFormattedText()));
          if (cost > 0) {
            if (consumeItem(player, item)) {
              UserManager.earnCurrency(player, ConfigHandler.serverCurrency, cost);
              return true;
            } else {
              ChatHelper.sendMessage(
                  player, LanguageModule.getUserLanguage(player).local.CHAT_ITEM_NOT_FOUND);
            }
          }
        } catch (NumberFormatException e) {
          // Invalid Sign so return false;
        }
      }
    }
    return false;
  }

  private static boolean consumeItem(EntityPlayer player, ItemStack stack) {
    if (player.inventory.hasItemStack(stack)) {
      player.inventory.deleteStack(stack);
      return true;
    }
    return false;
  }

  public static boolean handleAdminSigns(TileEntitySign sign, EntityPlayer player) {
    if (sign.signText[0].getUnformattedText().contains("Buy")) {
      return handleABuySign(sign, player);
    } else if (sign.signText[0].getUnformattedText().contains("Sell")) {
      return handleASellSign(sign, player);
    }
    return false;
  }

  private static boolean handleBuySign(TileEntitySign sign, EntityPlayer player) {
    return false;
  }

  private static boolean handleSellSign(TileEntitySign sign, EntityPlayer player) {
    return false;
  }

  public static boolean handleSigns(TileEntitySign sign, EntityPlayer player) {
    return false;
  }
}
