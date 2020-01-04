package com.wurmcraft.serveressentials.api.storage.json;

import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.common.utils.item.StackConverter;
import net.minecraft.item.ItemStack;

public class Reward implements FileType {

  public int tier;
  public String stack;
  public int chance;

  public Reward(int tier, String stack, int chance) {
    this.tier = tier;
    this.stack = stack;
    this.chance = chance;
  }

  public Reward() {
    this.tier = 0;
    this.stack = "";
    this.chance = 0;
  }

  @Override
  public String getID() {
    return stack.isEmpty() ? "Invalid" : stack.replaceAll("<", "").replaceAll(">", "").replaceAll(":", "_");
  }

  public ItemStack getStack() {
    return StackConverter.getData(stack);
  }
}
