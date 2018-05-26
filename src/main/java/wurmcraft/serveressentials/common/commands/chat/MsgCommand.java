package wurmcraft.serveressentials.common.commands.chat;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.config.ConfigHandler;
import wurmcraft.serveressentials.common.reference.Keys;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import javax.annotation.Nullable;
import java.util.List;

public class MsgCommand extends SECommand {

    public MsgCommand(Perm perm) {
        super(perm);
    }

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/msg <user> <message>";
    }

    @Override
    public String[] getAltNames() {
        return new String[]{"pm"};
    }

    @Override
    public String getDescription() {
        return "Send to message to another player";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        super.execute(server, sender, args);
        if (args.length > 1 && sender != null) {
            EntityPlayer reciv = UsernameResolver.getPlayer(args[0]);
            if (reciv != null) {
                String[] lines = new String[args.length - 1];
                for (int index = 1; index < args.length; index++)
                    lines[index - 1] = args[index];
                String message = Strings.join(lines, " ");
                if (sender.getCommandSenderEntity() != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
                    EntityPlayer entitySender = (EntityPlayer) sender.getCommandSenderEntity();
                    ChatHelper.sendMessageTo(entitySender, reciv, message);
                    sender.sendMessage(new TextComponentString(ConfigHandler.msgFormat.replaceAll(ChatHelper.USERNAME_KEY, TextFormatting.AQUA + "Server").replaceAll(ChatHelper.MESSAGE_KEY, TextFormatting.GRAY + message)));
                    DataHelper2.addTemp(Keys.LAST_MESSAGE, entitySender.getGameProfile().getId(), reciv.getGameProfile().getId(), false);
                } else {
                    ChatHelper.sendMessageTo(sender, reciv, ConfigHandler.msgFormat.replaceAll(ChatHelper.USERNAME_KEY, TextFormatting.AQUA + "Server").replaceAll(ChatHelper.MESSAGE_KEY, TextFormatting.GRAY + message));
                }
            } else
                sender.sendMessage(new TextComponentString(Local.PLAYER_NOT_FOUND.replace("#", args[0])));
        } else
            sender.sendMessage(new TextComponentString(getUsage(sender)));
    }

    @Override
    public boolean canConsoleRun() {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return autoCompleteUsername(args, 0);
    }
}
