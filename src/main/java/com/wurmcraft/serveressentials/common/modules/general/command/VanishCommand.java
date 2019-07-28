package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.general.event.GeneralEvents;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General")
public class VanishCommand extends Command {

  @Override
  public String getName() {
    return "vanish";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/vanish <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_VANISH;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (GeneralEvents.vanishedPlayers.contains(player)) {
        GeneralEvents.vanishedPlayers.remove(player);
        updatePlayer(player, true);
        ChatHelper.sendMessage(sender, senderLang.local.GENERAL_UNVANISH);
      } else {
        GeneralEvents.vanishedPlayers.add(player);
        updatePlayer(player, false);
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getWorld(player.dimension)
            .getEntityTracker()
            .untrack(player);
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getWorld(player.dimension)
            .getEntityTracker()
            .getTrackingPlayers(player)
            .forEach(
                tp -> {
                  ((EntityPlayerMP) player).connection.sendPacket(new SPacketSpawnPlayer(tp));
                });
        ChatHelper.sendMessage(sender, senderLang.local.GENERAL_VANISH);
      }
    } else if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        if (GeneralEvents.vanishedPlayers.contains(player)) {
          GeneralEvents.vanishedPlayers.remove(player);
          updatePlayer(player, true);
          ChatHelper.sendMessage(
              sender,
              senderLang.local.GENERAL_UNVANISH_OTHER.replaceAll(
                  Replacment.PLAYER, player.getDisplayNameString()));
          ChatHelper.sendMessage(
              player, LanguageModule.getUserLanguage(player).local.GENERAL_UNVANISH);
        } else {
          updatePlayer(player, false);
          GeneralEvents.vanishedPlayers.add(player);
          ChatHelper.sendMessage(
              sender,
              senderLang.local.GENERAL_VANISH_OTHER.replaceAll(
                  Replacment.PLAYER, player.getDisplayNameString()));
          ChatHelper.sendMessage(
              player, LanguageModule.getUserLanguage(player).local.GENERAL_VANISH);
        }
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("V");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }

  public static void updatePlayer(EntityPlayer player, boolean track) {
    if (track) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getWorld(player.dimension)
          .getEntityTracker()
          .track(player);
    } else {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getWorld(player.dimension)
          .getEntityTracker()
          .untrack(player);
    }
    FMLCommonHandler.instance()
        .getMinecraftServerInstance()
        .getWorld(player.dimension)
        .getEntityTracker()
        .getTrackingPlayers(player)
        .forEach(
            tp -> {
              ((EntityPlayerMP) player).connection.sendPacket(new SPacketSpawnPlayer(tp));
            });
  }
}
