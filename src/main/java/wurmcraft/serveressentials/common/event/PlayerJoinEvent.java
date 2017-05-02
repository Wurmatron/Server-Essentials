package wurmcraft.serveressentials.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.utils.ChatManager;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin (PlayerEvent.PlayerLoggedInEvent e) {
		DataHelper.registerPlayer (e.player);
		DataHelper.updateLastseen (e.player.getGameProfile ().getId ());
		String[] motd = DataHelper.globalSettings.getMotd ();
		if (motd != null && motd.length > 0)
			for (String mod : motd)
				ChatManager.sendMessage (e.player,mod.replaceAll ("&","\u00A7"));
	}
}
