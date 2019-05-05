package com.wurmcraft.serveressentials.api.storage;

/**
 * Used to allow for different Server-Essentials data storage types
 *
 * @see com.wurmcraft.serveressentials.common.storage.rest.RestStorage
 * @see com.wurmcraft.serveressentials.common.storage.file.FileStorage
 */
public interface Storage {

  /**
   * Called within Pre-Init to setup this storage type and load anything that'srequiredd for this
   * storage type to function, called after the list of active modules has been generated
   */
  void setup();
}
