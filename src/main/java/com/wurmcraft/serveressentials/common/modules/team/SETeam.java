package com.wurmcraft.serveressentials.common.modules.team;

import com.wurmcraft.serveressentials.api.team.Team;
import java.util.UUID;

// TODO Implement
public class SETeam implements Team {

  public SETeam() {}

  @Override
  public UUID getLeader() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String[] getMembers() {
    return new String[0];
  }
}
