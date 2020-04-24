package com.wurmcraft.serveressentials.core.api.json;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;

// <MODULE>_<COMMAND>_<SUB_COMMAND>
public class Language implements StoredDataType {

  // General
  public String langKey;

  // Error
  public String ERROR_NO_PERMS;
  public String ERROR_PLAYER_NOT_FOUND;

  // Core Module
  public String CORE_SE_VERSION;
  public String CORE_SE_STORAGE;
  public String CORE_SE_MODULES;

  // Language
  public String LANGUAGE_LANGUAGE_NO_FOUND;
  public String LANGUAGE_LANGUAGE_SET;

  // General
  public String GENERAL_TPA_SENT;
  public String GENERAL_TPA_REQUEST;

  public Language(String langKey) {
    this.langKey = langKey;
  }

  @Override
  public String getID() {
    return langKey;
  }
}
