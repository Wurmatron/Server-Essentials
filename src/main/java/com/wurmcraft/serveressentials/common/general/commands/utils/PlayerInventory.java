package com.wurmcraft.serveressentials.common.general.commands.utils;

import com.wurmcraft.serveressentials.common.general.events.PlayerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;

public class PlayerInventory extends InventoryBasic {

  public EntityPlayerMP viewer;
  public EntityPlayerMP owner;
  public boolean allowUpdate;
  public boolean echest;

  public PlayerInventory(EntityPlayerMP owner, EntityPlayerMP viewer) {
    super(owner.getName() + "'s inventory", false, owner.inventory.mainInventory.size());
    this.owner = owner;
    this.viewer = viewer;
  }

  public PlayerInventory(EntityPlayerMP owner, EntityPlayerMP viewer, boolean echest) {
    super(owner.getName() + "'s inventory", false, owner.inventory.mainInventory.size());
    this.owner = owner;
    this.viewer = viewer;
    this.echest = echest;
  }

  @Override
  public void openInventory(EntityPlayer player) {
    if (!echest) {
      allowUpdate = false;
      for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
        setInventorySlotContents(id, owner.inventory.mainInventory.get(id));
      }
      allowUpdate = true;
      PlayerTickEvent.register(this);
      super.openInventory(player);
    } else {
      allowUpdate = false;
      for (int id = 0; id < owner.getInventoryEnderChest().getSizeInventory(); ++id) {
        setInventorySlotContents(id, owner.getInventoryEnderChest().getStackInSlot(id));
      }
      allowUpdate = true;
      PlayerTickEvent.register(this);
      super.openInventory(player);
    }
  }

  @Override
  public void closeInventory(EntityPlayer player) {
    if (!echest) {
      if (allowUpdate) {
        for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
          owner.inventory.mainInventory.set(id, getStackInSlot(id));
        }
      }
      PlayerTickEvent.remove(this);
      markDirty();
      super.closeInventory(player);
    } else {
      if (allowUpdate) {
        for (int id = 0; id < owner.getInventoryEnderChest().getSizeInventory(); ++id) {
          owner.getInventoryEnderChest().setInventorySlotContents(id, getStackInSlot(id));
        }
      }
      PlayerTickEvent.remove(this);
      markDirty();
      super.closeInventory(player);
    }
  }

  @Override
  public void markDirty() {
    if (!echest) {
      super.markDirty();
      if (allowUpdate) {
        for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
          owner.inventory.mainInventory.set(id, getStackInSlot(id));
        }
      }
    } else {
      super.markDirty();
      if (allowUpdate) {
        for (int id = 0; id < owner.getInventoryEnderChest().getSizeInventory(); ++id) {
          owner.getInventoryEnderChest().setInventorySlotContents(id, getStackInSlot(id));
        }
      }
    }
  }

  public void update() {
    if (!echest) {
      allowUpdate = false;
      for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
        setInventorySlotContents(id, owner.inventory.mainInventory.get(id));
      }
      allowUpdate = true;
      markDirty();
    } else {
      allowUpdate = false;
      for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
        setInventorySlotContents(id, owner.getInventoryEnderChest().getStackInSlot(id));
      }
      allowUpdate = true;
      markDirty();
    }
  }
}
