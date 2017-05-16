package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.item.ItemStack;
import wurmcraft.serveressentials.common.utils.StackConverter;

public class Vault {

	private String name;
	private String[] items = new String[54];

	public Vault (String name,ItemStack[] items) {
		this.name = name;
		setItems (items);
	}

	public ItemStack[] getItems () {
		ItemStack[] outputStacks = new ItemStack[54];
		for (int index = 0; index < items.length; index++)
			outputStacks[index] = StackConverter.convertToStack (items[index]);
		if (outputStacks != null)
			return outputStacks;
		return null;
	}

	public void setItems (ItemStack[] stacks) {
		if (stacks != null) {
			String[] outputStacks = new String[54];
			for (int index = 0; index < stacks.length; index++)
				outputStacks[index] = StackConverter.convertToString (stacks[index]);
			items = outputStacks;
		} else
			items = new String[54];
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}
}
