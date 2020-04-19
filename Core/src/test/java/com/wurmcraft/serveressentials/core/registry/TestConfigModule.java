package com.wurmcraft.serveressentials.core.registry;

import static com.wurmcraft.serveressentials.core.SECore.GSON;
import static org.junit.Assert.assertEquals;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.classpath.module.config.TestConfigA;
import com.wurmcraft.serveressentials.core.utils.ModuleUtils;
import java.nio.file.Files;
import org.junit.Before;
import org.junit.Test;

public class TestConfigModule {

  @Before
  public void setup() {
    SECore.setup();
  }

  @Test
  public void TestModuleConfigLoading_TestA() {
    ModuleUtils.loadModuleConfigs();
    // Change Config Default Value
    TestConfigA config = (TestConfigA) SERegistry.getStoredData(DataKey.MODULE_CONFIG, "TestA");
    config.someConfigValue = "ABC";
    try {
      Files.write(
          ModuleUtils.getModuleConfigFile("TestA").toPath(), GSON.toJson(config).getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Reload changed config
    ModuleUtils.loadModuleConfigs();
    config = (TestConfigA) SERegistry.getStoredData(DataKey.MODULE_CONFIG, "TestA");
    assertEquals("TestConfigA has not loaded correctly!", "ABC", config.someConfigValue);
  }
}
