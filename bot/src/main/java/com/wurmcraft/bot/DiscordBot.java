package com.wurmcraft.bot;


import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import com.wurmcraft.bot.json.GlobalRestUser;
import com.wurmcraft.bot.json.Player;
import com.wurmcraft.bot.json.Players;
import com.wurmcraft.bot.json.ServerMatcher;
import com.wurmcraft.bot.json.ServerMatcher.Match;
import com.wurmcraft.bot.json.ServerTime;
import com.wurmcraft.bot.json.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;


public class DiscordBot {

  private static final int keyLength = 8;
  private static final long ACTIVE_TIME = (long) (30L * 8.64e7);

  // Setup for Wurmcraft Discord (varies per server)
  private static final String serverID = "144229954186510336";
  private static final long verifyRole = 661999713578385441L;
  private static ServerMatcher matcher;

  public static String auth;
  public static String baseURL;

  public static HashMap<Long, String> verifiedUsers = new HashMap<>();
  public static PteroUserAPI panelAPI;

  // 0 = Bot Auth Key
  // 1 = Rest API URL
  // 2 = Rest API Key
  // 3 = Panel URL
  // 4 = Panel User Key
  public static void main(String[] args) throws FileNotFoundException {
    matcher = RequestGenerator.GSON
        .fromJson(new FileReader(new File("servers.json")), ServerMatcher.class);
    if (args.length >= 5) {
      baseURL = com.wurmcraft.bot.RequestGenerator.parseConfigURL(args[1]);
      auth = "Basic " + args[2];
      panelAPI = new PteroUserAPI(args[3], args[4]);
      DiscordApi api = (new DiscordApiBuilder()).setToken(args[0]).login().join();
      FallbackLoggerConfiguration.setDebug(true);
      api.addMessageCreateListener(e -> {
        // Prevent the same discord ID from being verified multiple times
        if (e.isPrivateMessage() && e.getMessage().getContent().equalsIgnoreCase("!verify")
            && verifiedUsers.containsKey(e.getMessage().getUserAuthor().get().getId())) {
          e.getChannel().sendMessage("You have already been verified!");
        } else {
          if (e.isPrivateMessage() && e.getMessage().getContent().equalsIgnoreCase("!verify")) {
            String key = generateKey();
            e.getChannel().sendMessage("Your code is '" + key + "'");
            com.wurmcraft.bot.RequestGenerator.INSTANCE
                .post("/discord/add", new Token("" + e.getMessageAuthor().getId(), key));
            e.getChannel().sendMessage(
                "In-game type /verify <code> on any of the wurmcraft network servers, to link your discord account with your minecraft account");
          }
        }
        if (e.isPrivateMessage() && e.getMessage().getContent().startsWith("!deleteplayerfile")) {
          if (verifiedUsers.containsKey(e.getMessage().getUserAuthor().get().getId())) {
            String serverID = e.getMessage().getContent().replaceAll("(?i)!deleteplayerfile", "")
                .replaceAll(" ", "");
            boolean found = false;
            for (Match server : matcher.servers) {
              if (server.serverID.equalsIgnoreCase(serverID)) {
                found = true;
                List<UserServer> users = panelAPI.getServersController().getServers();
                for (UserServer pServer : users) {
                  if (pServer.getUuid().equals(server.panelID)) {
                    pServer.sendCommand("DPF " + verifiedUsers
                        .get(e.getMessage().getUserAuthor().get().getId()));
                    e.getChannel().sendMessage("Player file deleted!");
                  }
                }
              }
            }
            if (!found) {
              e.getMessage().getChannel().sendMessage(
                  "Invalid Server ID, " + serverID + " Checkout " + baseURL + "/status"
                      + " for the full list of servers.");
            }
          } else {
            System.out.println("You are not verified!");
          }
        } else if (e.isPrivateMessage() && e.getMessage().getContent().equalsIgnoreCase("!help")
            || e.isPrivateMessage() && e.getMessage().getContent().equalsIgnoreCase("help")) {
          e.getMessage().getChannel()
              .sendMessage("!verify (Gives a code for use in-game to verify your discord account");
          e.getMessage().getChannel().sendMessage(
              "!deleteplayerfile (deletes your player file on a given server, Notice, this cannot be undone)");
        }
      });
      api.getThreadPool().getScheduler().scheduleAtFixedRate(() -> {
        System.out.println("Checking for new user's to verify!");
        Player[] players = ((Players) RequestGenerator.INSTANCE
            .get("/user", (Class) Players.class)).players;
        verifiedUsers.clear();
        for (Player player : players) {
          if (player.discord != null && !player.discord.isEmpty()) {
            System.out
                .println("User '" + player.uuid + "' has been found, updating ranks!");
            try {
              User user = api.getUserById(player.discord).get();
              List<Role> roles = user.getRoles(api.getServerById(serverID).get());
              boolean verify = false;
              for (Role role : roles) {
                if (role.getId() == verifyRole && role.getUsers().contains(user)) {
                  verify = true;
                }
              }
              if (!verify) {
                System.out.println(
                    "User '" + user.getName() + "' has been verified with '" + player.uuid + "'");
                Role verifyDiscord = api.getRoleById(verifyRole).get();
                verifyDiscord.addUser(user);
                user.sendMessage("You have been verified!");
              }
              verifiedUsers.put(user.getId(), player.uuid);
              updateUserRanks(player, user, api);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }, 0L, 20L, TimeUnit.MINUTES);
    }
  }

  private static String generateKey() {
    int lower = 48;
    int higher = 122;
    Random random = new Random();
    return (random.ints(lower, higher + 1)
        .filter(i -> ((i <= 57 || i >= 65) && (i <= 90 || i >= 97))).limit(keyLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint,
            StringBuilder::append)).toString();
  }

  private static void updateUserRanks(Player player, User user, DiscordApi api) {
    GlobalRestUser globalRestUser = RequestGenerator.User.getUser(player.uuid);
    if (globalRestUser != null) {
      for (ServerTime time : globalRestUser.getServerData()) {
        for (Match match : matcher.servers) {
          if (match.serverID.equals(time.getServerID())) {
            Role role = api.getRoleById(match.rankID).get();
            if (time.getLastSeen() + ACTIVE_TIME > System.currentTimeMillis()) {
              if (!role.getUsers().contains(user)) {
                role.addUser(user);
              }
            } else {
              role.removeUser(user);
            }
          }
        }
      }
    }
  }

}
