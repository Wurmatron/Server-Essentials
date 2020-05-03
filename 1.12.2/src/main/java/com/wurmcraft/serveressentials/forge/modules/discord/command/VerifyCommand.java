package com.wurmcraft.serveressentials.forge.modules.discord.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.Token;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Discord", name = "Verify", aliases = {"V"})
public class VerifyCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Code"})
  public void verify(ICommandSender sender, String code) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "discord.verify") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() != null && sender
          .getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        try {
          Token[] tokens = RestRequestGenerator.Discord.getTokens();
          if (tokens != null && tokens.length > 0) {
            for (Token t : tokens) {
              if (code.equals(t.token)) {
                sender.sendMessage(new TextComponentString(
                    COMMAND_COLOR + PlayerUtils
                        .getUserLanguage(player).DISCORD_VERIFIED));
                SECore.executors.schedule(() -> {
                  GlobalPlayer globalData = RestRequestGenerator.User
                      .getPlayer(player.getGameProfile().getId().toString());
                  globalData.discordID = t.id;
                  RestRequestGenerator.User
                      .overridePlayer(player.getGameProfile().getId().toString(),
                          globalData);
                }, 250, TimeUnit.MILLISECONDS);
                return;
              }
            }
          }
        } catch (NoSuchElementException ignored) {
        }
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(player).DISCORD_INVALID));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
