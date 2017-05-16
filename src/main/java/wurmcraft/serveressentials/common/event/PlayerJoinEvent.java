package wurmcraft.serveressentials.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin (PlayerEvent.PlayerLoggedInEvent e) {
		DataHelper.registerPlayer (e.player);
		DataHelper.updateLastseen (e.player.getGameProfile ().getId ());
		String[] motd = DataHelper.globalSettings.getMotd ();
		if (motd != null && motd.length > 0)
			for (String mod : motd)
				ChatHelper.sendMessageTo (e.player,mod.replaceAll ("&","\u00A7"));
		if(Settings.forceChannelOnJoin)
			DataHelper.setChannel (e.player.getGameProfile ().getId (),ChannelManager.getFromName (Settings.default_channel));
		ChannelManager.setPlayerChannel (e.player.getGameProfile ().getId (), DataHelper.getChannel (e.player.getGameProfile ().getId ()));
		DataHelper.loadVault (e.player.getGameProfile ().getId ());
	}
}
