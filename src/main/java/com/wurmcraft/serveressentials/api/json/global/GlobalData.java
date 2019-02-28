package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GlobalData implements DataType {

  private SpawnPoint spawn;
  private String[] rules;
  private String[] motd;
  private String website;
  private boolean lockdown;
  private String[] bannedMods;
  private String[] globalMOTD;
  private HashMap<String, Integer> playerRewards = new HashMap<>();

  public GlobalData(SpawnPoint spawn, String[] rules, String[] motd, String website) {
    this.spawn = spawn;
    this.rules = rules;
    this.motd = motd;
    this.website = website;
    bannedMods = new String[0];
    globalMOTD = new String[0];
  }

  public SpawnPoint getSpawn() {
    return spawn;
  }

  public void setSpawn(SpawnPoint spawn) {
    this.spawn = spawn;
    DataHelper.forceSave(new File(ConfigHandler.saveLocation), this);
  }

  public String[] getRules() {
    return rules;
  }

  public void setRules(String[] rules) {
    this.rules = rules;
    DataHelper.forceSave(new File(ConfigHandler.saveLocation), this);
  }

  public void addRule(String rule) {
    String[] rules = getRules();
    if (rules != null) {
      List<String> listRules = new ArrayList<>();
      Collections.addAll(listRules, rules);
      listRules.add(rule);
      setRules(listRules.toArray(new String[0]));
    } else {
      setRules(new String[] {rule});
    }
  }

  public void removeRule(int id) {
    String[] rules = getRules();
    if (id < rules.length) {
      rules[id] = null;
      List<String> temp = new ArrayList<>();
      for (String rule : rules) {
        if (rule != null && rules.length > 0) {
          temp.add(rule);
        }
      }
      setRules(temp.toArray(new String[0]));
    }
  }

  public void removeMotd(int id) {
    String[] motd = getMotd();
    if (id < motd.length) {
      motd[id] = null;
      List<String> temp = new ArrayList<>();
      for (String mot : motd) {
        if (mot != null && rules.length > 0) {
          temp.add(mot);
        }
      }
      setMotd(temp.toArray(new String[0]));
    }
  }

  public String[] getMotd() {
    return motd;
  }

  public void setMotd(String[] motd) {
    this.motd = motd;
    DataHelper.forceSave(new File(ConfigHandler.saveLocation), this);
  }

  public void addMotd(String motd) {
    String[] motds = getMotd();
    if (motds != null) {
      List<String> listmotd = new ArrayList<>();
      Collections.addAll(listmotd, motds);
      listmotd.add(motd);
      setMotd(listmotd.toArray(new String[0]));
    } else {
      setMotd(new String[] {motd});
    }
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public boolean getLockDown() {
    return lockdown;
  }

  public void setLockDown(boolean lock) {
    lockdown = lock;
  }

  public String[] getBannedMods() {
    return bannedMods;
  }

  @Override
  public String getID() {
    return "Global";
  }

  public String[] getGlobalMOTD() {
    return globalMOTD;
  }

  public void setGlobalMOTD(String[] globalMOTD) {
    this.globalMOTD = globalMOTD;
    DataHelper.forceSave(new File(ConfigHandler.saveLocation), this);
  }

  public HashMap<String, Integer> getPlayerRewards() {
    return playerRewards;
  }

  public void setPlayerRewards(HashMap<String, Integer> playerRewards) {
    this.playerRewards = playerRewards;
  }

  public void addPlayerReward(UUID player, int amount) {
    playerRewards.put(player.toString(), playerRewards.getOrDefault(player.toString(), 0) + amount);
  }
}
