package wurmcraft.serveressentials.common.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HomeCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("Home");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getEntityWorld().isRemote)
            return;
        if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
            if (args.length == 0) {
                Home home = DataHelper.getPlayerData(player.getGameProfile().getId()).getHome(Settings.home_name);
                long teleport_timer = DataHelper.getPlayerData(player.getGameProfile().getId()).getTeleport_timer();
                if (home != null && (teleport_timer + (Settings.teleport_cooldown * 1000)) <= System.currentTimeMillis()) {
                    player.setLocationAndAngles(home.getPos().getX(), home.getPos().getY(), home.getPos().getZ(), player.rotationYaw, player.rotationPitch);
                    DataHelper.updateTeleportTimer(player.getGameProfile().getId());
                    sender.addChatMessage(new TextComponentString(Local.HOME_TELEPORTED.replace("#", home.getName())));
                } else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis())
                    sender.addChatMessage(new TextComponentString(Local.TELEPORT_COOLDOWN.replace("#", Long.toString((System.currentTimeMillis() - teleport_timer)))));
                else
                    sender.addChatMessage(new TextComponentString(Local.HOME_NONE));
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    PlayerData data = DataHelper.getPlayerData(player.getGameProfile().getId());
                    if (data == null)
                        DataHelper.reloadPlayerData(player.getGameProfile().getId());
                    if (data.getHomes().length > 0) {
                        ArrayList<String> homes = new ArrayList<>();
                        for (Home h : data.getHomes())
                            if (h != null)
                                homes.add(h.getName());
                        if (homes.size() > 0)
                            sender.addChatMessage(new TextComponentString(TextFormatting.AQUA + Strings.join(homes.toArray(new String[0]), ", ")));
                        else
                            sender.addChatMessage(new TextComponentString(Local.HOME_NONEXISTENT));
                    } else
                        sender.addChatMessage(new TextComponentString(Local.HOME_NONEXISTENT));
                } else {
                    Home home = DataHelper.getPlayerData(player.getGameProfile().getId()).getHome(args[0]);
                    long teleport_timer = DataHelper.getPlayerData(player.getGameProfile().getId()).getTeleport_timer();
                    if (home != null && (teleport_timer + (Settings.teleport_cooldown * 1000)) <= System.currentTimeMillis()) {
                        player.setLocationAndAngles(home.getPos().getX(), home.getPos().getY(), home.getPos().getZ(), player.rotationYaw, player.rotationPitch);
                        sender.addChatMessage(new TextComponentString(Local.HOME_TELEPORTED.replace("#", home.getName())));
                        DataHelper.updateTeleportTimer(player.getGameProfile().getId());
                    } else if ((teleport_timer + (Settings.teleport_cooldown * 1000)) > System.currentTimeMillis())
                        sender.addChatMessage(new TextComponentString(Local.TELEPORT_COOLDOWN.replace("#", Long.toString((System.currentTimeMillis() - teleport_timer)))));
                    else
                        sender.addChatMessage(new TextComponentTranslation(Local.HOME_INVALID.replace("#", args[0])));
                }
            }
        } else
            sender.addChatMessage(new TextComponentString("Command can only be run by players!"));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
