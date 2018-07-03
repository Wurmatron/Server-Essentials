package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandLoader {

  public static List<SECommand> loadedCommands;

  public static void locateCommands(ASMDataTable asmData) {
    List<SECommand> activeCommands = new ArrayList<>();
    for (ASMData data : asmData.getAll(Command.class.getName())) {
      try {
        String commandModule = data.getAnnotationInfo().getOrDefault("moduleName", "").toString();
        Class<?> asmClass = Class.forName(data.getClassName());
        Class<? extends SECommand> supportClass = asmClass.asSubclass(SECommand.class);
        SECommand command = supportClass.newInstance();
        if (!commandModule.isEmpty()) {
          for (IModule module : ServerEssentialsAPI.modules) {
            if (module.getClass().getAnnotation(Module.class) != null
                && module
                .getClass()
                .getAnnotation(Module.class)
                .name()
                .equalsIgnoreCase(commandModule)) {
              activeCommands.add(command);
              break;
            }
          }
        } else {
          activeCommands.add(command);
        }
        ServerEssentialsServer.logger.debug(
            "Loading Command '"
                + command.getName()
                + "' with permission "
                + command.getPermission());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    loadedCommands = activeCommands;
  }

  public static void registerCommands(FMLServerStartingEvent e) {
    if (loadedCommands.size() > 0) {
      for (SECommand command : loadedCommands) {
        e.registerServerCommand(command);
        ServerEssentialsServer.logger.debug(
            "Registered Command '"
                + command.getName()
                + "' with permission "
                + command.getPermission());
      }
    } else {
      ServerEssentialsServer.logger.error("No Commands Loaded / Found!");
    }
  }
}
