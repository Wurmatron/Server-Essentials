package wurmcraft.serveressentials.common.api.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 @see ITeam */
public class Team implements ITeam {

	private String teamName;
	private UUID leader;
	private boolean publi;
	private HashMap <UUID, String> members = new HashMap <> ();
	private ArrayList <UUID> requetedPlayers = new ArrayList <> ();

	public Team (String name,UUID owner,boolean publi) {
		this.teamName = name;
		this.leader = owner;
		this.publi = publi;
	}

	@Override
	public String getName () {
		return teamName;
	}

	@Override
	public HashMap <UUID, String> getMembers () {
		return members;
	}

	@Override
	public UUID getLeader () {
		return leader;
	}

	@Override
	public boolean isPublic () {
		return publi;
	}

	@Override
	public ArrayList <UUID> requestedPlayers () {
		return requetedPlayers;
	}

	@Override
	public boolean canJoin (UUID name) {
		for (UUID player : requetedPlayers)
			if (player.equals (name))
				return true;
		return isPublic ();
	}

	@Override
	public void addMember (UUID name) {
		if (!members.keySet ().contains (name)) {
			members.put (name,"default");
			requetedPlayers.remove (name);
		}
	}

	@Override
	public void removeMember (UUID name) {
		for (UUID mem : members.keySet ())
			if (mem.equals (name))
				members.remove (mem);
	}

	@Override
	public void addPossibleMember (UUID name) {
		if (!requetedPlayers.contains (name))
			requetedPlayers.add (name);
	}
}
