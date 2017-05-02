package wurmcraft.serveressentials.common.api.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 interface used for creation of a team
 */
public interface ITeam {

	/**
	 Name of the team
	 */
	String getName ();

	/**
	 Members within the team

	 @return uuid is playerName, String is the players rank within the team
	 */
	HashMap <UUID, String> getMembers ();

	/**
	 Leader of this group
	 */
	UUID getLeader ();

	/**
	 Can anyone join
	 */
	boolean isPublic ();

	/**
	 Players that have been requested to join the team
	 */
	ArrayList <UUID> requestedPlayers ();

	/**
	 Checks if the player is able to join the team
	 */
	boolean canJoin (UUID name);

	/**
	 Adds a member with the default group to the team
	 */
	void addMember (UUID name);

	/**
	 Removes a member from the team
	 */
	void removeMember (UUID name);

	/**
	 Adds a player to the requested list
	 */
	void addPossibleMember (UUID name);
}
