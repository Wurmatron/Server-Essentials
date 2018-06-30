package com.wurmcraft.serveressentials.api.json.user;

public class Rank {

  private String name;
  private String prefix;
  private String inheritance;
  private String permissions;

  public Rank(String name, String prefix, String inheritance, String permissions) {
    this.name = name;
    this.prefix = prefix;
    this.inheritance = inheritance;
    this.permissions = permissions;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getInheritance() {
    return inheritance;
  }

  public void setInheritance(String inheritance) {
    this.inheritance = inheritance;
  }

  public String getPermissions() {
    return permissions;
  }

  public void setPermissions(String permissions) {
    this.permissions = permissions;
  }
}
