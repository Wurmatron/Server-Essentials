package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.Arrays;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General")
public class SudoCommand extends Command {

  @Override
  public String getName() {
    return "Sudo";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/sudo <user> <command>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SUDO;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        String command = Strings.join(Arrays.copyOfRange(args, 1, args.length), " ");
        sender.sendMessage(
            new TextComponentString(
                senderLang
                    .local
                    .GENERAL_COMMAND_SUDO
                    .replaceAll(Replacment.COMMAND, command)
                    .replaceAll(Replacment.PLAYER, args[0])));
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getCommandManager()
            .executeCommand(player, command);
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }
}
