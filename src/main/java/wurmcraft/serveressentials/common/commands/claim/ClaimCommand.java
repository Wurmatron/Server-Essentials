package wurmcraft.serveressentials.common.commands.claim;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.RegionData;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.TeamManager;

public class ClaimCommand extends SECommand {

	public ClaimCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getName () {
		return "claim";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/claim";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		RegionData regionData = ChunkManager.getRegion (player.getPosition ());
		if (regionData != null) {
			Claim claim = regionData.getClaim (ChunkManager.getIndexForClaim (player.getPosition ()));
			if (claim == null) {
				Team team = TeamManager.getTeamFromLeader (player.getGameProfile ().getId ());
				regionData.addClaim (player.getPosition (),new Claim (team,player.getGameProfile ().getId ()));
				ChunkManager.handleRegionUpdate (ChunkManager.getRegionLocation (player.getPosition ()),regionData);
				ChatHelper.sendMessageTo (sender,Local.CHUNK_CLAIMED);
			} else
				ChatHelper.sendMessageTo (sender,Local.CHUNK_ALREADY_CLAIMED);
		} else {
			RegionData regionDataNew = new RegionData ();
			Team team = TeamManager.getTeamFromLeader (player.getGameProfile ().getId ());
			regionDataNew.addClaim (player.getPosition (),new Claim (team,player.getGameProfile ().getId ()));
			ChunkManager.handleRegionUpdate (ChunkManager.getRegionLocation (player.getPosition ()),regionDataNew);
			ChatHelper.sendMessageTo (sender,Local.CHUNK_CLAIMED);
		}
	}

	@Override
	public boolean canConsoleRun () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Claims a chunk the player is standing in";
	}
}
