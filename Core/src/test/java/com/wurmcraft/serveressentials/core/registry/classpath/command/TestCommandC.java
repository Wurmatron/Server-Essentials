package com.wurmcraft.serveressentials.core.registry.classpath.command;

import static com.wurmcraft.serveressentials.core.api.command.CommandArguments.INTEGER;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;

@ModuleCommand(name = "CommandC", moduleName = "TestC")
public class TestCommandC {

  @Command(
      inputArguments = {INTEGER, INTEGER},
      subCommand = "testCA")
  public void testC(int no, int no2) {}

  @Command(inputArguments = {INTEGER, INTEGER})
  public void testCA(int no, int no2) {}
}
