package wurmcraft.serveressentials.common.event;

import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.UUID;

public class MarketEvent {

	public static ArrayList <UUID> disableDrops = new ArrayList <> ();

	@SubscribeEvent
	public void onItemDrop (ItemTossEvent e) {
		if (disableDrops.size () > 0 && disableDrops.contains (e.getPlayer ().getGameProfile ().getId ()))
			e.setCanceled (true);
	}
}
