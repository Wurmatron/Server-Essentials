package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.reference.Local;

import static wurmcraft.serveressentials.common.utils.DataHelper.activeRequests;

public class TpaCommand extends EssentialsCommand {

    public TpaCommand(String perm) {
        super(perm);
    }

    @Override
    public String getCommandName() {
        return "tpa";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tpa <user>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            if (args.length == 1) {
                PlayerList players = server.getServer().getPlayerList();
                if (players.getPlayerList().size() > 0) {
                    boolean found = false;
                    for (EntityPlayerMP otherPlayer : players.getPlayerList())
                        if (otherPlayer.getGameProfile().getId().equals(server.getServer().getPlayerProfileCache().getGameProfileForUsername(args[0]).getId())) {
                            found = true;
                            if (!activeRequests.values().contains(new EntityPlayer[]{player, otherPlayer})) {
                                activeRequests.put(System.currentTimeMillis(), new EntityPlayer[]{player, otherPlayer});
                                player.addChatComponentMessage(new TextComponentString(Local.TPA_REQUEST_SENT.replaceAll("#", otherPlayer.getDisplayName().getUnformattedText())));
                                otherPlayer.addChatComponentMessage(new TextComponentString(Local.TPA_REQUEST.replaceAll("#", player.getDisplayName().getUnformattedText())));
                            }
                        }
                    if (!found)
                        ((EntityPlayer) sender).addChatComponentMessage(new TextComponentString(Local.TPA_USER_NOTFOUND));
                }
            } else
                ((EntityPlayer) sender).addChatComponentMessage(new TextComponentString(Local.TPA_USERNAME_NONE));
        }
    }
}
