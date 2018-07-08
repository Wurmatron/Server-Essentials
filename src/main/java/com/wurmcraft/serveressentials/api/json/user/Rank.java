package com.wurmcraft.serveressentials.api.json.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Rank implements IDataType {

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

  public Rank(Rank rank) {
    this.name = rank.name;
    this.prefix = rank.prefix;
    this.suffix = rank.suffix;
    this.inheritance = rank.inheritance;
    this.permission = rank.permission;
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

  @Override
  public String getID() {
    return name;
  }

  public void addPermission(String... perm) {
    List<String> currentPermissions = new ArrayList<>();
    Collections.addAll(currentPermissions, getPermission());
    currentPermissions.addAll(Arrays.asList(perm));
    this.permission = currentPermissions.toArray(new String[0]);
  }

  public void delPermission(String perm) {
    List<String> currentPermissions = new ArrayList<>();
    Collections.addAll(currentPermissions, getPermission());
    String delNode = "";
    for (String node : currentPermissions) {
      if (node.equalsIgnoreCase(perm)) {
        delNode = node;
      }
    }
    currentPermissions.remove(delNode);
    this.permission = currentPermissions.toArray(new String[0]);
  }
}
