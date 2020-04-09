package com.wurmcraft.serveressentials.core.registry.classpath.command;

import static com.wurmcraft.serveressentials.core.api.command.CommandArguments.INTEGER;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;

@ModuleCommand(name = "CommandD", moduleName = "TestC")
public class TestCommandD {

  @Command(inputArguments = {INTEGER, INTEGER})
  public void testD(int no, int no2) {}
}
