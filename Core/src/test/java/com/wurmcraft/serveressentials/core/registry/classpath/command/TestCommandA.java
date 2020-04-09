package com.wurmcraft.serveressentials.core.registry.classpath.command;

import static com.wurmcraft.serveressentials.core.api.command.CommandArguments.INTEGER;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;

@ModuleCommand(name = "CommandA", moduleName = "TestA")
public class TestCommandA {

  @Command(inputArguments = {INTEGER})
  public void testA(int no) {}
}
