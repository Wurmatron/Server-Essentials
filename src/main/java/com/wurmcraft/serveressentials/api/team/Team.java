package com.wurmcraft.serveressentials.api.team;

import java.util.UUID;

public interface Team {

  UUID getLeader();

  String getName();

  String[] getMembers();
}
