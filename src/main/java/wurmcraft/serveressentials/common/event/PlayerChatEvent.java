package wurmcraft.serveressentials.common.event;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerChatEvent {

    @SubscribeEvent
    public void onChat(ServerChatEvent e) {
        IRank rank = DataHelper.getPlayerData(e.getPlayer().getGameProfile().getId()).getRank();
        ITextComponent comp = new TextComponentTranslation("chat.type.text", rank.getPrefix()).appendSibling(e.getPlayer().getDisplayName()).appendSibling(new TextComponentTranslation(" ")).appendSibling(ForgeHooks.newChatWithLinks(e.getMessage()));
        e.setComponent(comp);
    }
}
