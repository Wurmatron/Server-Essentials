package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.RankManager;

import java.io.File;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin (PlayerEvent.PlayerLoggedInEvent e) {
		boolean newPlayer = DataHelper2.createIfNonExist (ConfigHandler.playerDataLocation,new PlayerData (e.player.getGameProfile ().getId (),RankManager.getDefaultRank ()));
		PlayerData playerData = DataHelper2.load (new File (ConfigHandler.playerDataLocation + File.separator + e.player.getGameProfile ().getId () + ".json"),Keys.PLAYER_DATA,new PlayerData ());
		if (playerData != null) {
			if (newPlayer)
				playerData.setFirstJoin ();
			playerData.setLastseen (System.currentTimeMillis ());
			if (Settings.forceChannelOnJoin) {
				playerData.setCurrentChannel (ChannelManager.getFromName (Settings.default_channel));
				ChannelManager.setPlayerChannel (e.player.getGameProfile ().getId (),DataHelper.getChannel (e.player.getGameProfile ().getId ()));
			}
			if (playerData.getMail () != null && playerData.getMail ().size () > 0)
				ChatHelper.sendMessageTo (e.player,Local.HAS_MAIL);
			if (playerData.isSpy ())
				DataHelper.spys.add (e.player.getGameProfile ().getId ());
			if (!DataHelper.joinTime.containsKey (e.player.getGameProfile ().getId ()))
				DataHelper.joinTime.put (e.player.getGameProfile ().getId (),System.currentTimeMillis ());
			DataHelper.handleAndUpdatePlayTime ();
			if (DataHelper.getPlayerData (e.player.getGameProfile ().getId ()).isFrozen ())
				PlayerTickEvent.addFrozen (e.player,new BlockPos (e.player.posX,e.player.posY,e.player.posZ));
			if (!SecurityUtils.isTrustedMember (e.player) && DataHelper.globalSettings.getBannedMods ().length > 0)
				for (String id : SecurityUtils.getPlayerMods (e.player))
					for (String modid : DataHelper.globalSettings.getBannedMods ())
						if (modid.equalsIgnoreCase (id))
							((EntityPlayerMP) e.player).connection.disconnect (new TextComponentString (Local.BANNED_MOD.replaceAll ("#",id)));
		}
		String[] motd = DataHelper.globalSettings.getMotd ();
		if (motd != null && motd.length > 0)
			for (String mod : motd)
				ChatHelper.sendMessageTo (e.player,mod.replaceAll ("&","\u00A7"));
	}
}
