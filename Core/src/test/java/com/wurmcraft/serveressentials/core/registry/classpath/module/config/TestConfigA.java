package com.wurmcraft.serveressentials.core.registry.classpath.module.config;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ModuleConfig;

@ModuleConfig(moduleName = "TestA")
public class TestConfigA implements StoredDataType {

  public String someConfigValue;
  public boolean value2;

  public TestConfigA() {
    this.someConfigValue = "abcdef";
    this.value2 = false;
  }

  public TestConfigA(String someConfigValue, boolean value2) {
    this.someConfigValue = someConfigValue;
    this.value2 = value2;
  }

  @Override
  public String getID() {
    return "TestA";
  }
}
