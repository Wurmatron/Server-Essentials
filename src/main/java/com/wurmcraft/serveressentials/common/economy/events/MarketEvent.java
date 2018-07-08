package com.wurmcraft.serveressentials.common.economy.events;

import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.StackConverter;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.codec.language.bm.Lang;

public class MarketEvent {

  @SubscribeEvent
  public void onInteractEvent(PlayerInteractEvent.RightClickBlock e) {
    IBlockState state = e.getEntityPlayer().world.getBlockState(e.getPos());
    if (state
        .getBlock()
        .getUnlocalizedName()
        .equalsIgnoreCase(Blocks.STANDING_SIGN.getUnlocalizedName())) {
      TileEntitySign sign = (TileEntitySign) e.getEntityPlayer().world.getTileEntity(e.getPos());
      if (sign != null) {
        if (!sign.getTileData().hasKey("shopData")) {
          if (sign.signText[0].getUnformattedComponentText().equalsIgnoreCase("[IBuy]")) {
            e.getEntityPlayer()
                .sendMessage(
                    new TextComponentString(createIBuySign(e.getEntityPlayer(), state, sign)));
          } else if (sign.signText[0].getUnformattedComponentText().equalsIgnoreCase("[Buy]")) {
            e.getEntityPlayer()
                .sendMessage(
                    new TextComponentString(createBuySign(e.getEntityPlayer(), state, sign)));
          } else if (sign.signText[0].getUnformattedComponentText().equalsIgnoreCase("[ISell]")) {
            e.getEntityPlayer()
                .sendMessage(
                    new TextComponentString(createISellSign(e.getEntityPlayer(), state, sign)));
          } else if (sign.signText[0].getUnformattedComponentText().equalsIgnoreCase("[Sell]")) {
            e.getEntityPlayer()
                .sendMessage(
                    new TextComponentString(createSellSign(e.getEntityPlayer(), state, sign)));
          }
        } else {
          String txt =
              TextFormatting.getTextWithoutFormattingCodes(sign.signText[0].getUnformattedText());
          if (txt.equalsIgnoreCase("[IBuy]")) {
            if (Ibuy(e.getEntityPlayer(), sign)) {
              e.getEntityPlayer().sendMessage(new TextComponentString(LanguageModule
                  .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).PURCHASED
                  .replaceAll("%COST%", TextFormatting.getTextWithoutFormattingCodes(
                      sign.signText[3].getUnformattedComponentText()))
                  .replaceAll("%COIN%", ConfigHandler.serverCurrency)));
            } else {
              e.getEntityPlayer().sendMessage(new TextComponentString(LanguageModule
                  .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).NO_MONEY));
            }
          } else if (txt.equalsIgnoreCase("[ISell]")) {
            if (ISell(e.getEntityPlayer(), sign)) {
              e.getEntityPlayer().sendMessage(new TextComponentString(LanguageModule
                  .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).SOLD
                  .replaceAll("%COST%", TextFormatting.getTextWithoutFormattingCodes(
                      sign.signText[3].getUnformattedComponentText()))
                  .replaceAll("%COIN%", ConfigHandler.serverCurrency)));
            } else {
              e.getEntityPlayer().sendMessage(new TextComponentString(LanguageModule
                  .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).NO_ITEM));
            }
          }
        }
      }
    }
  }

  private static String createIBuySign(
      EntityPlayer player, IBlockState state, TileEntitySign sign) {
    if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
      try {
        sign.signText[0] = new TextComponentString(TextFormatting.GOLD + "[IBuy]");
        double cost = Double.parseDouble(sign.signText[3].getUnformattedComponentText());
        sign.signText[3] = new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + cost);
        sign.getTileData()
            .setString("shopData", StackConverter.toString(player.getHeldItemMainhand()));
        sign.markDirty();
        player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
        return LanguageModule.getLangfromUUID(player.getGameProfile().getId())
            .SIGN_CREATED
            .replaceAll("%TYPE%", "IBuy");
      } catch (Exception e) {
        return LanguageModule.getLangfromUUID(player.getGameProfile().getId())
            .INVALID_NUMBER
            .replaceAll("%NUMBER%", sign.signText[3].getUnformattedComponentText());
      }
    } else {
      return LanguageModule.getLangfromUUID(player.getGameProfile().getId()).INVALID_HAND;
    }
  }

  // TODO Buy Sign
  private static String createBuySign(EntityPlayer player, IBlockState state, TileEntitySign sign) {

    sign.markDirty();
    player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
    return "Error, MarketEvent#createBuySign";
  }

  private static String createISellSign(
      EntityPlayer player, IBlockState state, TileEntitySign sign) {
    if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
      try {
        sign.signText[0] = new TextComponentString(TextFormatting.GOLD + "[ISell]");
        double cost = Double.parseDouble(sign.signText[3].getUnformattedComponentText());
        sign.signText[3] = new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + cost);
        sign.getTileData()
            .setString("shopData", StackConverter.toString(player.getHeldItemMainhand()));
        sign.markDirty();
        player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
        return LanguageModule.getLangfromUUID(player.getGameProfile().getId())
            .SIGN_CREATED
            .replaceAll("%TYPE%", "ISell");
      } catch (Exception e) {
        return LanguageModule.getLangfromUUID(player.getGameProfile().getId())
            .INVALID_NUMBER
            .replaceAll("%NUMBER%", sign.signText[3].getUnformattedComponentText());
      }
    } else {
      return LanguageModule.getLangfromUUID(player.getGameProfile().getId()).INVALID_HAND;
    }
  }

  // TODO Sell Sign
  private static String createSellSign(
      EntityPlayer player, IBlockState state, TileEntitySign sign) {

    sign.markDirty();
    player.world.notifyBlockUpdate(sign.getPos(), state, state, 3);
    return "Error, MarketEvent#createSellSign";
  }

  private static boolean Ibuy(EntityPlayer player, TileEntitySign sign) {
    GlobalUser global = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
    double playerBal = global.getBank().getCurrency(ConfigHandler.serverCurrency);
    double signCost =
        Double.parseDouble(
            TextFormatting.getTextWithoutFormattingCodes(
                sign.signText[3].getUnformattedComponentText()));
    if (playerBal >= signCost) {
      global.getBank().spend(ConfigHandler.serverCurrency, signCost);
      // TODO Cache and send so often due to currently sending updates every sign interaction
      player.inventory.addItemStackToInventory(
          StackConverter.getData(sign.getTileData().getString("shopData")));
      RequestHelper.UserResponses.overridePlayerData(global);
      return true;
    }
    return false;
  }

  private static boolean ISell(EntityPlayer player, TileEntitySign sign) {
    GlobalUser global = (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
    double sellAmount = Double.parseDouble(TextFormatting
        .getTextWithoutFormattingCodes(sign.signText[3].getUnformattedComponentText()));
    ItemStack sellItem = StackConverter.getData(sign.getTileData().getString("shopData"));
    int amountToSell = 0;
    if (player.inventory.hasItemStack(sellItem)) {
      for (int index = 0; index < player.inventory.mainInventory.size(); index++) {
        if (player.inventory.mainInventory.get(index).isItemEqual(sellItem)) {
          amountToSell +=
              player.inventory.mainInventory.get(index).getCount() / sellItem.getCount();
          player.inventory.mainInventory.set(index, ItemStack.EMPTY);
          if (!player.isSneaking()) {
            break;
          }
        }
      }
      global.getBank().earn(ConfigHandler.serverCurrency, amountToSell * sellAmount);
      RequestHelper.UserResponses.overridePlayerData(global);
      return true;
    }
    return false;
  }
}
