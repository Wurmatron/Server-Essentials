package wurmcraft.serveressentials.common.commands.claim;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.utils.ChunkHelper;
import wurmcraft.serveressentials.common.utils.ClaimManager;
import wurmcraft.serveressentials.common.utils.RegionClaim;

import java.util.UUID;

public class ClaimCommand extends EssentialsCommand {

		public ClaimCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "claim";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/claim";
		}

		@Override
		public void execute(MinecraftServer server,ICommandSender sender, String[] args) throws CommandException {
				if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
								RegionClaim regionClaim = ClaimManager.loadedClaims.get(ChunkHelper.getRegionLocation(player.getPosition()));
								if(regionClaim != null) {
										Claim claim = regionClaim.getClaim(player.getPosition());
										if (claim == null) {
												regionClaim.addClaim(ChunkHelper.getChunkFromCords(player.getPosition()), new Claim(new Team("test", UUID.randomUUID(), false), player.getGameProfile().getId()));
												player.addChatComponentMessage(new TextComponentString("Chunk Claimed"));
												ClaimManager.saveRegionClaim(player.dimension, player.getPosition(), regionClaim);
												ClaimManager.loadRegionClaim(player.dimension,ChunkHelper.getRegionLocation(player.getPosition()));
										} else
												player.addChatComponentMessage(new TextComponentString("Chunk Already Claimed"));
								} else {
										RegionClaim rClaim = new RegionClaim();
										rClaim.addClaim(ChunkHelper.getChunkFromCords(player.getPosition()), new Claim(new Team("temp", player.getGameProfile().getId(), false), player.getGameProfile().getId()));
										player.addChatComponentMessage(new TextComponentString("Chunk Claimed"));
										ClaimManager.saveRegionClaim(player.dimension, player.getPosition(), rClaim);
										ClaimManager.loadRegionClaim(player.dimension,ChunkHelper.getRegionLocation(player.getPosition()));
								}
				}
		}

		@Override
		public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
				return true;
		}
}
