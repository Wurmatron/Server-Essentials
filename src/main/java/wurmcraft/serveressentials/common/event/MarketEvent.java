package wurmcraft.serveressentials.common.event;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.utils.LogHandler;
import wurmcraft.serveressentials.common.utils.WorldUtils;

import java.util.ArrayList;
import java.util.UUID;

public class MarketEvent {

	public static ArrayList <UUID> disableDrops = new ArrayList <> ();

	@SubscribeEvent
	public void onItemDrop (ItemTossEvent e) {
		if (disableDrops.size () > 0 && disableDrops.contains (e.getPlayer ().getGameProfile ().getId ()))
			e.setCanceled (true);
	}

	// TODO Just a test place holder
	@SubscribeEvent
	public void onInteract (PlayerInteractEvent e) {
		if (e.getWorld ().getTileEntity (e.getPos ()) != null && e.getWorld ().getTileEntity (e.getPos ()) instanceof TileEntitySign) {
			ITextComponent[] signText = ((TileEntitySign) e.getWorld ().getTileEntity (e.getPos ())).signText;
			if (signText[0].getUnformattedText ().length () > 0 && signText[0].getUnformattedText ().equalsIgnoreCase ("[Buy]")) {
				String[] text = new String[4];
				for(int i = 0; i < 4; i++)
					text[i] = signText[i].getUnformattedText ();
				text[0] = TextFormatting.RED + "[Buy]";
				WorldUtils.setSignText (e.getWorld (),e.getPos (),text);
			} else if (signText[0].getUnformattedText ().length () > 0 && signText[0].getUnformattedText ().equalsIgnoreCase ("[Sell]")) {
				String[] text = new String[4];
				for(int i = 0; i < 4; i++)
					text[i] = signText[i].getUnformattedText ();
				text[0] = TextFormatting.RED + "[Sell]";
			} else if (signText[0].getUnformattedText ().length () > 0 && signText[0].getUnformattedText ().equalsIgnoreCase ("[Command]") || signText[0].getUnformattedText ().length () > 0 && signText[0].getUnformattedText ().equalsIgnoreCase ("[Execute]")) {
				LogHandler.info ("Command");
			}
		}
	}
}
