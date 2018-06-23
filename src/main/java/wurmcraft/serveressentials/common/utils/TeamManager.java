package wurmcraft.serveressentials.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.reference.Keys;

public class TeamManager {

  private static ArrayList<Team> teams = new ArrayList<>();
  private static HashMap<UUID, Team> ownerTeam = new HashMap<>();

  public static boolean register(Team team) {
    if (!teams.contains(team)) {
      for (Team name : teams) {
        if (name.getName().equalsIgnoreCase(team.getName())) {
          return false;
        }
      }
      teams.add(team);
      ownerTeam.put(team.getLeader(), team);
      DataHelper2.createIfNonExist(Keys.TEAM, team);
      return true;
    }
    return false;
  }

  public static Team getTeamFromName(String name) {
    for (Team team : teams) {
      if (team.getName().equalsIgnoreCase(name)) {
        return team;
      }
    }
    return null;
  }

  public static Team getTeamFromLeader(UUID name) {
    for (UUID leaders : ownerTeam.keySet()) {
      if (name.equals(leaders)) {
        return ownerTeam.get(leaders);
      }
    }
    return null;
  }

  public static List<Team> getTeams() {
    return Collections.unmodifiableList(teams);
  }

  public static void removeTeam(Team team) {
    teams.remove(team);
    ownerTeam.remove(team.getLeader());
    DataHelper2.delete(Keys.TEAM, team);
  }
}
