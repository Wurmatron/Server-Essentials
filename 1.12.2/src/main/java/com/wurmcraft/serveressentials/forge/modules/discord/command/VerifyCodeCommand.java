package com.wurmcraft.serveressentials.forge.modules.discord.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.Token;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.config.DiscordConfig;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Discord", name = "VerifyCode")
public class VerifyCodeCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Code"})
  public void verifyCode(ICommandSender sender, String verifyCode) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      Token[] tokens = RestRequestGenerator.Discord.getTokens();
      if (tokens != null && tokens.length > 0) {
        for (Token token : tokens) {
          if (token.token.equals(verifyCode)) {
            sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
                .getUserLanguage(sender).DISCORD_VERIFIED));
            GlobalPlayer globalData = RestRequestGenerator.User
                .getPlayer(player.getGameProfile().getId().toString());
            globalData.discordID = token.id;
            RestRequestGenerator.User
                .overridePlayer(player.getGameProfile().getId().toString(), globalData);
            try {
              Rank rank = (Rank) SERegistry.getStoredData(DataKey.RANK,
                  ((DiscordConfig) SERegistry
                      .getStoredData(DataKey.MODULE_CONFIG, "Discord")).verifiedRank);
              RankUtils.setRank(player, rank);
            } catch (NoSuchElementException e) {
              e.printStackTrace();
            }
            return;
          }
        }
      }
      sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
          .getUserLanguage(sender).DISCORD_VERIFIED));
    }
  }

}
