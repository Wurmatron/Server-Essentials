package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;

public class PingCommand extends SECommand {

  public PingCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "ping";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/ping";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    ChatHelper.sendMessageTo(sender, Local.PING_REPLY);
  }

  @Override
  public String getDescription() {
    return "Displays a message from the server";
  }
}
