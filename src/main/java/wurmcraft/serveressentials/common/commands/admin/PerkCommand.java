package wurmcraft.serveressentials.common.commands.admin;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

public class PerkCommand extends SECommand {

  public PerkCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "perk";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/perk <username> (maxHome,keepInv,vault) <val>";
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription() {
    return "Adds a Perk to a player";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 3) {
      if (UsernameResolver.isValidPlayer(args[0])) {
        EntityPlayer player = UsernameResolver.getPlayer(args[0]);
        String perk = args[1];
        PlayerData playerData = (PlayerData) DataHelper2
            .get(Keys.PLAYER_DATA, player.getGameProfile().getId().toString());
        try {
          int val = Integer.parseInt(args[2]);
          if (perk.equalsIgnoreCase("maxHome")) {
            playerData.setMaxHomes(val);
            DataHelper2.forceSave(Keys.PLAYER_DATA, playerData);
            ChatHelper.sendMessageTo(sender, Local.PERK_CHANGED.replaceAll("#", "maxHome")
                .replaceAll("@", player.getDisplayNameString()));
          } else if (perk.equalsIgnoreCase("vault")) {
            playerData.setVaultSlots(val);
            DataHelper2.forceSave(Keys.PLAYER_DATA, playerData);
            ChatHelper.sendMessageTo(sender, Local.PERK_CHANGED.replaceAll("#", "vault")
                .replaceAll("@", player.getDisplayNameString()));
          }
        } catch (NumberFormatException e) {
          ChatHelper.sendMessageTo(sender, Local.INVALID_NUMBER.replaceAll("#", args[2]));
        }
        if (perk.equalsIgnoreCase("keepInv")) {
          playerData.addCustomData("perk.keepInv");
          DataHelper2.forceSave(Keys.PLAYER_DATA, playerData);
          ChatHelper.sendMessageTo(sender, Local.PERK_CHANGED.replaceAll("#", "keepInv")
              .replaceAll("@", player.getDisplayNameString()));
        }
      }
    } else {
      ChatHelper.sendMessageTo(sender, getUsage(sender));
    }
  }
}
