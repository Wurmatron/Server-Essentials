package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.HashMap;

public class PlayerTickEvent {

		private static HashMap<EntityPlayer, PlayerInventory> openInv = new HashMap<>();

		public static void register(PlayerInventory inv) {
				openInv.put(inv.owner, inv);
		}

		public static void remove(PlayerInventory inv) {
				openInv.remove(inv.owner, inv);
		}

		@SubscribeEvent
		public void tickStart(TickEvent.PlayerTickEvent e) {
				if (openInv.size() > 0 && openInv.containsKey(e.player)) openInv.get(e.player).update();
				if (DataHelper.activeRequests.size() > 0 && e.player.worldObj.getWorldTime() % 20 == 0)
						for (long time : DataHelper.activeRequests.keySet())
								if ((time + (Settings.tpa_timeout * 1000)) <= System.currentTimeMillis())
										DataHelper.activeRequests.remove(time);
		}
}
