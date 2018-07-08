package com.wurmcraft.serveressentials.common.team;

import static com.wurmcraft.serveressentials.common.ConfigHandler.saveLocation;
import static com.wurmcraft.serveressentials.common.reference.Keys.TEAM;

import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.fileOnly.Team;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.LocalTeam;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.RestModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.team.events.FTBUtilsEvents;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

@Module(name = "Team")
public class TeamModule implements IModule {

  private static boolean globalTeams = false;

  public static void loadRestTeam(UUID uuid) {
    if (globalTeams) {
      GlobalUser globalUser = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      GlobalTeam globalTeam = RequestHelper.TeamResponses.getTeam(globalUser.getTeam());
      if (globalTeam != null) {
        LocalTeam localTeam = DataHelper.load(Keys.LOCAL_TEAM, new LocalTeam(globalUser.getTeam()));
        if (localTeam != null) {
          UserManager.teamCache.put(globalUser.getTeam(), new Object[] {globalTeam, localTeam});
        } else {
          localTeam = new LocalTeam(globalUser.getTeam());
          DataHelper.forceSave(Keys.LOCAL_TEAM, localTeam);
          UserManager.teamCache.put(globalUser.getTeam(), new Object[] {globalTeam, localTeam});
        }
      }
    }
  }

  public static void loadRestTeam(String team) {
    if (globalTeams) {
      GlobalTeam globalTeam = RequestHelper.TeamResponses.getTeam(team);
      if (!team.isEmpty() && globalTeam != null) {
        LocalTeam localTeam = DataHelper.load(Keys.LOCAL_TEAM, new LocalTeam(team));
        if (localTeam != null) {
          UserManager.teamCache.put(team, new Object[] {globalTeam, localTeam});
        } else {
          localTeam = new LocalTeam(team);
          DataHelper.forceSave(Keys.LOCAL_TEAM, localTeam);
          UserManager.teamCache.put(team, new Object[] {globalTeam, localTeam});
        }
      }
    }
  }

  public static void unloadRestTeam(GlobalUser user) {
    if (globalTeams) {
      RestModule.executors.schedule(
          () -> {
            int userLoadingTotal = isTeamLoaded(user.getTeam());
            if (userLoadingTotal <= 1) {
              UserManager.teamCache.remove(user.getTeam());
            }
          },
          ConfigHandler.syncPeriod,
          TimeUnit.MINUTES);
    }
  }

  private static int isTeamLoaded(String team) {
    int totalPlayers = 0;
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      GlobalUser globalUser =
          (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      if (globalUser.getTeam().equalsIgnoreCase(team)) {
        totalPlayers++;
      }
    }
    return totalPlayers;
  }

  @Override
  public void setup() {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      if (!Loader.isModLoaded("ftbutilities")) {
        loadAllTeams();
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      globalTeams = true;
      if (Loader.isModLoaded("ftbutilities")) {
        MinecraftForge.EVENT_BUS.register(new FTBUtilsEvents());
      }
    }
  }

  private void loadAllTeams() {
    File teamDir = new File(saveLocation + File.separator + TEAM.name());
    if (teamDir.exists()) {
      for (File file : Objects.requireNonNull(teamDir.listFiles())) {
        Team team = DataHelper.load(file, Keys.TEAM, new Team());
        UserManager.teamCache.put(team.getName(), new Object[] {team});
      }
    } else {
      teamDir.mkdirs();
    }
  }
}
