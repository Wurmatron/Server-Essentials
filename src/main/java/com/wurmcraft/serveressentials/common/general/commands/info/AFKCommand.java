package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

@Command(moduleName = "General")
public class AFKCommand extends SECommand {

  public static NonBlockingHashSet<EntityPlayer> afkPlayers = new NonBlockingHashSet<>();

  @Override
  public String getName() {
    return "afk";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A7b/afk";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (afkPlayers.contains(player)) {
      setAFK(player, false);
      sender.sendMessage(
          new TextComponentString(getCurrentLanguage(sender).AFK.replaceAll("&", "\u00A7")));
    } else {
      setAFK(player, true);
      sender.sendMessage(
          new TextComponentString(getCurrentLanguage(sender).NOTAFK.replaceAll("&", "\u00A7")));
    }
  }

  private static void setAFK(EntityPlayer player, boolean afk) {
    if (afk) {
      afkPlayers.add(player);
    } else {
      afkPlayers.remove(player);
    }
    for (EntityPlayer user :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      if (!afk) {
        user.sendMessage(
            new TextComponentString(
                LanguageModule.getLangfromUUID(user.getGameProfile().getId())
                    .AFK_OTHER
                    .replaceAll("%PLAYER%", player.getDisplayNameString())
                    .replaceAll("&", "\u00A7")));
      } else {
        user.sendMessage(
            new TextComponentString(
                LanguageModule.getLangfromUUID(user.getGameProfile().getId())
                    .NOTAFK_OTHER
                    .replaceAll("%PLAYER%", player.getDisplayNameString())
                    .replaceAll("&", "\u00A7")));
      }
    }
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_AFK.replaceAll("&", "\u00A7");
  }
}
