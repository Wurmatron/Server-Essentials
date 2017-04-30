package wurmcraft.serveressentials.common.commands.claim;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.api.storage.RegionData;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;

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
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
						EntityPlayer player     = (EntityPlayer) sender.getCommandSenderEntity();
						RegionData   regionData = ChunkManager.getRegion(player.getPosition()); if (regionData != null) {
								Claim claim = regionData.getClaim(ChunkManager.getIndexForClaim(player.getPosition())); if (claim == null) {
										regionData.addClaim(player.getPosition(), new Claim(new Team("test", UUID.randomUUID(), false), player.getGameProfile().getId()));
										ChunkManager.handleRegionUpdate(ChunkManager.getRegionLocation(player.getPosition()), regionData);
										player.addChatComponentMessage(new TextComponentString("Chunk Claimed"));
								} else player.addChatComponentMessage(new TextComponentString("Chunk Already Claimed"));
						} else {
								RegionData regionDataNew = new RegionData();
								regionDataNew.addClaim(player.getPosition(), new Claim(new Team("test", UUID.randomUUID(), false), player.getGameProfile().getId()));
								ChunkManager.handleRegionUpdate(ChunkManager.getRegionLocation(player.getPosition()), regionDataNew);
								player.addChatComponentMessage(new TextComponentString("Chunk Claimed"));
						}
				}
		}

		@Override
		public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
				return true;
		}
}
