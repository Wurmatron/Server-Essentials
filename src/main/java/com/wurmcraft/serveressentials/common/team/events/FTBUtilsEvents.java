package com.wurmcraft.serveressentials.common.team.events;

import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.LocalTeam;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.team.TeamModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FTBUtilsEvents {

  private static String[] convertToString(List<ForgePlayer> players) {
    List<String> members = new ArrayList<>();
    for (ForgePlayer player : players) {
      members.add(player.getId().toString());
    }
    return members.toArray(new String[0]);
  }

  private static void updatePlayer(ForgePlayer player) {
    GlobalUser user = (GlobalUser) UserManager.getPlayerData(player.getId())[0];
    user.setTeam(player.team.toString());
    RequestHelper.UserResponses.overridePlayerData(user);
    UserManager.playerData.put(
        player.getId(),
        new Object[]{user, (LocalUser) UserManager.getPlayerData(player.getId())[1]});
  }

  @SubscribeEvent
  public void onTeamCreated(ForgeTeamCreatedEvent e) {
    GlobalTeam global =
        new GlobalTeam(
            e.getTeam().toString(),
            new Bank(),
            new String[0],
            e.getTeam().owner.getId(),
            convertToString(e.getTeam().getMembers()));
    LocalTeam local = new LocalTeam(e.getTeam().toString());
    DataHelper.forceSave(Keys.LOCAL_TEAM, local);
    DataHelper.load(Keys.LOCAL_TEAM, local);
    RequestHelper.TeamResponses.addTeam(global);
    TeamModule.loadRestTeam(e.getTeam().toString());
  }

  @SubscribeEvent
  public void onMemberJoin(ForgeTeamPlayerJoinedEvent e) {
    GlobalTeam team = null;
    if (UserManager.getTeam(e.getTeam().toString()) == null) {
      team = RequestHelper.TeamResponses.getTeam(e.getTeam().toString());
      if (team != null) {
        UserManager.teamCache.put(
            e.getTeam().toString(),
            new Object[]{
                team, DataHelper.load(Keys.LOCAL_TEAM, new LocalTeam(e.getTeam().toString()))
            });
      }
    }
    team = (GlobalTeam) UserManager.getTeam(e.getTeam().toString())[0];
    team.addMember(e.getPlayer().getId().toString());
    RequestHelper.TeamResponses.overrideTeam(team);
    updatePlayer(e.getTeam().owner);
  }

  @SubscribeEvent
  public void onMemberLeave(ForgeTeamPlayerLeftEvent e) {
    GlobalTeam team = null;
    if (UserManager.getTeam(e.getTeam().toString()) == null) {
      team = RequestHelper.TeamResponses.getTeam(e.getTeam().toString());
      if (team != null) {
        UserManager.teamCache.put(
            e.getTeam().toString(),
            new Object[]{
                team, DataHelper.load(Keys.LOCAL_TEAM, new LocalTeam(e.getTeam().toString()))
            });
      }
    }
    team = (GlobalTeam) UserManager.getTeam(e.getTeam().toString())[0];
    team.delMember(e.getPlayer().getId().toString());
    RequestHelper.TeamResponses.overrideTeam(team);
    updatePlayer(e.getTeam().owner);
  }
}
