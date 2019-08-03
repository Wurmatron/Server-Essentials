package com.wurmcraft.serveressentials.api.user.transfer;

public class TransferBin {

  public String uuid;
  public ItemBin[] transfers;

  public TransferBin(String uuid, ItemBin[] transfers) {
    this.uuid = uuid;
    this.transfers = transfers;
  }
}
