package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.item.ItemStack;
import wurmcraft.serveressentials.common.utils.StackConverter;

public class ShopData {

	private String[] marketItems;
	private int[] amount;
	private boolean[] buy;

	public ShopData (ItemStack[] items,int[] data,boolean[] buy) {
		if (items.length == data.length && data.length == buy.length) {
			String[] marketStacks = new String[items.length];
			for (int index = 0; index < items.length; index++)
				marketStacks[index] = StackConverter.convertToString (items[index]);
			this.marketItems = marketStacks;
			this.amount = data;
			this.buy = buy;
		}
	}

	public ItemStack[] getMarketItems () {
		ItemStack[] items = new ItemStack[marketItems.length];
		for (int index = 0; index < items.length; index++)
			items[index] = StackConverter.convertToStack (marketItems[index]);
		return items;
	}

	public void setMarketItems (ItemStack[] items) {
		String[] marketStacks = new String[marketItems.length];
		for (int index = 0; index < items.length; index++)
			marketStacks[index] = StackConverter.convertToString (items[index]);
		this.marketItems = marketStacks;
	}

	public int[] getAmount () {
		return amount;
	}

	public void setAmount (int[] amount) {
		this.amount = amount;
	}

	public boolean[] getBuy () {
		return buy;
	}

	public void setBuy (boolean[] buy) {
		this.buy = buy;
	}
}
