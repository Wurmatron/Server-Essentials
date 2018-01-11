package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class PlayerDeathEvent {

	@SubscribeEvent
	public void onPlayerDeath (LivingDeathEvent e) {
		if (e.getEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity ();
			PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,((EntityPlayer) e.getEntity ()).getGameProfile ().getId ().toString ());
			playerData.setLastLocation (new BlockPos (player.posX,player.posY,player.posZ));
			if (playerData.getCustomData ().length > 0) {
				for (String data : playerData.getCustomData ())
					if (data.equalsIgnoreCase ("perk.keepInv"))
						PlayerRespawnEvent.add (player.getGameProfile ().getId (),new ItemStack[][] {player.inventory.mainInventory.toArray (new ItemStack[0]),player.inventory.armorInventory.toArray (new ItemStack[0]),player.inventory.offHandInventory.toArray (new ItemStack[0])});
			}
		}
	}
}
