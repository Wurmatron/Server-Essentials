package com.wurmcraft.serveressentials.api.user.event;

import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UserSyncEvent extends Event {

  public GlobalRestUser localServerUser;
  public GlobalRestUser restUser;

  public UserSyncEvent(GlobalRestUser localServerUser, GlobalRestUser restUser) {
    this.localServerUser = localServerUser;
    this.restUser = restUser;
  }
}
