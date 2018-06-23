package wurmcraft.serveressentials.common.security;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.LogHandler;

public class SecurityEvents {

  @SubscribeEvent
  public void creativeCheck(TickEvent.PlayerTickEvent e) {
    if (e.player.world.getWorldTime() % 100 == 0) {
      IRank rank = ((PlayerData) DataHelper2
          .get(Keys.PLAYER_DATA, e.player.getGameProfile().getId().toString())).getRank();
      boolean validInCreative = false;
      if (rank.hasPermission(Perm.CREATIVE)) {
        validInCreative = true;
      }
      if (e.player.capabilities.isCreativeMode && !validInCreative && !SecurityUtils
          .isTrustedMember(e.player)) {
        EntityPlayerMP player = (EntityPlayerMP) e.player;
        player.setGameType(GameType.SURVIVAL);
        player.connection.disconnect(new TextComponentString(Local.SECURITY_CREATIVE_KICK));
        LogHandler.info("# was kicked from the server for being in creative!"
            .replaceAll("#", player.getDisplayNameString()));
      }

    }
  }
}
