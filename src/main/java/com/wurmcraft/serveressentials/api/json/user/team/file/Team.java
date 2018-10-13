package com.wurmcraft.serveressentials.api.json.user.team.file;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.util.text.TextFormatting;

public class Team implements DataType, ITeam {

  private String teamName;
  private UUID leader;
  private boolean publi;
  private String[] members;
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

  @Override
  public String[] getMembers() {
    return members;
  }

  @Override
  public UUID getLeader() {
    return leader;
  }

  public boolean isPublic() {
    return publi;
  }

  public void setPublic(boolean value) {
    this.publi = value;
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

  public void addPossibleMember(UUID name) {
    if (!requetedPlayers.contains(name)) {
      requetedPlayers.add(name);
    }
  }

  public TextFormatting getTeamColor() {
    return teamColor;
  }

  public void setColor(TextFormatting value) {
    this.teamColor = value;
  }

  public String getID() {
    return teamName;
  }
}
