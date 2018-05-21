package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.StackConverter;
import wurmcraft.serveressentials.common.utils.WorldUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MarketEvent {

	public static ArrayList <UUID> disableDrops = new ArrayList <> ();
	private static final ItemStack linkStack = new ItemStack (Items.BUCKET);
	private static HashMap <UUID, BlockPos> shops = new HashMap <> ();

	@SubscribeEvent
	public void onInteract (PlayerInteractEvent e) {
		if (e.getWorld ().getTileEntity (e.getPos ()) != null && e.getWorld ().getTileEntity (e.getPos ()) instanceof TileEntitySign) {
			String[] signText = getLines (e.getWorld (),e.getPos ());
			if (signText[0].equalsIgnoreCase ("[IBuy]") && ConfigHandler.buySign) {
				handleIBuySign (e.getEntityPlayer (),e.getWorld (),e.getPos ());
			} else if (signText[0].equalsIgnoreCase ("[ISell]") && ConfigHandler.sellSign) {
				handleISellSign (e.getEntityPlayer (),e.getWorld (),e.getPos ());
			} else if (signText[0].equalsIgnoreCase ("[Buy]") && ConfigHandler.buySign) {
				handleBuySign (e.getEntityPlayer (),e.getWorld (),e.getPos ());
			} else if (signText[0].equalsIgnoreCase ("[Sell]") && ConfigHandler.sellSign) {
				handleSellSign (e.getEntityPlayer (),e.getWorld (),e.getPos ());
			} else if (e.getWorld ().getTileEntity (e.getPos ()) != null && isInventory (e.getWorld (),e.getPos ())) {
				handleLinking (e.getEntityPlayer (),e.getWorld (),e.getPos ());
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
		else if (text[0].equalsIgnoreCase ("[Buy]"))
			text[0] = TextFormatting.GOLD + "[" + TextFormatting.AQUA + "Buy" + TextFormatting.GOLD + "]";
		else if (text[0].equalsIgnoreCase ("[Sell]"))
			text[0] = TextFormatting.GOLD + "[" + TextFormatting.AQUA + "Sell" + TextFormatting.GOLD + "]";
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

	private boolean hasValidLink (World world,BlockPos pos) {
		return getOwner (world,pos) != null;
	}

	private int getPrice (World world,BlockPos pos) {
		try {
			return Integer.parseInt (getLines (world,pos)[3].replaceAll (ConfigHandler.currencySymbol,""));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private boolean addStack (EntityPlayer player,ItemStack stack) {
		return player.inventory.addItemStackToInventory (stack);
	}

	private boolean hasStack (EntityPlayer player,ItemStack stack) {
		for (ItemStack item : player.inventory.mainInventory)
			if (item != null && item.isItemEqual (stack) && item.getCount () > 0)
				return true;
		return false;
	}

	private void consumeStack (EntityPlayer player,ItemStack stack) {
		int amountLeft = stack.getCount ();
		for (int index = 0; index < player.inventory.mainInventory.size (); index++)
			if (amountLeft <= 0)
				return;
			else if (ItemStack.areItemsEqual (stack,player.inventory.mainInventory.get (index))) {
				if (stack.getCount () <= player.inventory.mainInventory.get (index).getCount ()) {
					player.inventory.mainInventory.get (index).setCount (player.inventory.mainInventory.get (index).getCount () - stack.getCount ());
					if (player.inventory.mainInventory.get (index).getCount () <= 0)
						player.inventory.setInventorySlotContents (index,null);
					break;
				} else if (stack.getCount () > player.inventory.mainInventory.get (index).getCount ()) {
					amountLeft -= player.inventory.mainInventory.get (index).getCount ();
					player.inventory.setInventorySlotContents (index,null);
				}
			}
	}

	private ItemStack getStackForSign (World world,BlockPos pos) {
		if (isInventory (world,pos)) {
			IInventory inv = (IInventory) world.getTileEntity (pos);
			return inv.getStackInSlot (0);
		}
		return null;
	}

	private boolean isInventory (World world,BlockPos pos) {
		return world.getTileEntity (pos) instanceof IInventory;
	}

	private UUID getOwner (World world,BlockPos pos) {
		TileEntitySign tile = (TileEntitySign) world.getTileEntity (pos);
		try {
			return UUID.fromString (tile.getTileData ().getString ("owner"));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private void setOwner (World world,BlockPos pos,UUID uuid) {
		TileEntitySign tile = (TileEntitySign) world.getTileEntity (pos);
		tile.getTileData ().setString ("owner",uuid.toString ());
	}

	private boolean canBuy (World world,BlockPos pos,ItemStack stack) {
		IInventory inv = (IInventory) world.getTileEntity (pos);
		for (int index = 0; index < inv.getSizeInventory (); index++)
			if (inv.getStackInSlot (index) != null && inv.getStackInSlot (index).isItemEqual (stack))
				return true;
		return false;
	}

	private boolean canSell (World world,BlockPos pos,ItemStack stack) {
		IInventory inv = (IInventory) world.getTileEntity (pos);
		for (int index = 0; index < inv.getSizeInventory (); index++)
			return inv.getStackInSlot (index) == null || inv.getStackInSlot (index) != null && inv.getStackInSlot (index).isItemEqual (stack);
		return false;
	}

	private void consumeStack (World world,BlockPos pos,ItemStack stack) {
		IInventory inv = (IInventory) world.getTileEntity (pos);
		int amountLeft = stack.getCount ();
		for (int index = 0; index < inv.getSizeInventory (); index++)
			if (amountLeft <= 0)
				return;
			else if (ItemStack.areItemsEqual (stack,inv.getStackInSlot (index))) {
				if (stack.getCount () <= inv.getStackInSlot (index).getCount ()) {
					inv.getStackInSlot (index).setCount (inv.getStackInSlot (index).getCount () - stack.getCount ());
					if (inv.getStackInSlot (index).getCount () <= 0)
						inv.setInventorySlotContents (index,null);
					break;
				} else if (stack.getCount () > inv.getStackInSlot (index).getCount ()) {
					amountLeft -= inv.getStackInSlot (index).getCount ();
					inv.setInventorySlotContents (index,null);
				}
			}
	}

	private void setChest (World world,BlockPos pos,BlockPos chestPos) {
		TileEntitySign sign = (TileEntitySign) world.getTileEntity (pos);
		sign.getTileData ().setIntArray ("pos",new int[] {chestPos.getX (),chestPos.getY (),chestPos.getZ ()});
	}

	private BlockPos getChest (World world,BlockPos pos) {
		TileEntitySign sign = (TileEntitySign) world.getTileEntity (pos);
		int[] data = sign.getTileData ().getIntArray ("pos");
		return new BlockPos (data[0],data[1],data[2]);
	}

	private void addStack (World world,BlockPos pos,ItemStack stack) {
		IInventory inv = (IInventory) world.getTileEntity (pos);
		for (int index = 0; index < inv.getSizeInventory (); index++)
			if (inv.getStackInSlot (index) != null && inv.getStackInSlot (index).isItemEqual (stack)) {
				inv.getStackInSlot (index).setCount (inv.getStackInSlot (index).getCount () + stack.getCount ());
				break;
			} else if (inv.getStackInSlot (index) == ItemStack.EMPTY) {
				inv.setInventorySlotContents (index,stack);
				break;
			}
	}

	private void handleIBuySign (EntityPlayer player,World world,BlockPos pos) {
		PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
		if (isValid (world,pos)) {
			if (playerData.getMoney () >= getPrice (world,pos)) {
				if (addStack (player,getStack (world,pos))) {
					playerData.setMoney (playerData.getMoney () - getPrice (world,pos));
					DataHelper2.forceSave (Keys.PLAYER_DATA,playerData);
					ChatHelper.sendMessageTo (player,Local.PURCHASE.replaceAll ("@","" + getPrice (world,pos)).replaceAll ("#",getStack (world,pos).getCount () + "x " + getStack (world,pos).getDisplayName ()));
				} else
					ChatHelper.sendMessageTo (player,Local.PLAYER_INVENTORY_FULL);
			} else
				ChatHelper.sendMessageTo (player,Local.MONEY_NONE);
		} else if (getPrice (world,pos) >= 0 && player.getHeldItemMainhand () != null && getStack (world,pos) == null) {
			setShopBuy (world,pos,player.getHeldItemMainhand ());
		} else
			ChatHelper.sendMessageTo (player,Local.SIGN_INVALID);
	}

	private void handleISellSign (EntityPlayer player,World world,BlockPos pos) {
		PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
		if (isValid (world,pos)) {
			if (playerData.getMoney () >= getPrice (world,pos)) {
				if (hasStack (player,getStack (world,pos))) {
					consumeStack (player,getStack (world,pos));
					ChatHelper.sendMessageTo (player,Local.ITEM_SOLD.replaceAll ("#",getStack (world,pos).getCount () + "x " + getStack (world,pos).getDisplayName ()).replaceAll ("@","" + getPrice (world,pos)));
					playerData.setMoney (playerData.getMoney () + getPrice (world,pos));
				} else
					ChatHelper.sendMessageTo (player,Local.ITEM_NONE.replaceAll ("#",getStack (world,pos).getDisplayName ()));
			} else
				ChatHelper.sendMessageTo (player,Local.MONEY_NONE);
		} else if (getPrice (world,pos) >= 0 && player.getHeldItemMainhand () != null && getStack (world,pos) == null) {
			setShopBuy (world,pos,player.getHeldItemMainhand ());
		} else
			ChatHelper.sendMessageTo (player,Local.SIGN_INVALID);
	}

	private void handleBuySign (EntityPlayer player,World world,BlockPos pos) {
		PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,player.getGameProfile ().getId ().toString ());
		if (isValid (world,pos) && hasValidLink (world,pos)) {
			if (canBuy (world,getChest (world,pos),getStack (world,pos))) {
				if (playerData.getMoney () >= getPrice (world,pos)) {
					if (addStack (player,getStack (world,pos))) {
						consumeStack (world,getChest (world,pos),getStack (world,pos));
						playerData.setMoney (playerData.getMoney () - getPrice (world,pos));
						PlayerData ownerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,getOwner (world,pos).toString ());
						ownerData.setMoney (ownerData.getMoney () + getPrice (world,pos));
						DataHelper2.forceSave (Keys.PLAYER_DATA,ownerData);
						DataHelper2.forceSave (Keys.PLAYER_DATA,playerData);
						ChatHelper.sendMessageTo (player,Local.PURCHASE.replaceAll ("@","" + getPrice (world,pos)).replaceAll ("#",getStack (world,pos).getCount () + "x " + getStack (world,pos).getDisplayName ()));
					} else
						ChatHelper.sendMessageTo (player,Local.PLAYER_INVENTORY_FULL);
				} else
					ChatHelper.sendMessageTo (player,Local.MONEY_NONE);
			} else
				ChatHelper.sendMessageTo (player,Local.SIGN_INVALID);
		} else if (getPrice (world,pos) >= 0 && player.getHeldItemMainhand () != null && player.getHeldItemMainhand ().isItemEqual (linkStack)) {
			shops.put (player.getGameProfile ().getId (),pos);
			ChatHelper.sendMessageTo (player,Local.LINK_CHEST);
		} else
			ChatHelper.sendMessageTo (player,Local.SIGN_INVALID);
	}

	private void handleSellSign (EntityPlayer player,World world,BlockPos pos) {
		if (isValid (world,pos) && hasValidLink (world,pos) && canSell (world,getChest (world,pos),getStack (world,pos))) {
			if (hasStack (player,getStack (world,pos))) {
				consumeStack (player,getStack (world,pos));
				addStack (world,getChest (world,pos),getStack (world,pos));
				PlayerData playerData = (PlayerData) DataHelper2.get (Keys.PLAYER_DATA,getOwner (world,pos).toString ());
				playerData.setMoney (playerData.getMoney () + getPrice (world,pos));
			} else
				ChatHelper.sendMessageTo (player,Local.ITEM_NONE.replaceAll ("#",getStack (world,pos).getDisplayName ()));
		} else if (getPrice (world,pos) >= 0 && player.getHeldItemMainhand () != null && player.getHeldItemMainhand ().isItemEqual (linkStack)) {
			shops.put (player.getGameProfile ().getId (),pos);
			ChatHelper.sendMessageTo (player,Local.LINK_CHEST);
		} else
			ChatHelper.sendMessageTo (player,Local.SIGN_INVALID);
	}

	private void handleLinking (EntityPlayer player,World world,BlockPos pos) {
		if (shops.containsKey (player.getGameProfile ().getId ())) {
			if (getStackForSign (world,pos) != null) {
				setShopBuy (world,shops.get (player.getGameProfile ().getId ()),getStackForSign (world,pos));
				setOwner (world,shops.get (player.getGameProfile ().getId ()),player.getGameProfile ().getId ());
				setChest (world,shops.get (player.getGameProfile ().getId ()),pos);
				shops.remove (player.getGameProfile ().getId ());
				ChatHelper.sendMessageTo (player,Local.LINKED);
			} else
				ChatHelper.sendMessageTo (player,Local.ITEM_NONE);
		} else if (player.getHeldItemMainhand () != null && player.getHeldItemMainhand ().isItemEqual (linkStack))
			ChatHelper.sendMessageTo (player,Local.SIGN_FIRST);
	}
}
