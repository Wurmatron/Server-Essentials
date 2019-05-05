package com.wurmcraft.serveressentials.api.user.event;

import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UserSyncEvent extends Event {

  public GlobalRestUser localServerUser;
  public GlobalRestUser restUser;
  public Type type;

  public UserSyncEvent(GlobalRestUser localServerUser, GlobalRestUser restUser, Type type) {
    this.localServerUser = localServerUser;
    this.restUser = restUser;
    this.type = type;
  }

  public enum Type {
    LOGIN,
    LOGOUT,
    STANDARD;
  }
}
