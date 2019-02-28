package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

public class Reward implements DataType {

  public int chance;
  public String itemNBT;

  public Reward() {}

  public Reward(int chance, String nbt) {
    this.chance = chance;
    this.itemNBT = itemNBT;
  }

  public Reward(int chance, ItemStack reward) {
    this.chance = chance;
    NBTTagCompound nbt = new NBTTagCompound();
    reward.writeToNBT(nbt);
    this.itemNBT = nbt.toString();
  }

  public double getChance() {
    return chance;
  }

  public String getItemNBT() {
    return itemNBT;
  }

  public ItemStack getItem() {
    if (itemNBT != null && itemNBT.length() > 0) {
      try {
        return new ItemStack(JsonToNBT.getTagFromJson(itemNBT));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public String getID() {
    return getItem().getUnlocalizedName();
  }
}
