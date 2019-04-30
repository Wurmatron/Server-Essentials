package com.wurmcraft.serveressentials.common.storage.rest;

import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.LOGGER;
import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.instance;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;

public class RequestGenerator {

  public static final String USER_AGENT = "Mozilla/5.0";
  public static final RequestGenerator INSTANCE =
      new RequestGenerator(ConfigHandler.restURL, ConfigHandler.restAuth);

  private String auth;
  private String baseURL;

  public RequestGenerator(String restURL, String restAuth) {
    baseURL = parseConfigURL(restURL);
    auth = createRestAuth(restAuth);
  }

  private boolean isBase64(String data) {
    try {
      Base64.getDecoder().decode(data.getBytes());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private String createRestAuth(String auth) {
    if (isBase64(auth)) {
      return "Basic " + auth.getBytes();
    } else {
      return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
  }

  private String parseConfigURL(String url) {
    if (url.endsWith("/")) {
      return ConfigHandler.restURL;
    } else {
      return ConfigHandler.restURL + "/";
    }
  }

  /**
   * Send some data to a given URL as a json format
   *
   * @param type type of request (PUT, POST)
   * @param url additional info in the url
   * @param data object to send to the url
   * @return https status code for https connection
   */
  private int send(String type, String url, Object data) {
    try {
      URL sendURL = new URL(baseURL + url);
      URLConnection connection = sendURL.openConnection();
      HttpsURLConnection https = (HttpsURLConnection) connection;
      https.setRequestMethod(type.toUpperCase());
      https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      https.setDoOutput(true);
      https.setRequestProperty("Authorization", auth);
      String json = instance.GSON.toJson(data);
      connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
      int status = ((HttpsURLConnection) connection).getResponseCode();
      if (status == HttpsURLConnection.HTTP_UNAUTHORIZED) {
        LOGGER.error("Invalid Rest API Key, Unable to Put");
      }
      return status;
    } catch (Exception e) {
      LOGGER.warn(e.getLocalizedMessage());
    }
    return 418; //  I'm a teapot
  }

  public void post(String url, Object data) {
    send("post", url, data);
  }

  public void put(String url, Object data) {
    send("put", url, data);
  }

  /**
   * Get the given data from the url and
   *
   * @param type type of object to cast the url data to
   * @param data additional info about the url
   * @return The url as the given object
   */
  public <T extends Object> T get(String data, Class<T> type) {
    if (!data.isEmpty()) {
      try {
        URL obj = new URL(baseURL + data);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK
            || responseCode == HttpsURLConnection.HTTP_ACCEPTED) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return instance.GSON.fromJson(response.toString(), type);
        }
      } catch (Exception e) {
        LOGGER.warn(e.getLocalizedMessage());
      }
    }
    return null;
  }
}
