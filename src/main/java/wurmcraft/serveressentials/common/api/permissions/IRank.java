package wurmcraft.serveressentials.common.api.permissions;

/**
	* Interface used for creation of ranks
	*/
public interface IRank {

		/**
			* Name of this rank
			* (Used for storage)
			*/
		String getName();

		/**
			* Is default rank?
			* more than 1 result in the one with the lower name when sorted alphabetical
			*/
		boolean isDefault();

		/**
			* Displayed before the players name
			*/
		String getPrefix();

		/**
			* Displayed after the players name
			*/
		String getSuffix();

		/**
			* Used for power and permissions
			* takes everything from its inheritanced groups
			*/
		String[] getInheritance();

		/**
			* Permissions / Commands this group is sable to run
			*/
		String[] getPermissions();
}