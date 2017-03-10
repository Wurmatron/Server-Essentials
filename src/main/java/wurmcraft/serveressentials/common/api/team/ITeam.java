package wurmcraft.serveressentials.common.api.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface ITeam {

		String getName();

		HashMap<UUID, String> getMembers();

		UUID getLeader();

		boolean isPublic();

		ArrayList<UUID> requestedPlayers();

		boolean canJoin(UUID name);

		void addMember(UUID name);

		void removeMember(UUID name);

		void addPossibleMember(UUID name);
}
