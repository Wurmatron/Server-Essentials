package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.json.ServerStatus;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.track.TrackModule;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General")
public class ListCommand extends Command {

  @Override
  public String getName() {
    return "List";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/list <server>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_LIST;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      for (ServerStatus status : TrackModule.networkStatus) {
        ChatHelper.sendMessage(sender, TextFormatting.RED + status.name);
        for (String player : status.players) {
          ChatHelper.sendMessage(sender, "  " + TextFormatting.AQUA + player);
        }
      }
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      for (String player :
          FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames()) {
        ChatHelper.sendMessage(sender, "  " + TextFormatting.AQUA + player);
      }
    }
    ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Players");
    return aliases;
  }
}
