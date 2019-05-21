package com.wurmcraft.serveressentials.api.storage.json;

import com.wurmcraft.serveressentials.api.storage.FileType;
import java.util.*;
import java.util.Collections;

public class Channel implements FileType {

  private String name;
  private String prefix;
  private boolean chatFilter;
  private boolean logChat;
  private Type type;
  private String typeData;
  private String[] filter;

  public Channel() {}

  /**
   * All the settings about a channel
   *
   * @param name Name of Channel (Used for Commands)
   * @param prefix Added before the player's name in chat
   * @param chatFilter Is the chat filter active in this channel
   * @param logChat Chat logged to a file
   * @param type Type of channel
   * @param data Stores the data about the type (IE: Pass = "Password", TeamCommand = "teamName",
   *     Rank = "Rank Name")
   * @param filter Replaces Words in chat with different ones based on this filter
   */
  public Channel(
      String name,
      String prefix,
      boolean chatFilter,
      boolean logChat,
      Type type,
      String data,
      String[] filter) {
    this.name = name;
    this.prefix = prefix;
    this.chatFilter = chatFilter;
    this.logChat = logChat;
    this.type = type;
    this.typeData = data;
    this.filter = filter;
  }

  public Channel(String name, String prefix, Type type, String data) {
    this.name = name;
    this.prefix = prefix;
    this.chatFilter = true;
    this.logChat = false;
    this.type = type;
    this.typeData = data;
  }

  public Channel(String name, String prefix, Type type, String data, String[] filter) {
    this.name = name;
    this.prefix = prefix;
    this.chatFilter = true;
    this.logChat = false;
    this.type = type;
    this.typeData = data;
    this.filter = filter;
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

  public boolean isChatFilter() {
    return chatFilter;
  }

  public void setChatFilter(boolean chatFilter) {
    this.chatFilter = chatFilter;
  }

  public boolean isLogChat() {
    return logChat;
  }

  public void setLogChat(boolean logChat) {
    this.logChat = logChat;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getTypeData() {
    return typeData;
  }

  public void setTypeData(String typeData) {
    this.typeData = typeData;
  }

  public String[] getFilter() {
    return filter;
  }

  public void setFilter(String[] filter) {
    this.filter = filter;
  }

  public void addFilter(String find, String replace) {
    addFilter(new String[] {find + " " + replace});
  }

  public void addFilter(String[] filter) {
    List<String> filters = Arrays.asList(getFilter());
    Collections.addAll(filters, filter);
    setFilter(filters.toArray(new String[0]));
  }

  @Override
  public String getID() {
    return name;
  }

  public enum Type {
    PUBLIC,
    PASS,
    TEAM,
    RANK
  }
}
