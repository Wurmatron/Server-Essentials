package com.wurmcraft.serveressentials.api.user.transfer;

public class ItemBin {

  public String transferID;
  public String[] items;

  public ItemBin(String transferID, String[] storage) {
    this.transferID = transferID;
    this.items = storage;
  }
}
