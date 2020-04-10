package com.wurmcraft.serveressentials.core;

import com.wurmcraft.serveressentials.core.registry.TestModuleConfig;
import com.wurmcraft.serveressentials.core.registry.TestSERegistry;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    TestModuleConfig.class,
    TestSERegistry.class
})
public class CoreTestSuite {

}
