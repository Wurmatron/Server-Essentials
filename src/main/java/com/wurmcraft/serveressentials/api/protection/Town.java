package com.wurmcraft.serveressentials.api.protection;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import java.util.ArrayList;
import java.util.List;

public class Town implements DataType {

  private String name;
  private String ownerID;
  private List<ClaimedArea> claimedArea;

  public Town() {
    name = "";
    ownerID = "";
    claimedArea = new ArrayList<>();
  }

  public Town(String name, String ownerID) {
    this.name = name;
    this.ownerID = ownerID;
    claimedArea = new ArrayList<>();
  }

  @Override
  public String getID() {
    return name;
  }

  public List<ClaimedArea> getClaimedArea() {
    return claimedArea;
  }

  public void addClaimedArea(ClaimedArea area) {
    claimedArea.add(area);
  }

  public String getOwnerID() {
    return ownerID;
  }
}
