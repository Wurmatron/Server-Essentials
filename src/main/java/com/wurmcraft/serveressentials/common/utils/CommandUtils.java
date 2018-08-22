package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandUtils {

  public static String[] getArgsAfterCommand(int argPos, String[] args) {
    if (argPos < args.length) {
      return ArrayUtils.splice(args, argPos, args.length - 1);
    }
    return new String[0];
  }

  public static String[] getSenderPermissions(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() != null) {
      if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
        GlobalUser user =
            (GlobalUser)
                UserManager.getPlayerData(((EntityPlayer) sender.getCommandSenderEntity()))[0];
        List<String> perms = new ArrayList<>();
        Rank userRank = UserManager.getRank(user.getRank());
        Collections.addAll(perms, user.getPermission());
        Collections.addAll(perms, userRank.getPermission());
        for (String inheritance : userRank.getInheritance()) {
          Rank lowerRank = UserManager.getRank(inheritance);
          if (lowerRank != null) {
            Collections.addAll(perms, lowerRank.getPermission());
          }
        }
        return perms.toArray(new String[0]);
      } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
        PlayerData data =
            (PlayerData)
                UserManager.getPlayerData(((EntityPlayer) sender.getCommandSenderEntity()))[0];
        return data.getRank().getPermission();
      }
    }
    return new String[0];
  }

  public static boolean hasPerm(String perm, ICommandSender sender) {
    String[] perms = getSenderPermissions(sender);
    for (String p : perms) {
      if (p.equalsIgnoreCase(perm) || p.equalsIgnoreCase("*")) {
        return true;
      }
      if (p.contains("*")
          && p.contains(".")
          && perm.contains(".")
          && p.substring(0, p.lastIndexOf("."))
              .equalsIgnoreCase(perm.substring(0, perm.lastIndexOf(".")))) {
        return true;
      }
    }
    return false;
  }

  public static boolean isNumberAtPos(String[] commandArgs, int index) {
    if (commandArgs != null && commandArgs.length >= index) {
      try {
        Integer.parseInt(commandArgs[index]);
      } catch (NumberFormatException e) {
      }
    }
    return false;
  }
}
