package wurmcraft.serveressentials.common.commands.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import wurmcraft.serveressentials.common.api.storage.Vault;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.UUID;

public class VaultInventory extends InventoryBasic {

	public EntityPlayerMP viewer;
	public UUID owner;
	public Vault vault;

	public VaultInventory (EntityPlayerMP viewer,UUID owner,Vault vault) {
		super (vault.getName (),false,vault.getItems ().length);
		this.viewer = viewer;
		this.owner = owner;
	}

	@Override
	public void openInventory (EntityPlayer player) {
		try {
			ItemStack[] vaultItems = vault.getItems ();
			if (vaultItems != null)
				for (int id = 0; id < vaultItems.length; id++)
					setInventorySlotContents (id,vaultItems[id]);
			super.openInventory (player);
		}catch (NullPointerException e) {}
	}

	@Override
	public void closeInventory (EntityPlayer player) {
		ItemStack[] vaultItems = vault.getItems ();
		ItemStack[] currentItems = new ItemStack[vaultItems.length];
		for (int id = 0; id < vaultItems.length; id++)
			currentItems[id] = getStackInSlot (id);
		vault.setItems (currentItems);
		markDirty ();
	}

	@Override
	public void markDirty () {
		super.markDirty ();
		DataHelper.saveVault (owner,vault);
	}
}
