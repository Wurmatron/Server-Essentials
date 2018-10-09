package com.wurmcraft.serveressentials.common.teleport.commands.user;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.global.Warp;
import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Teleportation")
public class WarpCommand extends SECommand {

  @Override
  public String getName() {
    return "warp";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1) {
      DataType warp = DataHelper.get(Keys.WARP, args[0]);
      if (warp != null) {
        TeleportUtils.teleportTo((EntityPlayerMP) player, ((Warp) warp).getPos(), true);
        ChatHelper.sendMessage(player, getCurrentLanguage(sender).TP.replaceAll("%NAME%", args[0]));
      } else {
        ChatHelper.sendMessage(
            sender, TextFormatting.LIGHT_PURPLE + Strings.join(listWarps(), ", "));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return Arrays.asList(listWarps());
  }

  private String[] listWarps() {
    List<String> list = new ArrayList<>();
    for (DataType warp : DataHelper.getData(Keys.WARP)) {
      list.add(((Warp) warp).getName());
    }
    return list.toArray(new String[0]);
  }
}
