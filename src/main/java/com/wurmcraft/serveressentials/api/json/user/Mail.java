package com.wurmcraft.serveressentials.api.json.user;

import java.util.UUID;

/**
 * Stores a message sent from another user
 */
public class Mail {

  private UUID sender;
  private UUID reciver;
  private String message;
  private long timestamp;

  public Mail(UUID sender, UUID reciver, String message) {
    this.sender = sender;
    this.reciver = reciver;
    this.message = message;
    this.timestamp = System.currentTimeMillis();
  }

  public UUID getSender() {
    return sender;
  }

  public void setSender(UUID sender) {
    this.sender = sender;
  }

  public UUID getReciver() {
    return reciver;
  }

  public void setReciver(UUID reciver) {
    this.reciver = reciver;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
