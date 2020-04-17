package com.wurmcraft.serveressentials.core.api.json;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;

// <MODULE>_<COMMAND>_<SUB_COMMAND>
public class Language implements StoredDataType {

  // General
  public String langKey;

  // Core Module
  public String CORE_SE_VERSION;
  public String CORE_SE_STORAGE;
  public String CORE_SE_MODULES;

  public Language(String langKey) {
    this.langKey = langKey;
  }

  @Override
  public String getID() {
    return langKey;
  }
}
