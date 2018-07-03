package com.wurmcraft.serveressentials.common.utils;

public class CommandUtils {

  public static String[] getArgsAfterCommand(int argPos, String[] args) {
    if (argPos < args.length) {
      return ArrayUtils.splice(args, argPos, args.length - 1);
    }
    return new String[0];
  }

}
