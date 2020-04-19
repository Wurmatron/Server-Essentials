package com.wurmcraft.serveressentials.core;

import com.wurmcraft.serveressentials.core.registry.TestConfigModule;
import com.wurmcraft.serveressentials.core.registry.TestSERegistry;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestConfigModule.class, TestSERegistry.class})
public class CoreTestSuite {}
