package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand
public class DPFCommand extends Command {

  @Override
  public String getName() {
    return "DeletePlayerFile";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/DPF <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_DELETEPLAYERFILE;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        UserManager.setLastLocation(
            player, new LocationWrapper(player.getPosition(), player.dimension));
        player.onKillCommand();
        ((EntityPlayerMP) player)
            .connection.disconnect(
                new TextComponentString(
                    LanguageModule.getUserLanguage(player).local.GENERAL_DELETE_PLAYER_FILE));
        File playerFile =
            new File(
                server.getDataDirectory(),
                File.separator
                    + server.getFolderName()
                    + File.separator
                    + "playerdata"
                    + File.separator
                    + player.getGameProfile().getId().toString()
                    + ".dat");
        ServerEssentialsServer.LOGGER.info(
            "Deleting " + player.getDisplayNameString() + "'s player file");
        try {
          Files.delete(playerFile.toPath());
        } catch (IOException e) {
          ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
        }
        ChatHelper.sendMessage(
            sender,
            senderLang.local.GENERAL_DPF_DELETED.replaceAll(
                Replacment.PLAYER, player.getDisplayNameString()));
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("DPF");
    aliases.add("Dpf");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
