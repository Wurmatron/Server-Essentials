package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.item.ItemStack;

public class Kit {

	private ItemStack[] items;
	private int time;

	public Kit (ItemStack[] items,int time) {
		this.items = items;
		this.time = time;
	}

	public ItemStack[] getItems () {
		return items;
	}

	public int getTime () {
		return time;
	}

	public void setTime (int time) {
		this.time = time;
	}
}
