package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerJoinEvent {

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent e) {
        if(e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            DataHelper.registerPlayer(player);
        }
    }
}
