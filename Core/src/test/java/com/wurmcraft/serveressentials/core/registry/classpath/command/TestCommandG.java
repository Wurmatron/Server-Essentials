package com.wurmcraft.serveressentials.core.registry.classpath.command;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;

@ModuleCommand(name = "CommandG", moduleName = "TestA")
public class TestCommandG {

  @Command(inputArguments = {})
  public void testG() {}
}
