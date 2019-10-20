package com.wurmcraft.serveressentials.common.modules.economy.utils;

import com.wurmcraft.serveressentials.common.modules.economy.event.SignShopEvents;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.NBT;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SignCreationHelper {

  private static boolean createAdminBuySign(
      TileEntitySign sign, IBlockState state, EntityPlayer player, ItemStack stack) {
    try {
      double cost = Double.parseDouble(sign.signText[3].getUnformattedText());
      if (cost > 0 && stack != ItemStack.EMPTY && stack.getCount() > 0) {
        sign.signText[0] = new TextComponentString(TextFormatting.GOLD + "[IBuy]");
        sign.signText[3] = new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + cost);
        sign.getTileData().setString(NBT.SHOP_DATA, StackConverter.toString(stack));
        sign.markDirty();
        player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
        return true;
      }
    } catch (NumberFormatException e) {
      // Invalid Number
      ChatHelper.sendMessage(
          player, LanguageModule.getUserLanguage(player).local.CHAT_INVALID_NUMBER);
    }
    return false;
  }

  private static boolean createAdminSellSign(
      TileEntitySign sign, IBlockState state, EntityPlayer player, ItemStack stack) {
    try {
      double cost = Double.parseDouble(sign.signText[3].getUnformattedText());
      if (cost > 0 && stack != ItemStack.EMPTY && stack.getCount() > 0) {
        sign.signText[0] = new TextComponentString(TextFormatting.GOLD + "[ISell]");
        sign.signText[3] = new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + cost);
        sign.getTileData().setString(NBT.SHOP_DATA, StackConverter.toString(stack));
        sign.markDirty();
        player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
        return true;
      }
    } catch (NumberFormatException e) {
      // Invalid Number
      ChatHelper.sendMessage(
          player, LanguageModule.getUserLanguage(player).local.CHAT_INVALID_NUMBER);
    }
    return false;
  }

  public static boolean createAdminSign(
      TileEntitySign sign, IBlockState state, EntityPlayer player, ItemStack stack) {
    if (UserManager.hasPerm(player, "economy.adminSignCreation")) {
      if (sign.signText[0].getUnformattedText().equalsIgnoreCase("[ABuy]")
          || sign.signText[0].getUnformattedText().equalsIgnoreCase("[IBuy]")) {
        return createAdminBuySign(sign, state, player, stack);
      } else if (sign.signText[0].getUnformattedText().equalsIgnoreCase("[ASell]")
          || sign.signText[0].getUnformattedText().equalsIgnoreCase("[ISell]")) {
        return createAdminSellSign(sign, state, player, stack);
      }
    }
    return false;
  }

  // TODO Buy Sign Usage
  private static boolean createBuySign(
      TileEntitySign sign, IBlockState state, EntityPlayer player, ItemStack stack) {
    try {
      double cost = Double.parseDouble(sign.signText[3].getUnformattedText());
      ItemStack signStack = getItemStack(player);
      if (signStack != null) {
        sign.signText[0] = new TextComponentString(TextFormatting.GOLD + "[Buy]");
        sign.signText[3] = new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + cost);
        sign.getTileData().setString(NBT.SHOP_DATA, SignShopEvents.tracker.get(player).toString());
        sign.markDirty();
        player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
        SignShopEvents.tracker.remove(player);
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.ECO_SIGN_CREATED);
      } else {
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.ECO_SIGN_LINK_NOT_STARTED);
      }

    } catch (NumberFormatException e) {
      ChatHelper.sendMessage(
          player, LanguageModule.getUserLanguage(player).local.CHAT_INVALID_NUMBER);
    }
    return false;
  }

  private static ItemStack getItemStack(EntityPlayer player) {
    return getStackFromInv(
        ((IInventory) player.world.getTileEntity(SignShopEvents.tracker.get(player))), player);
  }

  private static ItemStack getStackFromInv(IInventory inv, EntityPlayer player) {
    if (inv.isUsableByPlayer(player)) { // Prevent Buying items from an another's claim
      if (inv.getStackInSlot(0) != ItemStack.EMPTY) {
        return inv.getStackInSlot(0);
      } else {
        for (int index = 1; index < inv.getSizeInventory(); index++) {
          if (inv.getStackInSlot(index) != ItemStack.EMPTY) {
            return inv.getStackInSlot(index).splitStack(inv.getStackInSlot(index).getCount() - 1);
          }
        }
      }
    }
    return ItemStack.EMPTY;
  }

  // TODO Sell Sign Usage
  private static boolean createSellSign(
      TileEntitySign sign, IBlockState state, EntityPlayer player, ItemStack stack) {
    try {
      double cost = Double.parseDouble(sign.signText[3].getUnformattedText());
      ItemStack signStack = getItemStack(player);
      if (signStack != null) {
        sign.signText[0] = new TextComponentString(TextFormatting.GOLD + "[Sell]");
        sign.signText[3] = new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + cost);
        sign.getTileData().setString(NBT.SHOP_DATA, SignShopEvents.tracker.get(player).toString());
        sign.markDirty();
        player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
        SignShopEvents.tracker.remove(player);
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.ECO_SIGN_CREATED);
      } else {
        ChatHelper.sendMessage(
            player, LanguageModule.getUserLanguage(player).local.ECO_SIGN_LINK_NOT_STARTED);
      }

    } catch (NumberFormatException e) {
      ChatHelper.sendMessage(
          player, LanguageModule.getUserLanguage(player).local.CHAT_INVALID_NUMBER);
    }
    return false;
  }

  public static boolean createSign(
      TileEntitySign sign, IBlockState state, EntityPlayer player, ItemStack stack) {
    if (UserManager.hasPerm(player, "economy.signCreation")
        && stack.isItemEqual(SignShopEvents.SHOP_CREATION_TOOL)) {
      if (sign.signText[0].getUnformattedText().equalsIgnoreCase("[Buy]")) {
        return createBuySign(sign, state, player, stack);
      } else if (sign.signText[0].getUnformattedText().equalsIgnoreCase("[Sell]")) {
        return createSellSign(sign, state, player, stack);
      }
    }
    return false;
  }
}
