package com.wurmcraft.serveressentials.forge.modules.economy.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SignUtils {

  public static SignType getType(TileEntitySign sign) {
    for (SignType type : SignType.values()) {
      if (sign.signText[0].getUnformattedText()
          .substring(1, sign.signText[0].getUnformattedText().length() - 1)
          .equalsIgnoreCase(type.name())) {
        return type;
      }
    }
    return null;
  }

  public static boolean isValidSign(World world, BlockPos pos) {
    if (world != null && pos != null) {
      return world.getTileEntity(pos) instanceof TileEntitySign;
    }
    return false;
  }

  public static double getCost(EntityPlayer player, TileEntitySign sign) {
    try {
      return Double.parseDouble(sign.signText[3].getUnformattedText());
    } catch (NumberFormatException e) {
      handleInvalidSign(player, sign);
    }
    return -1;
  }

  public static boolean hasStack(EntityPlayer player, ItemStack stack) {
    if (player.inventory.hasItemStack(stack)) {
      return true;
    }
    for (ItemStack inv : player.inventory.mainInventory) {
      if (stack.getTagCompound() != null && inv.getTagCompound() != null) {
        if (inv.isItemEqual(stack) && inv.getTagCompound().equals(stack.getTagCompound())
            && inv.getCount() >= stack.getCount()) {
          return true;
        } else if (inv.isItemEqual(stack) && inv.getCount() >= stack.getCount()) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean consumeItem(EntityPlayer player, ItemStack stack) {
    for (ItemStack inv : player.inventory.mainInventory) {
      if (inv.isItemEqual(stack) && inv.getCount() >= stack.getCount()) {
        if (inv.getTagCompound() != null && stack.getTagCompound() != null && inv
            .getTagCompound().equals(stack.getTagCompound())) {
          inv.setCount(inv.getCount() - stack.getCount());
          if(inv.getCount() <= 0) {
            inv = ItemStack.EMPTY;
          }
          return true;
        } else if (inv.getTagCompound() == null && stack.getTagCompound() == null) {
          inv.setCount(inv.getCount() - stack.getCount());
          if(inv.getCount() <= 0) {
            inv = ItemStack.EMPTY;
          }
          return true;
        }
      }
    }
    return false;
  }

  public static void handleInvalidSign(EntityPlayer player, TileEntitySign sign) {
    // TODO Invalidate Sign / Loose Colors
  }

  public enum SignType {
    BUY, SELL, COMMAND
  }

}
