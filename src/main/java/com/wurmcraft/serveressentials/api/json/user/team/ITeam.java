package com.wurmcraft.serveressentials.api.json.user.team;

import java.util.UUID;

public interface ITeam {

  UUID getLeader();

  String getName();
}
