package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;
import wurmcraft.serveressentials.common.utils.LogHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SetHomeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "setHome";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sethome";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("sethome");
        aliases.add("sHome");
        aliases.add("SetHome");
        aliases.add("shome");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getEntityWorld().isRemote)
            return;
        if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
            if (args != null && args.length > 0) {
                sender.addChatMessage(new TextComponentString(DataHelper.addPlayerHome(player.getGameProfile().getId(), new Home(args[0], player.getPosition()))));
            } else
                sender.addChatMessage(new TextComponentString(DataHelper.addPlayerHome(player.getGameProfile().getId(), new Home(Settings.default_home_name, player.getPosition()))));
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
