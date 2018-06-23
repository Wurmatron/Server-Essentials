package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class ReplyCommand extends SECommand {

  public ReplyCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "reply";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/reply <message>";
  }

  @Override
  public String getDescription() {
    return "Sends a message to the last known person";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"r"};
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (DataHelper2.getTemp(Keys.LAST_MESSAGE, player.getGameProfile().getId(),
        player.getGameProfile().getId()) != null) {
      for (EntityPlayer entity : server.getPlayerList().getPlayers()) {
        if (entity.getGameProfile().getId().equals(DataHelper2
            .getTemp(Keys.LAST_MESSAGE, player.getGameProfile().getId(),
                player.getGameProfile().getId()))) {
          FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()
              .executeCommand(sender,
                  "/msg " + entity.getDisplayNameString() + " " + Strings.join(args, " "));
        }
      }
    } else {
      ChatHelper.sendMessageTo(player, getDescription());
    }
  }
}
