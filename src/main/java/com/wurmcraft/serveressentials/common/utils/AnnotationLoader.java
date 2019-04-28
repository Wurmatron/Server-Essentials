package com.wurmcraft.serveressentials.common.utils;

import static com.wurmcraft.serveressentials.api.ServerEssentialsAPI.isModuleLoaded;
import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.LOGGER;
import static com.wurmcraft.serveressentials.common.utils.command.CommandUtils.isCommandDisabled;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.CommandLoadEvent;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;

public class AnnotationLoader {

  private AnnotationLoader() {}

  private static List<Object> getAnnotations(ASMDataTable asmTable, String annotationName) {
    List<Object> modules = new ArrayList<>();
    for (ASMData data : asmTable.getAll(annotationName)) {
      try {
        modules.add(Class.forName(data.getClassName()).newInstance());
      } catch (Exception e) {
        LOGGER.error(e.getLocalizedMessage());
      }
    }
    return modules;
  }

  // Get a list of module instances
  public static List<Object> loadModules(ASMDataTable asmTable) {
    List<Object> activeModules = new ArrayList<>();
    getAnnotations(asmTable, Module.class.getName())
        .forEach(
            module ->
                Arrays.stream(ConfigHandler.modules)
                    .filter(
                        activeModule ->
                            activeModule.equalsIgnoreCase(
                                module.getClass().getAnnotation(Module.class).name()))
                    .map(activeModule -> module)
                    .forEach(activeModules::add));
    return activeModules;
  }

  // Get a list of commands
  public static List<Command> loadCommands(ASMDataTable asmTable) {
    List<Command> commands = new ArrayList<>();
    for (Object command : getAnnotations(asmTable, ModuleCommand.class.getName())) {
      String commandModule = command.getClass().getAnnotation(ModuleCommand.class).moduleName();
      if (isModuleLoaded(commandModule) && command instanceof Command) {
        CommandLoadEvent commandLoad =
            new CommandLoadEvent(
                commandModule,
                (Command) command,
                command.getClass().getAnnotation(ModuleCommand.class).perm(),
                command.getClass().getAnnotation(ModuleCommand.class).trustedRequired());
        MinecraftForge.EVENT_BUS.post(commandLoad);
        if (!commandLoad.isCanceled() && !isCommandDisabled(((Command) command).getName())) {
          commands.add((Command) command);
        }
      }
    }
    return commands;
  }

  // Convert an list of Module instances into its names
  public static List<String> moduleListToName(List<Object> activeModules) {
    List<String> modules = new ArrayList<>();
    activeModules
        .stream()
        .map(module -> module.getClass().getAnnotation(Module.class).name())
        .forEach(activeModules::add);
    return modules;
  }
}
