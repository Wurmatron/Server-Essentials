package wurmcraft.serveressentials.common.commands.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class MarketInventory extends InventoryBasic {

	public EntityPlayerMP viewer;
	public boolean allowUpdate;

	public MarketInventory (EntityPlayerMP viewer) {
		super (viewer.getTabListDisplayName () + "'s inventory",false,54);
		this.viewer = viewer;
	}

	@Override
	public void openInventory (EntityPlayer player) {
		allowUpdate = false;
		for (int id = 0; id < 54; ++id)
			setInventorySlotContents (id,new ItemStack (Items.BLAZE_ROD,1 + id));
		allowUpdate = true;
		//		PlayerTickEvent.register (this);
		super.openInventory (player);

	}

	@Override
	public void closeInventory (EntityPlayer player) {
		if (allowUpdate)
			for (int id = 0; id < 56; ++id)
				//				owner.inventory.mainInventory[id] = getStackInSlot (id);
				//		PlayerTickEvent.remove (this);
				markDirty ();
		super.closeInventory (player);

	}

	@Override
	public void markDirty () {
		super.markDirty ();
		//		if (allowUpdate)
		//			for (int id = 0; id < 56; ++id)
		//				owner.inventory.mainInventory[id] = getStackInSlot (id);

	}

	public void update () {
		allowUpdate = false;
		//		for (int id = 0; id < owner.inventory.mainInventory.length; ++id)
		//			setInventorySlotContents (id,owner.inventory.mainInventory[id]);
		allowUpdate = true;
		markDirty ();
	}
}
