package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class SmiteCommand extends SECommand {

  @Override
  public String getName() {
    return "smite";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return super.getUsage(sender);
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("lightning");
    alts.add("s");
    return alts;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0 && sender instanceof EntityPlayer) {
      sender
          .getEntityWorld()
          .addWeatherEffect(
              new EntityLightningBolt(
                  sender.getEntityWorld(),
                  sender.getPosition().getX(),
                  sender.getPosition().getY(),
                  sender.getPosition().getZ(),
                  false));
    } else if (args.length == 0) {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).PLAYER_ONLY);
    } else if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        player
            .getEntityWorld()
            .addWeatherEffect(
                new EntityLightningBolt(
                    player.getEntityWorld(),
                    player.getPosition().getX(),
                    player.getPosition().getY(),
                    player.getPosition().getZ(),
                    false));
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_SMITE;
  }
}
