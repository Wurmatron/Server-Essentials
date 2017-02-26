package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;

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
        if (openInv.size() > 0) {
            if (openInv.containsKey(e.player))
                openInv.get(e.player).update();
        }
    }
}
