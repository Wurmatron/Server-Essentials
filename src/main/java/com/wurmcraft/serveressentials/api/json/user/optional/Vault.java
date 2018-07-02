package com.wurmcraft.serveressentials.api.json.user.optional;

import com.wurmcraft.serveressentials.api.json.user.IDataType;
import net.minecraft.item.ItemStack;

public class Vault implements IDataType {

  private String name;
  private String[] items = new String[54];

  public Vault(String name, ItemStack[] items) {
    this.name = name;
    setItems(items);
  }

  public ItemStack[] getItems() {
    ItemStack[] outputStacks = new ItemStack[54];
    for (int index = 0; index < items.length; index++) {
      // TODO Implement
      //      outputStacks[index] = StackConverter.convertToStack(items[index]);
    }
    if (outputStacks != null) {
      return outputStacks;
    }
    return null;
  }

  public void setItems(ItemStack[] stacks) {
    if (stacks != null) {
      String[] outputStacks = new String[54];
      for (int index = 0; index < stacks.length; index++) {
        // TODO Implement
        //        outputStacks[index] = StackConverter.convertToString(stacks[index]);
      }
      items = outputStacks;
    } else {
      items = new String[54];
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getID() {
    return name;
  }
}
