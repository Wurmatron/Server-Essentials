package wurmcraft.serveressentials.common.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends EssentialsCommand {

    public WarpCommand(String perm) {
        super(perm);
    }

    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/warp <name>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getEntityWorld().isRemote)
            return;
        if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
            if (args.length == 0)
                execute(server,sender,new String[] {"list"});
            else {
                if (args[0].equalsIgnoreCase("list")) {
                    List<String> warps = new ArrayList<>();
                    for (Warp warp : DataHelper.getWarps())
                        warps.add(warp.getName());
                    sender.addChatMessage(new TextComponentString(TextFormatting.AQUA + Strings.join(warps, ", ")));
                } else if (DataHelper.getWarp(args[0]) != null) {
                    Warp warp = DataHelper.getWarp(args[0]);
                    EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
                    long teleport_timer = DataHelper.getPlayerData(player.getGameProfile().getId()).getTeleport_timer();
                    if (teleport_timer + (Settings.teleport_cooldown * 1000) <= System.currentTimeMillis()) {
                        DataHelper.updateTeleportTimer(player.getGameProfile().getId());
                        TextComponentString text = new TextComponentString(Local.WARP_TELEPORT.replaceAll("#", warp.getName()));
                        text.getStyle().setHoverEvent(hoverEvent(warp));
                        player.addChatComponentMessage(text);
                        player.setLocationAndAngles(warp.getPos().getX(), warp.getPos().getY(), warp.getPos().getZ(), warp.getYaw(), warp.getPitch());
                    } else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis())
                        sender.addChatMessage(new TextComponentString(Local.TELEPORT_COOLDOWN.replace("#", Integer.toString(Math.round((System.currentTimeMillis() - teleport_timer))))));
                } else
                    sender.addChatMessage(new TextComponentString(Local.WARP_NONE.replaceAll("#", args[0])));
            }
        }
    }

    public HoverEvent hoverEvent(Warp warp) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, DataHelper.displayLocation(warp));
    }
}
