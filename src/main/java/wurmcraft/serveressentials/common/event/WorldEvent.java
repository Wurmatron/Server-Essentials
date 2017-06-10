package wurmcraft.serveressentials.common.event;


import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class WorldEvent {

	@SubscribeEvent
	public void onWorldTick (TickEvent.WorldTickEvent e) {
		if (e.world.getWorldTime () % 12000 == 0) {
			DataHelper.handleAndUpdatePlayTime ();
		}
	}

}
