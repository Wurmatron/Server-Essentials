package wurmcraft.serveressentials.common.utils;

import wurmcraft.serveressentials.common.api.storage.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamManager {

		private static ArrayList<Team> teams = new ArrayList<>();

		public static boolean register(Team team) {
				if (!teams.contains(team)) {
						for (Team name : teams)
								if (name.getName().equalsIgnoreCase(team.getName()))
										return false;
						teams.add(team);
						DataHelper.createTeam(team,false);
						return true;
				} return false;
		}

		public static Team getTeamFromName(String name) {
				for (Team team : teams)
						if (team.getName().equalsIgnoreCase(name)) return team; return null;
		}

		public static List<Team> getTeams() {
				return Collections.unmodifiableList(teams);
		}
}
