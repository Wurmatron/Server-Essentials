package com.wurmcraft.serveressentials.api.json.user;

public class Rank {

  private String name;
  private String prefix;
  private String suffix;
  private String[] inheritance;
  private String[] permission;

  public Rank() {
    name = "";
    prefix = "";
    suffix = "";
    inheritance = new String[] {};
    permission = new String[] {};
  }

  public Rank(
      String name, String prefix, String suffix, String[] inheritance, String[] permission) {
    this.name = name;
    this.prefix = prefix;
    this.suffix = suffix;
    this.inheritance = inheritance;
    this.permission = permission;
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

  public String[] getInheritance() {
    return inheritance;
  }

  public void setInheritance(String[] inheritance) {
    this.inheritance = inheritance;
  }

  public String[] getPermission() {
    return permission;
  }

  public void setPermission(String[] permission) {
    this.permission = permission;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }
}
