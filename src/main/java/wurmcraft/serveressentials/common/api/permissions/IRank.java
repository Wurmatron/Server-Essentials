package wurmcraft.serveressentials.common.api.permissions;

import wurmcraft.serveressentials.common.reference.Perm;

/**
 Interface used for creation of ranks
 */
public interface IRank {

	/**
	 Name of this rank
	 (Used for storage)
	 */
	String getName ();

	/**
	 Is default rank?
	 more than 1 result in the one with the lower name when sorted alphabetical
	 */
	boolean isDefault ();

	/**
	 Displayed before the players name
	 */
	String getPrefix ();

	/**
	 Displayed after the players name
	 */
	String getSuffix ();

	/**
	 Used for power and permissions
	 takes everything from its inheritanced groups
	 */
	String[] getInheritance ();

	/**
	 Permissions / Commands this group is sable to run
	 */
	String[] getPermissions ();

	/**Checks for whether or not the given player has the supplied permission.
	 *
	 * @return <code>true</code> if player has permission, <code>false</code> if not.
	 */
	default boolean hasPermission(String perm) {
		if (Perm.isValidPermission(perm)) {
			for (String permission : getPermissions()) if (perm.equalsIgnoreCase(permission)) return true;
		}
		return false;
	}
}