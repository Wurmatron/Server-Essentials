package com.wurmcraft.serveressentials.common.team.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.rest.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

// TODO Rework Command
@Command(moduleName = "Team")
public class TeamCommand extends SECommand {

  private static String[] formatTeam(Local lang, GlobalTeam team) {
    List<String> lines = new ArrayList<>();
    lines.add(lang.CHAT_SPACER);
    if (team != null) {
      lines.add(
          TextFormatting.LIGHT_PURPLE
              + lang.CHAT_NAME
              + ": "
              + TextFormatting.AQUA
              + team.getName());
      lines.add(
          TextFormatting.LIGHT_PURPLE
              + lang.CHAT_LEADER
              + ": "
              + TextFormatting.AQUA
              + team.getLeader());
      for (String active : ConfigHandler.activeCurrency) {
        lines.add(
            TextFormatting.LIGHT_PURPLE
                + active
                + ": "
                + TextFormatting.GOLD
                + team.getBank().getCurrency(active.replaceAll(" ", "_")));
      }
    }
    lines.add(lang.CHAT_SPACER);
    return lines.toArray(new String[0]);
  }

  @Override
  public String getName() {
    return "seTeam";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/seTeam <name> <info | tpAll>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, getUsage(sender));
    } else {
      if (args.length == 2 && args[1].equalsIgnoreCase("info")) {
        GlobalTeam team = forceTeamFromName(args[0]);
        for (String line : formatTeam(getCurrentLanguage(sender), team)) {
          ChatHelper.sendMessage(sender, line);
        }
      } else {
        ChatHelper.sendMessage(sender, getUsage(sender));
      }
    }
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @SubCommand
  public void tpAll(ICommandSender sender, String[] args) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
        GlobalUser user =
            (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
        if (user.getTeam() != null && user.getTeam().length() > 0) {
          GlobalTeam team = (GlobalTeam) UserManager.getTeam(user.getTeam())[0];
          if (team.getLeader()
              .toString()
              .equalsIgnoreCase(player.getGameProfile().getId().toString())) {
            for (String member : team.getMembers()) {
              for (EntityPlayerMP entityMP :
                  FMLCommonHandler.instance()
                      .getMinecraftServerInstance()
                      .getPlayerList()
                      .getPlayers()) {
                if (entityMP.getGameProfile().getId().toString().equalsIgnoreCase(member)) {
                  TeleportUtils.teleportTo(entityMP, player, true);
                }
              }
            }
          } else {
            ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_LEADER);
          }
        }
      } else {
        // TODO File Support
      }
    }
  }
}
