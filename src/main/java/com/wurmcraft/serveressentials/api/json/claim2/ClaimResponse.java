package com.wurmcraft.serveressentials.api.json.claim2;

public enum ClaimResponse {
  SUCCESSFUL(""),
  FAILED(""),
  EMPTY(""),
  UNSUPPORTED("");

  private String key;

  ClaimResponse(String langKey) {
    this.key = langKey;
  }
}
