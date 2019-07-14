package com.wurmcraft.serveressentials.api.storage;

/** Used to automatically save files based on there ID */
public interface FileType {

  /**
   * Used as the name for the file to be created
   *
   * @return ID / name of the file
   */
  String getID();
}
