package wurmcraft.serveressentials.common.event;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.Channel;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class PlayerChatEvent {

  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onChat(ServerChatEvent e) {
    PlayerData playerData = (PlayerData) DataHelper2
        .get(Keys.PLAYER_DATA, e.getPlayer().getGameProfile().getId().toString());
		if (!playerData.isMuted()) {
			Channel ch = ChannelManager.getPlayerChannel(e.getPlayer().getGameProfile().getId());
			if (DataHelper2.globalSettings.getGlobalChannel().equals(ch.getName())) {
				String name = playerData.getNickname() != null && playerData.getNickname().length() > 0 ?
						TextFormatting.GRAY + "*" + TextFormatting.RESET + playerData.getNickname()
								.replaceAll("&", "\u00A7") : e.getUsername();
				Team team = playerData.getTeam();
				ChatHelper.sendMessage(name, playerData.getRank(),
						ChannelManager.getPlayerChannel(e.getPlayer().getGameProfile().getId()),
						e.getPlayer().dimension, team, e.getMessage());
				e.setCanceled(true);
			} else {
				e.setCanceled(true);

			}
		} else {
			ChatHelper.sendMessageTo(e.getPlayer(), Local.NOTIFY_MUTED);
		}

    TextComponentString string = new TextComponentString("");
    string.setStyle()
  }
}
