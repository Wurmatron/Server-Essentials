package com.wurmcraft.serveressentials.common.claim.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.claim.Claim;
import com.wurmcraft.serveressentials.api.json.claim.RegionData;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.claim.ChunkManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Deprecated
@Command(moduleName = "Claim")
public class DelClaimCommand extends SECommand {

  @Override
  public String getName() {
    return "delClaim";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    RegionData regionData = ChunkManager.getRegion(player.getPosition());
    if (regionData != null) {
      Claim claim = regionData.getClaim(ChunkManager.getIndexForClaim(player.getPosition()));
      if (claim != null && ChunkManager.isOwnerOrLeader(player.getGameProfile().getId(), claim)) {
        regionData.setClaim(ChunkManager.getIndexForClaim(player.getPosition()), null);
        ChunkManager.handleRegionUpdate(
            ChunkManager.getRegionLocation(player.getPosition()), regionData);
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CLAIM_REMOVED);
      }
    } else {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).MISSING_CLAIM);
    }
  }
}
