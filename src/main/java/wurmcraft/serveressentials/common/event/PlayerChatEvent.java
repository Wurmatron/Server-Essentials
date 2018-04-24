package wurmcraft.serveressentials.common.event;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class PlayerChatEvent {

	@SubscribeEvent
	public void onChat (ServerChatEvent e) {
		e.setCanceled (true);
		PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,e.getPlayer ().getGameProfile ().getId ().toString ());
		if (!playerData.isMuted ()) {
			String name = playerData.getNickname () != null && playerData.getNickname ().length () > 0 ? TextFormatting.GRAY + "*" + TextFormatting.RESET + playerData.getNickname ().replaceAll ("&","\u00A7") : e.getUsername ();
			Team team = playerData.getTeam ();
			ChatHelper.sendMessage (name,playerData.getRank (),ChannelManager.getPlayerChannel (e.getPlayer ().getGameProfile ().getId ()),e.getPlayer ().dimension,team,e.getMessage ());
		} else
			ChatHelper.sendMessageTo (e.getPlayer (),Local.NOTIFY_MUTED);
	}
}
