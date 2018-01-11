package wurmcraft.serveressentials.common.commands.item;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Kit;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

import javax.annotation.Nullable;
import java.util.List;

public class KitCommand extends SECommand {

	public KitCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "kit";
	}

	@Override
	public String[] getCommandAliases () {
		return new String[] {"k"};
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/kit <name>";
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Gives a certain kit of items";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		//		super.execute (server,sender,args);
		//		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		//		if (args.length == 1) {
		//			if (args[0].equalsIgnoreCase ("list")) {
		//				List <String> kitNames = new ArrayList <> ();
		//				for (Kit kit : DataHelper.loadedKits)
		//					kitNames.add (kit.getName ());
		//				ChatHelper.sendMessageTo (player,TextFormatting.DARK_AQUA + "Kit: " + TextFormatting.AQUA + Strings.join (kitNames," "));
		//			} else {
		//				if (DataHelper.loadedKits.size () > 0) {
		//					boolean found = false;
		//					for (Kit kit : DataHelper.loadedKits)
		//						if (kit != null && kit.getName ().equalsIgnoreCase (args[0]) && hasPerm (player,kit)) {
		//							found = true;
		//							for (ItemStack stack : kit.getItems ())
		//								addStack (player,stack);
		//						}
		//					if (!found)
		//						ChatHelper.sendMessageTo (player,TextFormatting.RED + Local.KIT_NOTFOUND.replaceAll ("#",args[0]));
		//					else
		//						ChatHelper.sendMessageTo (player,Local.KIT.replaceAll ("#",args[0]));
		//				} else
		//					ChatHelper.sendMessageTo (player,Local.NO_KITS);
		//			}
		//		} else
		//			ChatHelper.sendMessageTo (player,getUsage (sender));
	}

	private boolean addStack (EntityPlayer player,ItemStack stack) {
		if (stack.getItem () instanceof ItemArmor) {
			ItemArmor armor = (ItemArmor) stack.getItem ();
			if (armor.armorType.equals (EntityEquipmentSlot.HEAD) && player.inventory.getStackInSlot (100) == null) {
				player.inventory.armorInventory.set (3,stack);
				return true;
			} else if (armor.armorType.equals (EntityEquipmentSlot.CHEST) && player.inventory.getStackInSlot (101) == null) {
				player.inventory.armorInventory.set (2,stack);
				return true;
			} else if (armor.armorType.equals (EntityEquipmentSlot.LEGS) && player.inventory.getStackInSlot (102) == null) {
				player.inventory.armorInventory.set (1,stack);
				return true;
			} else if (armor.armorType.equals (EntityEquipmentSlot.FEET) && player.inventory.getStackInSlot (103) == null) {
				player.inventory.armorInventory.set (0,stack);
				return true;
			}
		} else {
			for (int index = 0; index < 36; index++)
				if (player.inventory.getStackInSlot (index) == null) {
					player.inventory.setInventorySlotContents (index,stack);
					return true;
				}
			EntityItem entityItem = new EntityItem (player.world,player.posX,player.posY,player.posZ,stack);
			player.world.spawnEntity (entityItem);
			ChatHelper.sendMessageTo (player,Local.FULL_INV);
			return true;
		}
		return false;
	}

	private boolean hasPerm (EntityPlayer player,Kit kit) {
		//		PlayerData data = DataHelper.getPlayerData (player.getGameProfile ().getId ());
		//		IRank rank = data.getRank ();
		//		if (rank.getPermissions ().length > 0)
		//			for (String perm : rank.getPermissions ())
		//				if (perm != null)
		//					if (perm.equalsIgnoreCase ("kit." + kit.getName ())) {
		//						return true;
		//					} else if (perm.startsWith ("*")) {
		//						return true;
		//					} else if (perm.endsWith ("*") && ("kit." + kit.getName ()).startsWith (perm.substring (0,perm.indexOf ("*"))))
		//						return true;
		//		if (rank.getInheritance () != null && rank.getInheritance ().length > 0) {
		//			for (String preRank : rank.getInheritance ())
		//				if (RankManager.getRankFromName (preRank) != null) {
		//					IRank tempRank = RankManager.getRankFromName (preRank);
		//					if (tempRank.getPermissions ().length > 0)
		//						for (String perm : tempRank.getPermissions ())
		//							if (perm != null)
		//								if (perm.equalsIgnoreCase ("kit." + kit.getName ())) {
		//									return true;
		//								} else if (perm.startsWith ("*")) {
		//									return true;
		//								} else if (perm.endsWith ("*") && ("kit." + kit.getName ()).startsWith (perm.substring (0,perm.indexOf ("*"))))
		//									return true;
		//				}
		//		}
		//		return false;
		return false;
	}

	@Override
	public List <String> getTabCompletions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		return autoComplete (args,DataHelper2.getData (Keys.KIT),0);
	}
}
