package com.wurmcraft.serveressentials.forge.modules.rank;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import org.cliffc.high_scale_lib.NonBlockingHashMap;


public class RankUtils {

  public static void scheduleRankUpdates(int delayPeriod) {
    SECore.executors.scheduleAtFixedRate(() -> {
      NonBlockingHashMap<String, Rank> data = SECore.dataHandler
          .getDataFromKey(DataKey.RANK, new Rank());
      if (data.isEmpty()) {
        ServerEssentialsServer.logger.fatal("Connection to rank database is broken!");
      }
    }, 300, delayPeriod, TimeUnit.SECONDS);
  }

  public static boolean hasPermission(Rank rank, String perm) {
    String[] rankPermissions = getPermissions(rank);
    String[] permShredder = perm.split("\\.");
    String module = permShredder[0];
    String command = permShredder[1];
    String subCommand = "*";
    if (permShredder.length == 3) {
      subCommand = permShredder[2];
    }
    for (String p : rankPermissions) {
      if (p.equals("*")) {
        return true;
      } else {
        String[] splitNode = p.split("\\.");
        String cm = splitNode[0];
        String ccm = splitNode[1];
        String csc = "*";
        boolean quickCheck = module.equalsIgnoreCase(cm) && command.equalsIgnoreCase(ccm);
        if (splitNode.length == 3) {
          csc = splitNode[2];
        }
        if (!csc.equals("*") && !subCommand.equals("*")) {
             if(quickCheck && subCommand.equalsIgnoreCase(csc))
               return true;
        } else if(quickCheck){
          return true;
        }
      }
    }
    return false;
  }

  public static String[] getPermissions(Rank rank) {
    List<String> permissionNodes = new ArrayList<>();
    Collections.addAll(permissionNodes, rank.getPermission());
    if (rank.getInheritance() != null && rank.getInheritance().length > 0) {
      for (String ih : rank.getInheritance()) {
        Collections.addAll(permissionNodes,
            getPermissions((Rank) SERegistry.getStoredData(DataKey.RANK, ih)));
      }
    }
    return permissionNodes.toArray(new String[0]);
  }

  public static Rank getRank(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry.getStoredData(DataKey.PLAYER,
            ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId()
                .toString());
        return (Rank) SERegistry.getStoredData(DataKey.RANK, playerData.global.rank);
      } catch (NoSuchElementException e) {
        ServerEssentialsServer.logger.info(sender.getName() + " does not have any loaded data / a valid rank!");
        return new Rank();
      }
    }
    return new Rank("Console","[Console]","",new String[0], new String[] {"*"});
  }

  public static void setRank(EntityPlayer player, Rank rank) {
    StoredPlayer playerData = (StoredPlayer) SERegistry.getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
    playerData.global.rank = rank.getID();
    SERegistry.register(DataKey.PLAYER, playerData);
    if(SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      GlobalPlayer restData = RestRequestGenerator.User.getPlayer(player.getGameProfile().getId().toString());
      restData.rank = rank.getID();
      RestRequestGenerator.User.overridePlayer(player.getGameProfile().getId().toString(),restData);
      playerData.global = restData;
    }
  }
}
