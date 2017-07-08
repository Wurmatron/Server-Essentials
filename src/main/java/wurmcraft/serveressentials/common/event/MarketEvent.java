package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.StackConverter;
import wurmcraft.serveressentials.common.utils.WorldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketEvent {

	public static ArrayList <UUID> disableDrops = new ArrayList <> ();

	@SubscribeEvent
	public void onItemDrop (ItemTossEvent e) {
		if (disableDrops.size () > 0 && disableDrops.contains (e.getPlayer ().getGameProfile ().getId ()))
			e.setCanceled (true);
	}

	@SubscribeEvent
	public void onInteract (PlayerInteractEvent e) {
		if (e.getWorld ().getTileEntity (e.getPos ()) != null && e.getWorld ().getTileEntity (e.getPos ()) instanceof TileEntitySign) {
			String[] signText = getLines (e.getWorld (),e.getPos ());
			if (signText[0].equalsIgnoreCase ("[IBuy]")) {
				if (isValid (e.getWorld (),e.getPos ())) {
					if (DataHelper.getMoney (e.getEntityPlayer ().getGameProfile ().getId ()) >= getPrice (e.getWorld (),e.getPos ())) {
						if (addStack (e.getEntityPlayer (),getStack (e.getWorld (),e.getPos ()))) {
							DataHelper.setMoney (e.getEntityPlayer ().getGameProfile ().getId (),DataHelper.getMoney (e.getEntityPlayer ().getGameProfile ().getId ()) - getPrice (e.getWorld (),e.getPos ()));
							ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.PURCHASE.replaceAll ("@","" + getPrice (e.getWorld (),e.getPos ())).replaceAll ("#",getStack (e.getWorld (),e.getPos ()).stackSize + "x " + getStack (e.getWorld (),e.getPos ()).getDisplayName ()));
						} else
							ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.PLAYER_INVENTORY_FULL);
					} else
						ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.MONEY_NONE);
				} else if (getPrice (e.getWorld (),e.getPos ()) >= 0 && e.getEntityPlayer ().getHeldItemMainhand () != null && getStack (e.getWorld (),e.getPos ()) == null) {
					setShopBuy (e.getWorld (),e.getPos (),e.getEntityPlayer ().getHeldItemMainhand ());
				} else
					ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.SIGN_INVALID);
			} else if (signText[0].equalsIgnoreCase ("[ISell]")) {
				if (isValid (e.getWorld (),e.getPos ())) {
					if (DataHelper.getMoney (e.getEntityPlayer ().getGameProfile ().getId ()) >= getPrice (e.getWorld (),e.getPos ())) {
						if (hasStack (e.getEntityPlayer (),getStack (e.getWorld (),e.getPos ()))) {
							consumeStack (e.getEntityPlayer (),getStack (e.getWorld (),e.getPos ()));
							ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.ITEM_SOLD.replaceAll ("#",getStack (e.getWorld (),e.getPos ()).stackSize + "x " + getStack (e.getWorld (),e.getPos ()).getDisplayName ()).replaceAll ("@","" + getPrice (e.getWorld (),e.getPos ())));
							DataHelper.setMoney (e.getEntityPlayer ().getGameProfile ().getId (),DataHelper.getMoney (e.getEntityPlayer ().getGameProfile ().getId ()) + getPrice (e.getWorld (),e.getPos ()));
						} else
							ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.ITEM_NONE.replaceAll ("#",getStack (e.getWorld (),e.getPos ()).getDisplayName ()));
					} else
						ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.MONEY_NONE);
				} else if (getPrice (e.getWorld (),e.getPos ()) >= 0 && e.getEntityPlayer ().getHeldItemMainhand () != null && getStack (e.getWorld (),e.getPos ()) == null) {
					setShopBuy (e.getWorld (),e.getPos (),e.getEntityPlayer ().getHeldItemMainhand ());
				} else
					ChatHelper.sendMessageTo (e.getEntityPlayer (),Local.SIGN_INVALID);
			}
		}
	}

	private void setShopBuy (World world,BlockPos pos,ItemStack stack) {
		TileEntitySign tile = (TileEntitySign) world.getTileEntity (pos);
		tile.getTileData ().setString ("stack",StackConverter.convertToString (stack));
		String[] text = getLines (world,pos);
		if (text[0].equalsIgnoreCase ("[IBuy]"))
			text[0] = TextFormatting.GOLD + "[" + TextFormatting.DARK_RED + TextFormatting.BOLD + "IBuy" + TextFormatting.RESET + TextFormatting.GOLD + "]";
		else if (text[0].equalsIgnoreCase ("[ISell]"))
			text[0] = TextFormatting.GOLD + "[" + TextFormatting.DARK_RED + TextFormatting.BOLD + "ISell" + TextFormatting.RESET + TextFormatting.GOLD + "]";
		text[3] = TextFormatting.AQUA + text[3];
		WorldUtils.setSignText (world,pos,text);
	}

	private ItemStack getStack (World world,BlockPos pos) {
		TileEntitySign tile = (TileEntitySign) world.getTileEntity (pos);
		return StackConverter.convertToStack (tile.getTileData ().getString ("stack"));
	}

	private String[] getLines (World world,BlockPos pos) {
		TileEntitySign tile = (TileEntitySign) world.getTileEntity (pos);
		List <String> lines = new ArrayList <> ();
		for (ITextComponent text : tile.signText)
			lines.add (TextFormatting.getTextWithoutFormattingCodes (text.getFormattedText ()));
		return lines.toArray (new String[0]);
	}

	private boolean isValid (World world,BlockPos pos) {
		return getStack (world,pos) != null && getPrice (world,pos) >= 0;
	}

	private int getPrice (World world,BlockPos pos) {
		try {
			return Integer.parseInt (getLines (world,pos)[3].replaceAll (Settings.currencySymbol,""));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private boolean addStack (EntityPlayer player,ItemStack stack) {
		return player.inventory.addItemStackToInventory (stack);
	}

	private boolean hasStack (EntityPlayer player,ItemStack stack) {
		for(ItemStack item : player.inventory.mainInventory)
			if(item != null && item.isItemEqual (stack) && item.stackSize > 0)
				return true;
		return false;
	}

	private void consumeStack (EntityPlayer player,ItemStack stack) {
		int amountLeft = stack.stackSize;
		for (int index = 0; index < player.inventory.mainInventory.length; index++)
			if (amountLeft <= 0)
				return;
			else if (ItemStack.areItemsEqual (stack,player.inventory.mainInventory[index])) {
				if (stack.stackSize <= player.inventory.mainInventory[index].stackSize) {
					player.inventory.mainInventory[index].stackSize -= stack.stackSize;
					break;
				}
				else if (stack.stackSize > player.inventory.mainInventory[index].stackSize) {
					amountLeft -= player.inventory.mainInventory[index].stackSize;
					player.inventory.setInventorySlotContents (index,null);
				}
			}
	}
}
