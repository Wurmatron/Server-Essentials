package wurmcraft.serveressentials.common.commands.admin;


import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.security.SecurityUtils;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class ModListCommand extends SECommand {

  public ModListCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "modlist";
  }

  @Override
  public String getDescription() {
    return "Get a list of Player's Mods";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"ModList"};
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/modlist <user>";
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        ChatHelper.sendMessageTo(sender,
            TextFormatting.AQUA + Strings.join(SecurityUtils.getPlayerMods(player), ","));
      } else {
        ChatHelper.sendMessageTo(sender, Local.PLAYER_NOT_FOUND.replaceAll("#", args[0]));
      }
    } else {
      ChatHelper.sendMessageTo(sender, getUsage(sender));
    }
  }
}
