package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.item.ItemStack;
import wurmcraft.serveressentials.common.utils.StackConverter;

import java.util.ArrayList;
import java.util.List;

public class Kit {

	private String name;
	private String[] items;
	private int time;

	public Kit (String name,ItemStack[] items,int time) {
		this.name = name;
		List <String> stack = new ArrayList <> ();
		for (ItemStack item : items)
			stack.add (StackConverter.convertToString (item));
		this.items = stack.toArray (new String[0]);
		this.time = time;
	}

	public ItemStack[] getItems () {
		List <ItemStack> stacks = new ArrayList <> ();
		for (String item : items)
			stacks.add (StackConverter.convertToStack (item));
		return stacks.toArray (new ItemStack[0]);
	}

	public int getTime () {
		return time;
	}

	public void setTime (int time) {
		this.time = time;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public void setItems (ItemStack[] items) {
		List <String> stack = new ArrayList <> ();
		for (ItemStack item : items)
			stack.add (StackConverter.convertToString (item));
		this.items = stack.toArray (new String[0]);
	}
}
