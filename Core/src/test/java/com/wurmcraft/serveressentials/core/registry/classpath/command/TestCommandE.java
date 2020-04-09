package com.wurmcraft.serveressentials.core.registry.classpath.command;

import static com.wurmcraft.serveressentials.core.api.command.CommandArguments.INTEGER;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;

@ModuleCommand(name = "CommandE", moduleName = "TestG")
public class TestCommandE {

  @Command(inputArguments = {INTEGER, INTEGER})
  public void testG(int no, int no2) {}
}
