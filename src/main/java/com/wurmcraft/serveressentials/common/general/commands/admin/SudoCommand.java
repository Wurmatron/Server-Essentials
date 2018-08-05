package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

// TODO Rework Command
@Command(moduleName = "General")
public class SudoCommand extends SECommand {

  @Override
  public String getName() {
    return "sudo";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      EntityPlayer victim = UsernameResolver.getPlayer(args[0]);
      if (victim != null) {
        if (args.length >= 2) {
          String command = Strings.join(Arrays.copyOfRange(args, 1, args.length), " ");
          victim.sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(victim.getGameProfile().getId())
                      .COMMAND_FORCED
                      .replaceAll("%COMMAND%", command)));
          FMLCommonHandler.instance()
              .getMinecraftServerInstance()
              .getCommandManager()
              .executeCommand(victim, command);
          ChatHelper.sendMessage(
              sender,
              getCurrentLanguage(sender)
                  .COMMAND_SENDER_FORCED
                  .replaceAll("%PLAYER%", victim.getDisplayNameString())
                  .replaceAll("%COMMAND%", command));
        } else {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).COMMAND_NOT_FOUND.replaceAll("%COMMAND%", "/"));
        }
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return autoCompleteUsername(args, 0);
  }
}
