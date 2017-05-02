package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerDeathEvent {

	@SubscribeEvent
	public void onPlayerDeath (LivingDeathEvent e) {
		if (e.getEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity ();
			DataHelper.setLastLocation (player.getGameProfile ().getId (),new BlockPos (player.posX,player.posY,player.posZ));
		}
	}
}
