package com.wurmcraft.serveressentials.core.api.data;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;

/**
 * Allows for easy storage of data objects within the DataHandler System
 *
 * @see IDataHandler
 */
public interface StoredDataType extends JsonParser {

  /** @return ID of this given instance */
  String getID();
}
