package wurmcraft.serveressentials.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;

import java.util.*;

/**
 Created by matthew on 6/24/17.
 */
public class UsernameResolver {

	public static final class AbstractUsernameCollection <T extends String> extends AbstractCollection <T> {

		List <T> names;

		{
			names = new LinkedList <> ();
		}

		public AbstractUsernameCollection (Collection <T> values) {
			for (T value : values)
				add (value);
		}

		@Override
		public boolean contains (Object s) {
			Iterator <T> itr = this.iterator ();
			if (s == null)
				return false;
			else
				while (itr.hasNext ())
					if (((String) s).equalsIgnoreCase (itr.next ()))
						return true;
			return false;
		}

		@Override
		public Iterator <T> iterator () {
			return names.iterator ();
		}

		@Override
		public boolean add (T s) {
			if (!contains (s)) {
				names.add (s);
				return true;
			} else
				return false;
		}

		@Override
		public int size () {
			return names.size ();
		}
	}

	private static MinecraftServer getServer () {
		return FMLCommonHandler.instance ().getMinecraftServerInstance ();
	}

	public static boolean isValidPlayer (String username) {
		return new AbstractUsernameCollection <String> (UsernameCache.getMap ().values ()).contains (username) || isValidNickname (username);
	}

	public static boolean printIsValidPlayer (EntityPlayer sender,String username) {
		String rUsername = usernameFromNickname (username);
		if (rUsername == null) {
			ChatHelper.sendMessageTo (sender,Local.PLAYER_NOT_FOUND.replaceAll (((username == null) ? "\"#\" " : "\"#\""),((username == null) ? "" : username)));
			return false;
		}
		return true;
	}

	public static boolean isValidNickname (String username) {
		String resolvedName = usernameFromNickname (username);
		return (resolvedName != username) && (resolvedName != null);
	}

	public static boolean isNickname (String username) {
		return TextFormatting.getTextWithoutFormattingCodes (username).startsWith ("*");
	}

	public static String usernameFromNickname (String username) {
		String unFormattedName = TextFormatting.getTextWithoutFormattingCodes (username).replaceAll ("\\*","");
		if (new AbstractUsernameCollection (UsernameCache.getMap ().values ()).contains (unFormattedName))
			return unFormattedName;
		else {
			for (UUID uid : UsernameCache.getMap ().keySet ()) {
				String nick = ((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,uid.toString ())).getNickname ();
				if (nick != null && nick.equalsIgnoreCase (unFormattedName))
					return getUsername (uid);
			}
			return null;
		}
	}

	public static String getNickname (UUID uuid) {
		return ((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,uuid.toString ())).getNickname ();
	}

	public static PlayerData getPlayerData (UUID uniqueID) {
		return ((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,uniqueID.toString ()));
	}

	public static PlayerData getPlayerData (String username) {
		String nick = usernameFromNickname (username);
		if (!nick.equalsIgnoreCase (username))
			username = usernameFromNickname (username);
		Set <UUID> uuids = UsernameCache.getMap ().keySet ();
		for (UUID uuid : uuids)
			if (UsernameCache.getMap ().get (uuid).equalsIgnoreCase (username))
				return ((PlayerData) DataHelper2.get (Keys.PLAYER_DATA,uuid.toString ()));
		return null;
	}

	public static UUID getPlayerUUID (String username) {
		String nick = usernameFromNickname (username);
		if (!nick.equalsIgnoreCase (username))
			username = usernameFromNickname (username);
		Set <UUID> uuids = UsernameCache.getMap ().keySet ();
		for (UUID uuid : uuids)
			if (UsernameCache.getMap ().get (uuid).equalsIgnoreCase (username))
				return uuid;
		return null;
	}

	public static String getUsername (UUID uniqueID) {
		return UsernameCache.getMap ().get (uniqueID);
	}

	public static EntityPlayer getPlayer (String username) {
		String nick = usernameFromNickname (username);
		if (isValidPlayer (username))
			return getServer ().getPlayerList ().getPlayerByUsername ((nick == null) ? username : nick);
		else
			return null;
	}

	public static EntityPlayer getPlayer (UUID uuid) {
		return getServer ().getPlayerList ().getPlayerByUUID (uuid);
	}
}
