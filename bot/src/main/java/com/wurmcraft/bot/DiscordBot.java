package java.com.wurmcraft.bot;


import com.wurmcraft.json.Token;
import java.util.Random;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class DiscordBot {

  private static final int keyLength = 8;

  public static String auth;
  public static String baseURL;

  // [0] = Discord BOT API
  // [1] = Rest Web
  /// [2] = Rest Api key (Base-64)
  public static void main(String[] args) {
    if (args.length >= 3) {
      baseURL = RequestGenerator.parseConfigURL(args[1]);
      auth = args[2];
      DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();
      api.addMessageCreateListener((MessageCreateEvent e) -> {
        if (e.isPrivateMessage() && e.getMessage().getContent().equalsIgnoreCase("!verify")) {
          String key = generateKey();
          e.getChannel().sendMessage("Your code is '" + key + "'");
          RequestGenerator.INSTANCE.post("discord/add", new Token("" + e.getMessageAuthor().getId(), key));
          e.getChannel().sendMessage("In-game type /verify <code> on any of the wurmcraft network servers, to link your discord account with your "
              + "minecraft account");
        }
      });
    }
  }

  private static String generateKey() {
    int lower = 48;
    int higher = 122;
    Random random = new Random();
    return random.ints(lower, higher + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(keyLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
  }
}
