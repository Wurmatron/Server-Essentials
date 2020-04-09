package com.wurmcraft.serveressentials.core.registry.classpath.command;

import static com.wurmcraft.serveressentials.core.api.command.CommandArguments.INTEGER;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;

@ModuleCommand(name = "CommandB", moduleName = "TestA")
public class TestCommandB {

  @Command(inputArguments = {INTEGER})
  public void testB(int no) {}
}
