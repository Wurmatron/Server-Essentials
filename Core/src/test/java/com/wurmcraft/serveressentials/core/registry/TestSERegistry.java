package com.wurmcraft.serveressentials.core.registry;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class TestSERegistry {

  private List<String> modulesThatShouldBeLoaded =
      Arrays.asList("TestA", "TestB", "TestC", "TestD");

  @Before
  public void setup() {
    SERegistry.loadAndSetup();
  }

  @Test
  public void TestLoadedModulesArray() {
    List<String> loadedModules = Arrays.asList(SERegistry.getLoadedModules());
    assertTrue(
        modulesThatShouldBeLoaded.size() == loadedModules.size()
            && modulesThatShouldBeLoaded.containsAll(loadedModules)
            && loadedModules.containsAll(modulesThatShouldBeLoaded));
  }

  @Test
  public void TestIsModuleLoaded() {
    assertFalse("ModuleE is somehow loaded!", SERegistry.isModuleLoaded("ModuleE"));
  }

  @Test
  public void TestGetModuleB() {
    Object module = SERegistry.getModule("TestB");
    Field[] field = module.getClass().getDeclaredFields();
    try {
      String moduleField = "";
      if (field.length > 0) {
        moduleField = field[0].get("someVar").toString();
      }
      assertEquals("Module 'TestB' is loaded correctly!", "B", moduleField);
    } catch (Exception e) {
      System.out.println("Field Data in the TestSERegistry#TestGetModule test!");
    }
  }

  @Test
  public void TestGetModuleE() {
    Object module = null;
    try {
      module = SERegistry.getModule("TestE");
    } catch (Exception e) {
    }
    assertNull("ModuleE is loaded!", module);
  }

  @Test
  public void TestModuleG() {
    Object module = null;
    try {
      module = SERegistry.getModule("TestG");
    } catch (Exception e) {
    }
    assertNull("ModuleG is loaded!", module);
  }
}
