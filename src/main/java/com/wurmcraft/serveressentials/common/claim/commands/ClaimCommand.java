package com.wurmcraft.serveressentials.common.claim.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.claim.Claim;
import com.wurmcraft.serveressentials.api.json.claim.RegionData;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.api.json.user.team.fileOnly.Team;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.claim.ChunkManager;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Deprecated
@Command(moduleName = "Claim")
public class ClaimCommand extends SECommand {

  private static ITeam getTeamFromUser(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return (Team) UserManager.getTeam(data.getTeam())[0];
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      return (GlobalTeam) UserManager.getTeam(user.getTeam())[0];
    }
    return null;
  }

  @Override
  public String getName() {
    return "claim";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    RegionData regionData = ChunkManager.getRegion(player.getPosition());
    if (regionData != null) {
      Claim claim = regionData.getClaim(ChunkManager.getIndexForClaim(player.getPosition()));
      if (claim == null) {
        ITeam team = getTeamFromUser(player.getGameProfile().getId());
        regionData.addClaim(player.getPosition(), new Claim(team, player.getGameProfile().getId()));
        ChunkManager.handleRegionUpdate(
            ChunkManager.getRegionLocation(player.getPosition()), regionData);
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHUNK_CLAIMED);
      } else {
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHUNK_ALREADY_CLAIMED);
      }
    } else {
      RegionData regionDataNew = new RegionData();
      ITeam team = getTeamFromUser(player.getGameProfile().getId());
      regionDataNew.addClaim(
          player.getPosition(), new Claim(team, player.getGameProfile().getId()));
      ChunkManager.handleRegionUpdate(
          ChunkManager.getRegionLocation(player.getPosition()), regionDataNew);
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHUNK_CLAIMED);
    }
  }
}
