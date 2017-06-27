package wurmcraft.serveressentials.common.commands.claim;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.RegionData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;

public class RemoveClaimCommand extends EssentialsCommand {

	public RemoveClaimCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "removeClaim";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/removeclaim";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		RegionData regionData = ChunkManager.getRegion (player.getPosition ());
		if (regionData != null) {
			Claim claim = regionData.getClaim (ChunkManager.getIndexForClaim (player.getPosition ()));
			if (claim != null && ChunkManager.isOwnerOrLeader (player.getGameProfile ().getId (),claim)) {
				regionData.setClaim (ChunkManager.getIndexForClaim (player.getPosition ()),null);
				ChunkManager.handleRegionUpdate (ChunkManager.getRegionLocation (player.getPosition ()),regionData);
				ChatHelper.sendMessageTo (player,Local.CLAIM_REMOVED);
			}
		} else
			ChatHelper.sendMessageTo (player, Local.MISSING_CLAIM);
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Deletes a claim in a the chunk you are standing in";
	}
}
