package wurmcraft.serveressentials.common.utils;

import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.config.ConfigHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankManager {

	private static ArrayList <IRank> loadedRanks = new ArrayList <> ();

	public static List <IRank> getRanks () {
		return Collections.unmodifiableList (loadedRanks);
	}

	public static IRank getDefaultRank () {
		for (IRank group : loadedRanks)
			if (group.isDefault ())
				return group;
		if (loadedRanks.size () > 0) {
			LogHandler.error ("No Default Rank Found! Using " + loadedRanks.get (0).getName () + " as default group!");
			return loadedRanks.get (0);
		}
		ConfigHandler.createDefaultRank ();
		LogHandler.error ("Unable to load / find any groups!");
		return null;
	}

	public static boolean registerRank (IRank group) {
		if (group != null && group.getName () != null && getRankFromName (group.getName ()) == null) {
			if (group.getInheritance () != null && group.getInheritance ().length > 0) {
				for (String g : group.getInheritance ())
					if (g == null || g.equalsIgnoreCase (""))
						return false;
				loadedRanks.add (group);
				return true;
			} else if (group.getInheritance () == null) {
				loadedRanks.add (group);
				return true;
			}
		}
		return false;
	}

	public static IRank getRankFromName (String name) {
		if (name != null && name.length () > 0)
			return loadedRanks.stream ().filter (group -> group.getName ().equalsIgnoreCase (name)).findFirst ().orElse (null);
		return null;
	}

	public static void removeRank (IRank group) {
		loadedRanks.remove (group);
	}

	public static void removeRank (String group) {
		for (IRank g : loadedRanks)
			if (g.getName ().equalsIgnoreCase (group))
				removeRank (g);
	}

	public static void clearAllRanks () {
		loadedRanks.clear ();
		ConfigHandler.loadRanks ();
	}

	public static boolean isMore (IRank higherRank,IRank lowerRank) {
		if (higherRank.isDefault () || higherRank.getInheritance () == null)
			return false;
		IRank[] lower = getAllLower (higherRank,null);
		if (lower != null && lower.length > 0)
			for (IRank rank : lower)
				if (rank.getName ().equalsIgnoreCase (lowerRank.getName ()))
					return true;
		return false;
	}

	public static IRank[] getAllLower (IRank rank,List <IRank> ranks) {
		if (rank.isDefault () || rank.getInheritance () == null)
			return null;
		if (ranks == null)
			ranks = new ArrayList <> ();
		for (String r : rank.getInheritance ())
			if (RankManager.getRankFromName (r) != null) {
				IRank sub = RankManager.getRankFromName (r);
				ranks.add (sub);
				if (!sub.isDefault () || sub.getInheritance () != null)
					getAllLower (rank,ranks);
			}
		if (ranks.size () > 0)
			return ranks.toArray (new IRank[0]);
		return null;
	}

	public static boolean hasPermission (IRank rank,String perm) {
		if (rank.hasPermission (perm))
			return true;
		else if (rank.getInheritance () != null && rank.getInheritance ().length > 0) {
			for (String inheritance : rank.getInheritance ())
				if (getRankFromName (inheritance) != null)
					return getRankFromName (inheritance).hasPermission (perm);
		}
		return false;
	}
}
