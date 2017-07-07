package wurmcraft.serveressentials.common.event;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerChatEvent {

	@SubscribeEvent
	public void onChat (ServerChatEvent e) {
		e.setCanceled (true);
		if (!DataHelper.isMuted (e.getPlayer ().getGameProfile ().getId ())) {
			PlayerData data = DataHelper.getPlayerData (e.getPlayer ().getGameProfile ().getId ());
			String name = data.getNickname () != null ? TextFormatting.GRAY + "*" + TextFormatting.RESET + data.getNickname ().replaceAll ("&","\u00A7") : e.getUsername ();
			Team team = data.getTeam ();
			ChatHelper.sendMessage (name,DataHelper.getPlayerData (e.getPlayer ().getGameProfile ().getId ()).getRank (),ChannelManager.getPlayerChannel (e.getPlayer ().getGameProfile ().getId ()),e.getPlayer ().dimension,team,e.getMessage ());
		} else
			ChatHelper.sendMessageTo (e.getPlayer (),Local.NOTIFY_MUTED);
	}
}
