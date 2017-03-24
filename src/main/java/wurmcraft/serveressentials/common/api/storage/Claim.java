package wurmcraft.serveressentials.common.api.storage;

import wurmcraft.serveressentials.common.api.team.Team;

import java.util.UUID;

public class Claim {

		private String team;
		private UUID   owner;

		public Claim(Team team, UUID owner) {
				this.team = team.getName();
				this.owner = owner;
		}

		public String getTeam() {
				return team;
		}

		public UUID getOwner() {
				return owner;
		}
}
