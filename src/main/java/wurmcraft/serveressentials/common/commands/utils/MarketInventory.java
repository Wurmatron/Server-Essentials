package wurmcraft.serveressentials.common.commands.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.storage.ShopData;
import wurmcraft.serveressentials.common.api.storage.Vault;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.eco.MarketCommand;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.event.MarketEvent;
import wurmcraft.serveressentials.common.event.PlayerTickEvent;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;

import java.util.UUID;

public class MarketInventory extends InventoryBasic {

	private EntityPlayerMP viewer;
	private UUID marketOwner;
	private int startingIndex;
	private ShopData shopData;

	public MarketInventory (EntityPlayerMP viewer,UUID marketOwner,int startingIndex,ShopData data) {
		super ("",false,54);
		if (UsernameCache.getLastKnownUsername (marketOwner) != null)
			setCustomName (UsernameCache.getLastKnownUsername (marketOwner) + "'s Shop");
		this.viewer = viewer;
		this.marketOwner = marketOwner;
		this.startingIndex = startingIndex;
		this.shopData = data;
		PlayerTickEvent.addMarket (viewer,this);
		MarketEvent.disableDrops.add (viewer.getGameProfile ().getId ());
	}

	@Override
	public void openInventory (EntityPlayer player) {
		if (shopData != null && startingIndex >= 0) {
			int shopAmount;
			if (shopData.getAmount ().length >= MarketCommand.MAX_PER_PAGE)
				shopAmount = MarketCommand.MAX_PER_PAGE;
			else
				shopAmount = shopData.getAmount ().length;
			ItemStack[] shopItems = shopData.getMarketItems ();
			for (int index = startingIndex; index < startingIndex + shopAmount; index++)
				setInventorySlotContents (9 + index,addShopData (shopItems[index]));
			addShopButtons ();
		}
	}

	@Override
	public void closeInventory (EntityPlayer player) {
		PlayerTickEvent.removeMarket (player);
		MarketEvent.disableDrops.remove (viewer.getGameProfile ().getId ());
	}

	private ItemStack addShopData (ItemStack stack) {
		try {
			NBTTagCompound nbt = JsonToNBT.getTagFromJson ("{display:{Name:\"name here\",color:,Lore:[\"lore here\", \"lore here\"]}}");
			stack.setTagCompound (nbt);
			LogHandler.info ("NBT: " + stack.getTagCompound ().toString ());
			return stack;
		} catch (NBTException e) {
			e.printStackTrace ();
		}
		return stack;
	}

	private void addShopButtons () {
		if (startingIndex > shopData.getAmount ().length)
			setInventorySlotContents (0,new ItemStack (Blocks.STAINED_GLASS_PANE,1,14).setStackDisplayName ("Back"));
		if (shopData.getAmount ().length > MarketCommand.MAX_PER_PAGE)
			setInventorySlotContents (8,new ItemStack (Blocks.STAINED_GLASS_PANE,1,13).setStackDisplayName ("Next"));
		setInventorySlotContents (4,new ItemStack (Items.BOOK).setStackDisplayName ("Market"));
	}

	public void handleUpdate () {
		if (getStackInSlot (4) == null) {
			//			viewer.displayGUIChest ();
		}
		int shopAmount;
		if (shopData != null && shopData.getAmount ().length > 0 && shopData.getAmount ().length >= MarketCommand.MAX_PER_PAGE)
			shopAmount = MarketCommand.MAX_PER_PAGE;
		else
			shopAmount = shopData.getAmount ().length;
		for (int index = startingIndex; index < startingIndex + shopAmount; index++) {
			if (getStackInSlot (9 + index) == null && shopData.getMarketItems ()[index] != null)
				handleShopClick (index);
			if (getStackInSlot (9 + index) == null && shopData.getMarketItems ()[index] != null || getStackInSlot (9 + index).getCount() != shopData.getMarketItems ()[index].getCount()) {
				viewer.connection.sendPacket (new SPacketSetSlot (-1,-1,null));
				//				for (ItemStack stack : viewer.inventory.mainInventory)
				//					if (stack != null)
				//						if (stack.isItemEqual (shopData.getMarketItems ()[index]))
				//							viewer.inventory.deleteStack (stack);
			}
			setInventorySlotContents (9 + index,shopData.getMarketItems ()[index]);
		}
	}

	@Override
	public boolean isItemValidForSlot (int index,ItemStack stack) {
		return false;
	}

	private void handleShopClick (int index) {
		ItemStack shopStack = shopData.getMarketItems ()[index];
		PlayerData data = DataHelper.getPlayerData (viewer.getGameProfile ().getId ());
		if (shopData.getBuy ()[index] && data.getMoney () >= shopData.getAmount ()[index]) {
			viewer.inventory.addItemStackToInventory (shopStack);
			DataHelper.setMoney (viewer.getGameProfile ().getId (),data.getMoney () - shopData.getAmount ()[index]);
			ChatHelper.sendMessageTo (viewer,Local.PURCHASE.replaceAll ("#",shopData.getMarketItems ()[index].getCount() + "x " + shopStack.getDisplayName ()).replaceAll ("@",Settings.currencySymbol + shopData.getAmount ()[index] + ""));
			addOutput (shopData.getMarketItems ()[index]);
		} else if (!shopData.getBuy ()[index]) {
			if (hasStack (shopData.getMarketItems ()[index])) {
				deleteStack (shopData.getMarketItems ()[index]);
				data.setMoney (data.getMoney () + shopData.getAmount ()[index]);
				ChatHelper.sendMessageTo (viewer,Local.SELL_STACK.replaceAll ("#",shopData.getMarketItems ()[index].getCount() + "x " + shopData.getMarketItems ()[index].getDisplayName ()).replaceAll ("@",UsernameCache.getLastKnownUsername (marketOwner)).replaceAll ("&",Settings.currencySymbol + shopData.getAmount ()[index]));
			} else
				ChatHelper.sendMessageTo (viewer,Local.MISSING_STACK.replaceAll ("#",shopData.getMarketItems ()[index].getDisplayName ()));
		}
	}

	private boolean addOutput (ItemStack stack) {
		boolean added = false;
		Vault[] vaults = DataHelper.playerVaults.get (viewer.getGameProfile ().getId ());
		if (vaults != null && vaults.length > 0) {
			Vault vault = vaults[0];
			for (int id = 0; id < vault.getItems ().length; id++) {
				if (!added) {
					ItemStack item = vault.getItems ()[id];
					if (item == null) {
						added = true;
						ItemStack[] vaultItems = vault.getItems ();
						vaultItems[id] = stack;
						vault.setItems (vaultItems);
						DataHelper.saveVault (viewer.getGameProfile ().getId (),vault);
						DataHelper.loadVault (viewer.getGameProfile ().getId ());
					}
				}
			}
		}
		return added;
	}

	private boolean hasStack (ItemStack stack) {
		for (ItemStack item : viewer.inventory.mainInventory)
			if (stack.isItemEqual (item) && stack.getTagCompound () == null && item.getTagCompound () == null || stack.isItemEqual (item) && stack.getTagCompound () != null && item.getTagCompound () != null && stack.getTagCompound ().equals (item.getTagCompound ()))
				return true;
		return false;
	}

	private void deleteStack (ItemStack stack) {
//		if (stack != null) {
//			int amountNeeeded = stack.getCount();
//			for (ItemStack item : viewer.inventory.mainInventory) {
//				if (item != null) {
//					if (stack.isItemEqual (item) && stack.getItemDamage () == item.getItemDamage ()) {
//						if (stack.getTagCompound () == null && item.getTagCompound () == null || stack.getTagCompound () != null && item.getTagCompound () != null && stack.getTagCompound ().equals (item.getTagCompound ())) {
//							if (item.getCount() >= stack.getCount()) {
//								item.getCount() -= stack.getCount();
//								if (item.getCount() <= 0)
//									viewer.inventory.deleteStack (item);
//								return;
//							} else if (item.getCount() < stack.getCount()) {
//								if (amountNeeeded >= item.getCount()) {
//									amountNeeeded -= item.getCount();
//									item.getCount() = 0;
//									viewer.inventory.deleteStack (item);
//								} else if (amountNeeeded < item.getCount()) {
//									item.getCount() -= amountNeeeded;
//									if (item.getCount() <= 0)
//										viewer.inventory.deleteStack (item);
//								}
//								if (amountNeeeded <= 0)
//									return;
//							}
//						}
//					}
//				}
//			}
//		}
	}

}
