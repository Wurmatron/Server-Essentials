package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.SpawnPoint;
import wurmcraft.serveressentials.common.utils.DataHelper2;

import java.util.HashMap;
import java.util.UUID;

public class PlayerRespawnEvent {

	private static HashMap <UUID, ItemStack[][]> suicideData = new HashMap <> ();

	@SubscribeEvent
	public void constructEvent (PlayerEvent.Clone e) {
		//		if (e.isWasDeath () && !e.isCanceled ()) {
		//			if (Settings.respawn_point.equalsIgnoreCase ("spawn")) {
		//				teleportToSpawn (e.getEntityPlayer ());
		//			} else if (Settings.respawn_point.equalsIgnoreCase ("home")) {
		//				PlayerData playerData = DataHelper.getPlayerData (e.getEntityPlayer ().getGameProfile ().getId ());
		//				Home defaultHome = playerData.getHome (Settings.home_name);
		//				if (defaultHome != null) {
		//					e.getEntityPlayer ().setLocationAndAngles (defaultHome.getPos ().getX (),defaultHome.getPos ().getY (),defaultHome.getPos ().getZ (),defaultHome.getYaw (),defaultHome.getPitch ());
		//					e.getEntityPlayer ().dimension = defaultHome.getDimension ();
		//				}
		//			}
		//		}
		//		if (suicideData.size () > 0 && suicideData.get (e.getEntityPlayer ().getGameProfile ().getId ()) != null) {
		//			ItemStack[][] inv = suicideData.get (e.getEntityPlayer ().getGameProfile ().getId ());
		//			for (int index = 0; index < inv[0].length; index++)
		//				e.getEntityPlayer ().inventory.setInventorySlotContents (index,inv[0][index]);
		//			e.getEntityPlayer ().inventory.armorInventory.set (0,inv[1][0]);
		//			e.getEntityPlayer ().inventory.armorInventory.set (1,inv[1][1]);
		//			e.getEntityPlayer ().inventory.armorInventory.set (2,inv[1][2]);
		//			e.getEntityPlayer ().inventory.armorInventory.set (3,inv[1][3]);
		//			e.getEntityPlayer ().inventory.offHandInventory.set (0,inv[2][0]);
		//			remove (e.getEntityPlayer ().getGameProfile ().getId ());
		//		}
	}

	private void teleportToSpawn (EntityPlayer player) {
		SpawnPoint spawn = DataHelper2.globalSettings.getSpawn ();
		if (spawn != null) {
			player.setLocationAndAngles (spawn.location.getX (),spawn.location.getY (),spawn.location.getZ (),spawn.yaw,spawn.pitch);
			player.dimension = spawn.dimension;
		}
	}

	public static void add (UUID uuid,ItemStack[][] inventory) {
		if (!suicideData.keySet ().contains (uuid) && inventory != null)
			suicideData.put (uuid,inventory);
	}

	public static void remove (UUID uuid) {
		if (suicideData.containsKey (uuid))
			suicideData.remove (uuid);
	}
}
