package com.wurmcraft.serveressentials.forge.modules.language.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.language.LanguageUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.UUID;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "Language", name = "Reply", aliases = {"R"})
public class ReplyCommand {

  @Command(inputArguments = {CommandArguments.STRING_ARR}, inputNames = {"Msg"})
  public void reply(ICommandSender sender, String[] msg) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "language.reply") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (LanguageUtils.lastMessageTracker
            .containsKey(player.getGameProfile().getId().toString())) {
          String name = UsernameCache.getLastKnownUsername(UUID.fromString(
              LanguageUtils.lastMessageTracker
                  .get(player.getGameProfile().getId().toString())));
          FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()
              .executeCommand(sender, "/msg " + name + " " + Strings
                  .join(msg, " "));
        } else {
          sender.sendMessage(new TextComponentString(
              ERROR_COLOR + PlayerUtils.getUserLanguage(sender).LANGUAGE_REPLY));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
