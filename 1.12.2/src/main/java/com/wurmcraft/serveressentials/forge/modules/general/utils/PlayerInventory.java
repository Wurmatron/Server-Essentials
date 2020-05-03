package com.wurmcraft.serveressentials.forge.modules.general.utils;

import com.wurmcraft.serveressentials.forge.modules.general.event.GeneralEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;

public class PlayerInventory extends InventoryBasic {

  public final EntityPlayer viewer;
  public final EntityPlayer owner;
  public boolean allowUpdate;
  public boolean canEdit;
  public boolean echest;

  public PlayerInventory(EntityPlayer owner, EntityPlayer viewer) {
    super(owner.getName() + "'s inventory", false, owner.inventory.mainInventory.size());
    this.owner = owner;
    this.viewer = viewer;
    canEdit = false;
  }

  public PlayerInventory(EntityPlayer owner, EntityPlayer viewer, boolean echest) {
    super(owner.getName() + "'s inventory", false, owner.inventory.mainInventory.size());
    this.owner = owner;
    this.viewer = viewer;
    this.echest = echest;
    canEdit = false;
  }

  @Override
  public void openInventory(EntityPlayer player) {
    if (!echest) {
      allowUpdate = false;
      for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
        setInventorySlotContents(id, owner.inventory.mainInventory.get(id));
      }
      allowUpdate = true;
      GeneralEvents.register(this);
      super.openInventory(player);
    } else {
      allowUpdate = false;
      for (int id = 0; id < owner.getInventoryEnderChest().getSizeInventory(); ++id) {
        setInventorySlotContents(id, owner.getInventoryEnderChest().getStackInSlot(id));
      }
      allowUpdate = true;
      GeneralEvents.register(this);
      super.openInventory(player);
    }
  }

  @Override
  public void closeInventory(EntityPlayer player) {
    if (!echest) {
      if (allowUpdate && canEdit) {
        for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
          owner.inventory.mainInventory.set(id, getStackInSlot(id));
        }
      }
      GeneralEvents.remove(this);
      markDirty();
      super.closeInventory(player);
    } else {
      if (allowUpdate && canEdit) {
        for (int id = 0; id < owner.getInventoryEnderChest().getSizeInventory(); ++id) {
          owner.getInventoryEnderChest().setInventorySlotContents(id, getStackInSlot(id));
        }
      }
      GeneralEvents.remove(this);
      markDirty();
      super.closeInventory(player);
    }
  }

  @Override
  public void markDirty() {
    if (!echest) {
      super.markDirty();
      if (allowUpdate && canEdit) {
        for (int id = 0; id < owner.inventory.mainInventory.size(); ++id) {
          owner.inventory.mainInventory.set(id, getStackInSlot(id));
        }
      }
    } else {
      super.markDirty();
      if (allowUpdate && canEdit) {
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