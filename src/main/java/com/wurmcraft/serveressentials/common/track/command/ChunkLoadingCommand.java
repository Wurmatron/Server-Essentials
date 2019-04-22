package com.wurmcraft.serveressentials.common.track.command;

import com.google.common.collect.ImmutableSetMultimap;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

@Command(moduleName = "track") // /cl <player | mod> <name>
public class ChunkLoadingCommand extends SECommand {

  @Override
  public String getName() {
    return "chunkloading";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/cl <player | mod> <name>";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alt = new ArrayList<>();
    alt.add("cl");
    alt.add("CL");
    return alt;
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @SubCommand
  public void player(ICommandSender sender, String[] args) {
    if (args.length == 1) {
      List<Ticket> playerTicker = getPlayerTickets(sender.getEntityWorld(), args[0]);
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
      for (Ticket ticket : playerTicker) {
        sender.sendMessage(displayTicket(ticket));
      }
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  private static ITextComponent displayTicket(Ticket ticket) {
    TextComponentString text =
        new TextComponentString(ticket.getModId() + "_" + ticket.getChunkList().toString());
    ChunkPos firstPos = ticket.getChunkList().asList().get(0);
    Style style = new Style();
    style.setClickEvent(
        new ClickEvent(
            Action.SUGGEST_COMMAND,
            "/tp "
                + (firstPos.getXStart() + 8)
                + " "
                + ticket
                    .world
                    .getTopSolidOrLiquidBlock(
                        new BlockPos(firstPos.getXStart() + 8, 255, firstPos.getZStart() + 8))
                    .getY()
                + (firstPos.getZStart() + 8)) {});
    style.setHoverEvent(
        new HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            new TextComponentString(
                "X: "
                    + (firstPos.getXStart() + 8)
                    + " Z: "
                    + (firstPos.getZStart() + 8)
                    + " Count: "
                    + ticket.getChunkList().size())));
    text.setStyle(style);
    return text;
  }

  private List<Ticket> getPlayerTickets(World world, String name) {
    List<Ticket> temp = new ArrayList<>();
    ImmutableSetMultimap<ChunkPos, Ticket> data = ForgeChunkManager.getPersistentChunksFor(world);
    for (Ticket ticket : data.values()) {
      if (ticket.isPlayerTicket() && ticket.getPlayerName().equalsIgnoreCase(name)) {
        temp.add(ticket);
      }
    }
    return temp;
  }

  private List<Ticket> getModTickets(World world, String name) {
    List<Ticket> temp = new ArrayList<>();
    ImmutableSetMultimap<ChunkPos, Ticket> data = ForgeChunkManager.getPersistentChunksFor(world);
    if (name.equalsIgnoreCase("all")) {
      for (Ticket ticket : data.values()) {
        temp.add(ticket);
      }
      return temp;
    }
    for (Ticket ticket : data.values()) {
      if (!ticket.isPlayerTicket() && ticket.getModId().equals(name)) {
        temp.add(ticket);
      }
    }
    return temp;
  }

  @SubCommand
  public void mod(ICommandSender sender, String[] args) {
    if (args.length == 1) {
      List<Ticket> modTickets = getModTickets(sender.getEntityWorld(), args[0]);
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
      for (Ticket ticket : modTickets) {
        sender.sendMessage(displayTicket(ticket));
      }
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
