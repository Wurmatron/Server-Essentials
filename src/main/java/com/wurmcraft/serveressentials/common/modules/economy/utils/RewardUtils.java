package com.wurmcraft.serveressentials.common.modules.economy.utils;

import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.api.storage.json.Reward;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.util.*;
import net.minecraft.world.World;

public class RewardUtils {

  public static int getMaxTier() {
    if (DataHelper.getData(Storage.REWARD).size() > 0) {
      int max = 0;
      for (FileType type : DataHelper.getData(Storage.REWARD)) {
        if (((Reward) type).tier > max) {
          max = ((Reward) type).tier;
        }
      }
      return max;
    } else {
      return -1;
    }
  }

  private static int randomTier(World world) {
    double rand = world.rand.nextDouble();
    return (int) (getMaxTier() * rand * rand);
  }

  private static List<Reward> getRewardsPerTier(int tier) {
    if (DataHelper.getData(Storage.REWARD).size() > 0) {
      List<Reward> inThisTier = new ArrayList<>();
      for (FileType type : DataHelper.getData(Storage.REWARD)) {
        if (((Reward) type).tier == tier) {
          inThisTier.add((Reward) type);
        }
      }
      return inThisTier;
    }
    return new ArrayList<>();
  }

  public static Reward getRandomReward(World world, int tierAddon) {
    List<Reward> possibleRewards = getRewardsPerTier(randomTier(world) + tierAddon);
    int poolSize = 0;
    for (Reward reward : possibleRewards) {
      poolSize += reward.chance;
    }
    int rewardNo = world.rand.nextInt(poolSize) + 1;
    int probobility = 0;
    for (int index = 0; index < possibleRewards.size(); index++) {
      probobility += possibleRewards.get(index).chance;
      if (rewardNo <= probobility) {
        return possibleRewards.get(index);
      }
    }
    return null;
  }
}
