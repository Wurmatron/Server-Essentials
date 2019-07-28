package com.wurmcraft.serveressentials.common.modules.general.command;

import com.google.common.collect.ImmutableCollection;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

@ModuleCommand(moduleName = "General")
public class ChunkLoadingCommand extends Command {

  @Override
  public String getName() {
    return "chunkLoading";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/chunkLoading <all | mod | player>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_CHUNKLOADING;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void all(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      ImmutableCollection<Ticket> tickets =
          ForgeChunkManager.getPersistentChunksFor(sender.getEntityWorld()).values();
      for (Ticket ticket : tickets) {
        ChatHelper.sendMessage(
            sender,
            TextFormatting.AQUA
                + "["
                + ticket.getChunkList().asList().get(0).getXStart()
                + ", "
                + ticket.getChunkList().asList().get(0).getZStart()
                + "]");
      }
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void mod(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      String modid = args[0];
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      ImmutableCollection<Ticket> tickets =
          ForgeChunkManager.getPersistentChunksFor(sender.getEntityWorld()).values();
      for (Ticket ticket : tickets) {
        if (ticket.getModId().equalsIgnoreCase(modid)) {
          ChatHelper.sendMessage(
              sender,
              TextFormatting.AQUA
                  + "["
                  + ticket.getChunkList().asList().get(0).getXStart()
                  + ", "
                  + ticket.getChunkList().asList().get(0).getZStart()
                  + "]");
        }
      }
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @SubCommand
  public void player(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 1) {
      String player = args[0];
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      ImmutableCollection<Ticket> tickets =
          ForgeChunkManager.getPersistentChunksFor(sender.getEntityWorld()).values();
      for (Ticket ticket : tickets) {
        if (ticket.getPlayerName() != null && ticket.getPlayerName().equalsIgnoreCase(player)) {
          ChatHelper.sendMessage(
              sender,
              TextFormatting.AQUA
                  + "["
                  + ticket.getChunkList().asList().get(0).getXStart()
                  + ", "
                  + ticket.getChunkList().asList().get(0).getZStart()
                  + "]");
        }
      }
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Loaded");
    aliases.add("Chunks");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 1 && args[0].equalsIgnoreCase("player")) {
      return CommandUtils.predictUsernames(args, 1);
    }
    return Arrays.asList("all", "mod", "player");
  }
}
