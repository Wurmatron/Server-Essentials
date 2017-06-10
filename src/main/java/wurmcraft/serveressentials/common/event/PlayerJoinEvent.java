package wurmcraft.serveressentials.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
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
		if (Settings.forceChannelOnJoin)
			DataHelper.setChannel (e.player.getGameProfile ().getId (),ChannelManager.getFromName (Settings.default_channel));
		ChannelManager.setPlayerChannel (e.player.getGameProfile ().getId (),DataHelper.getChannel (e.player.getGameProfile ().getId ()));
		DataHelper.loadVault (e.player.getGameProfile ().getId ());
		PlayerData data = DataHelper.getPlayerData (e.player.getGameProfile ().getId ());
		if (data != null && data.getMail () != null && data.getMail ().size () > 0)
			ChatHelper.sendMessageTo (e.player,Local.HAS_MAIL);
		if (data.isSpy ())
			DataHelper.spys.add (e.player.getGameProfile ().getId ());
		if (!DataHelper.joinTime.containsKey (e.player.getGameProfile ().getId ()))
			DataHelper.joinTime.put (e.player.getGameProfile ().getId (),System.currentTimeMillis ());
	}
}
