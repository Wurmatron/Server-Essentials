package wurmcraft.serveressentials.common.commands.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import wurmcraft.serveressentials.common.event.PlayerTickEvent;

public class PlayerInventory extends InventoryBasic {

    public EntityPlayerMP viewer;
    public EntityPlayerMP owner;
    public boolean allowUpdate;

    public PlayerInventory(EntityPlayerMP owner, EntityPlayerMP viewer) {
        super(owner.getName() + "'s inventory", false, owner.inventory.mainInventory.length);
        this.owner = owner;
        this.viewer = viewer;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        allowUpdate = false;
        for (int id = 0; id < owner.inventory.mainInventory.length; ++id) {
            setInventorySlotContents(id, owner.inventory.mainInventory[id]);
        }
        allowUpdate = true;
        PlayerTickEvent.register(this);
        super.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (allowUpdate) {
            for (int id = 0; id < owner.inventory.mainInventory.length; ++id) {
                owner.inventory.mainInventory[id] = getStackInSlot(id);
            }
        }
        PlayerTickEvent.remove(this);
        markDirty();
        super.closeInventory(player);
    }

    @Override
    public void markDirty() {

        super.markDirty();
        if (allowUpdate) {
            for (int id = 0; id < owner.inventory.mainInventory.length; ++id) {
                owner.inventory.mainInventory[id] = getStackInSlot(id);
            }
        }
    }

    public void update() {
        allowUpdate = false;
        for (int id = 0; id < owner.inventory.mainInventory.length; ++id) {
            setInventorySlotContents(id, owner.inventory.mainInventory[id]);
        }
        allowUpdate = true;
        markDirty();
    }
}
