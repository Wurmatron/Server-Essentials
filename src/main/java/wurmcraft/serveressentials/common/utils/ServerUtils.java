package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 Created by matthew on 6/25/17.
 */
@SideOnly (Side.SERVER)
public class ServerUtils {

	@SideOnly (Side.SERVER)
	public static class CustomDedicatedPlayerList extends DedicatedPlayerList {

		public String disconnectMessage = "Server closed";

		public CustomDedicatedPlayerList (DedicatedPlayerList list) {
			super (list.getServerInstance ());
		}

		public void setDisconnectMessage (String message) {
			this.disconnectMessage = message;
		}

		@Override
		public void removeAllPlayers () {
			for (int i = 0; i < getPlayerList ().size (); ++i) {
				(getPlayerList ().get (i)).connection.kickPlayerFromServer (disconnectMessage);
			}
		}
	}

	public static void customizeShutdownMessage (String newMessage) {
		//        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		//        server.setPlayerList((DedicatedPlayerList)new CustomDedicatedPlayerList((DedicatedPlayerList)server.getPlayerList()));
	}
}
