package com.wurmcraft.serveressentials.core.registry;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
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
      e.printStackTrace();
    }
    assertNull("ModuleE is loaded!", module);
  }

  @Test
  public void TestModuleG() {
    Object module = null;
    try {
      module = SERegistry.getModule("TestG");
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertNull("ModuleG is loaded!", module);
  }

  @Test
  public void TestCommandLoadedTestA() {
    assertTrue("Command 'CommandA' is not loaded!", SERegistry.isCommandLoaded("CommandA"));
  }

  @Test
  public void TestCommandLoadedTestB() {
    assertTrue("Command 'CommandB' is not loaded!", SERegistry.isCommandLoaded("CommandB"));
  }

  @Test
  public void TestCommandLoadedTestC() {
    assertTrue("Command 'CommandC' is not loaded!", SERegistry.isCommandLoaded("CommandC"));
  }

  @Test
  public void TestCommandLoadedTestD() {
    assertTrue("Command 'CommandD' is not loaded!", SERegistry.isCommandLoaded("CommandD"));
  }

  @Test
  public void TestCommandLoadedTestE() {
    assertFalse("Command 'CommandE' is loaded!", SERegistry.isCommandLoaded("CommandE"));
  }

  @Test
  public void TestCommandLoadedTestF() {
    assertFalse("Command 'CommandF' is  loaded!", SERegistry.isCommandLoaded("CommandF"));
  }

  @Test
  public void TestCommandLoadedTestG() {
    assertTrue("Command 'CommandG' is not loaded!", SERegistry.isCommandLoaded("CommandG"));
  }

  @Test
  public void TestGetCommandValid() {
    assertNotNull("CommandA is not loaded", SERegistry.getCommand("CommandA"));
  }

  @Test
  public void TestGetCommandInvalid() {
    try {
      assertNull("CommandF is loaded!", SERegistry.getCommand("CommandF"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void TestValueSave() {
    SERegistry.register(DataKey.PLAYER, () -> "test");
    try {
      StoredDataType data = SERegistry.getStoredData(DataKey.PLAYER, "test");
      assertEquals("Storage is not working correct", data.getID(), "test");
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void TestValueDel() {
    SERegistry.register(DataKey.PLAYER, () -> "test");
    StoredDataType data = null;
    try {
      data = SERegistry.getStoredData(DataKey.PLAYER, "test");
      if (data != null) {
        SERegistry.delStoredData(DataKey.PLAYER, data.getID());
        data = null;
        data = SERegistry.getStoredData(DataKey.PLAYER, "test");
      }
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
    assertNull("Stored Data for Player 'Test' exists after being removed", data);
  }
}
