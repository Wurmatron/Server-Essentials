package wurmcraft.serveressentials.common.commands.admin;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.api.storage.Kit;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class KitAdminCommand extends SECommand {

  public KitAdminCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "kitAdmin";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/kitAdmin create <name> <time> | /kitAdmin remove <name>";
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Used to easily create or remove a kit";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length >= 2) {
      if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("new")) {
        if (!args[1].equalsIgnoreCase("list")) {
          List<ItemStack> kitItems = new ArrayList<>();
          for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null) {
              kitItems.add(stack);
            }
          }
          if (kitItems.size() > 0) {
            try {
              int time = Integer.valueOf(args[2]);
              if (time > 0 && args.length == 3) {
                Kit kit = new Kit(args[1], kitItems.toArray(new ItemStack[0]), time);
                DataHelper2.forceSave(Keys.KIT, kit);
                ChatHelper.sendMessageTo(player, Local.KIT_CREATED.replaceAll("#", kit.getName()));
              }
            } catch (NumberFormatException e) {
              ChatHelper.sendMessageTo(player, Local.INVALID_NUMBER.replaceAll("#", args[2]));
            }
          }
        } else {
          ChatHelper.sendMessageTo(player, Local.INVALID_KIT_NAME.replaceAll("#", args[1]));
        }
      } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem") || args[0]
          .equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
        boolean found = false;
        for (IDataType kit : DataHelper2.getData(Keys.KIT)) {
          if (kit.getID().equalsIgnoreCase(args[1])) {
            found = true;
            DataHelper2.delete(Keys.KIT, kit);
            ChatHelper.sendMessageTo(player, Local.KIT_REMOVED.replaceAll("#", kit.getID()));
            break;
          }
        }
        if (!found) {
          ChatHelper.sendMessageTo(player, Local.KIT_NOTFOUND.replaceAll("#", args[1]));
        }
      } else {
        ChatHelper.sendMessageTo(sender, getUsage(player));
      }
    } else {
      ChatHelper.sendMessageTo(sender, getUsage(player));
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      String[] args, @Nullable BlockPos pos) {
    return autoComplete(args, DataHelper2.getData(Keys.KIT), 1);
  }
}
