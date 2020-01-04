package com.wurmcraft.bot;


import com.wurmcraft.bot.json.Player;
import com.wurmcraft.bot.json.Players;
import com.wurmcraft.bot.json.Token;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import org.javacord.api.util.logging.FallbackLoggerConfiguration;


public class DiscordBot {

  private static final int keyLength = 8;

  // Setup for Wurmcraft Discord (varies per server)
  private static final String serverID = "144229954186510336";
  private static final long verifyRole = 661999713578385441L;

  public static String auth;
  public static String baseURL;

  public static List<Long> verifiedUsers = new ArrayList<>();

  public static void main(String[] args) {
    if (args.length >= 3) {
      baseURL = com.wurmcraft.bot.RequestGenerator.parseConfigURL(args[1]);
      auth = "Basic " + args[2];
      DiscordApi api = (new DiscordApiBuilder()).setToken(args[0]).login().join();
      FallbackLoggerConfiguration.setDebug(true);
      api.addMessageCreateListener(e -> {
        // Prevent the same discord ID from being verified multiple times
        if(verifiedUsers.contains(e.getMessage().getUserAuthor().get().getId())) {
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
      });
      api.getThreadPool().getScheduler().scheduleAtFixedRate(() -> {
        System.out.println("Checking for new user's to verify!");
        Player[] players = ((Players) com.wurmcraft.bot.RequestGenerator.INSTANCE
            .get("/user", (Class) Players.class)).players;
        verifiedUsers.clear();
        for (Player player : players) {
          if (player.discord != null && !player.discord.isEmpty()) {
            System.out
                .println("User '" + player.uuid + "' has been verified, Checking discord rank!");
            try {
              User user = api.getUserById(player.discord).get();
              List<Role> roles = user.getRoles(api.getServerById(serverID).get());
              boolean verify = false;
              for (Role role : roles) {
                if (role.getId() == 661999713578385441L && role.getUsers().contains(user)) {
                  verify = true;
                }
              }
              if (!verify) {
                System.out.println(
                    "User '" + user.getName() + "' has been verified with '" + player.uuid + "'");
                Role verifyDiscord = api.getRoleById(verifyRole).get();
                verifyDiscord.addUser(user);
                user.sendMessage("You have been verified!");
                verifiedUsers.add(user.getId());
              } else {
                verifiedUsers.add(user.getId());
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }, 0L, 5L, TimeUnit.MINUTES);
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
}
