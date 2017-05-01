package wurmcraft.serveressentials.common.commands.claim;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.RegionData;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;

public class RemoveClaimCommand extends EssentialsCommand {

		public RemoveClaimCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "removeClaim";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/removeclaim";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
						EntityPlayer player     = (EntityPlayer) sender.getCommandSenderEntity();
						RegionData   regionData = ChunkManager.getRegion(player.getPosition()); if (regionData != null) {
								Claim claim = regionData.getClaim(ChunkManager.getIndexForClaim(player.getPosition())); if (claim != null) {
										regionData.setClaim(ChunkManager.getIndexForClaim(player.getPosition()), null);
										ChunkManager.handleRegionUpdate(ChunkManager.getRegionLocation(player.getPosition()), regionData);
										player.addChatComponentMessage(new TextComponentString("Claim Removed"));
								}
						}
				}
		}

		@Override
		public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
				return true;
		}
}
