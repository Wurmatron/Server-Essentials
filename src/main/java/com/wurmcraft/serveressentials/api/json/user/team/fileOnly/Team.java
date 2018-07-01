package wurmcraft.serveressentials.common.api.team;

import com.wurmcraft.serveressentials.api.json.user.IDataType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.util.text.TextFormatting;

public class Team implements IDataType {

  private String teamName;
  private UUID leader;
  private boolean publi;
  private HashMap<UUID, String> members = new HashMap<>();
  private ArrayList<UUID> requetedPlayers = new ArrayList<>();
  private TextFormatting teamColor;

  public Team() {
    this.teamName = "";
  }

  public Team(String name, UUID owner, boolean publi) {
    this.teamName = name;
    this.leader = owner;
    this.publi = publi;
    teamColor = TextFormatting.GRAY;
  }

  public String getName() {
    return teamName;
  }

  public HashMap<UUID, String> getMembers() {
    return members;
  }

  public UUID getLeader() {
    return leader;
  }

  public boolean isPublic() {
    return publi;
  }

  public ArrayList<UUID> requestedPlayers() {
    return requetedPlayers;
  }

  public boolean canJoin(UUID name) {
    for (UUID player : requetedPlayers) {
      if (player.equals(name)) {
        return true;
      }
    }
    return isPublic();
  }

  public void addMember(UUID name) {
    if (!members.keySet().contains(name)) {
      members.put(name, "default");
      requetedPlayers.remove(name);
    }
  }

  public void removeMember(UUID name) {
    for (UUID mem : members.keySet()) {
      if (mem.equals(name)) {
        members.remove(mem);
      }
    }
  }

  public void addPossibleMember(UUID name) {
    if (!requetedPlayers.contains(name)) {
      requetedPlayers.add(name);
    }
  }

  public TextFormatting getTeamColor() {
    return teamColor;
  }

  public void setPublic(boolean value) {
    this.publi = value;
  }

  public void setColor(TextFormatting value) {
    this.teamColor = value;
  }

  public String getID() {
    return teamName;
  }
}
