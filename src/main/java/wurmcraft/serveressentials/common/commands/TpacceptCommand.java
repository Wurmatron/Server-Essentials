package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class TpacceptCommand extends EssentialsCommand {

    public TpacceptCommand(String perm) {
        super(perm);
    }

    @Override
    public String getCommandName() {
        return "tpaccept";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tpaccept";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            if (DataHelper.activeRequests.size() > 0) {
                for (long time : DataHelper.activeRequests.keySet()) {
                    EntityPlayer[] otherPlayer = DataHelper.activeRequests.get(time);
                    if (otherPlayer[1].getGameProfile().getId().equals(player.getGameProfile().getId())) {
                        otherPlayer[0].setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                        otherPlayer[1].addChatComponentMessage(new TextComponentString(Local.TPA_ACCEPED_OTHER));
                        otherPlayer[0].addChatComponentMessage(new TextComponentString(Local.TPA_ACCEPTED.replaceAll("#", otherPlayer[1].getDisplayName().getUnformattedText())));
                        DataHelper.activeRequests.remove(time);
                    }
                }
            }
        }
    }
}
