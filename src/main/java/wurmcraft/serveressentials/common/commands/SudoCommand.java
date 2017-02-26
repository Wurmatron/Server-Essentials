package wurmcraft.serveressentials.common.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.reference.Local;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SudoCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "sudo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sudo <name> <command>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            PlayerList players = server.getServer().getPlayerList();
            if (players.getPlayerList().size() > 0) {
                boolean found = false;
                for (EntityPlayerMP victim : players.getPlayerList()) {
                    if (victim.getGameProfile().getId().equals(server.getServer().getPlayerProfileCache().getGameProfileForUsername(args[0]).getId())) {
                        found = true;
                        if (args.length >= 2) {
                            victim.addChatComponentMessage(new TextComponentString(Local.COMMAND_FORCED));
                            String command = Strings.join(Arrays.copyOfRange(args, 1, args.length), " ");
                            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(victim,command);
                            sender.addChatMessage(new TextComponentString(Local.COMMAND_SENDER_FORCED.replaceAll("#",victim.getDisplayName().getUnformattedText()) + "/" + command));
                        } else
                            sender.addChatMessage(new TextComponentString(Local.COMMAND_NOT_FOUND));
                    }
                }
                if (!found)
                    sender.addChatMessage(new TextComponentString(Local.PLAYER_NOT_FOUND.replaceAll("#", args[0])));
            }
        } else
            sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> list = new ArrayList<>();
        if (args.length == 0) {
            if (sender instanceof EntityPlayer)
                Collections.addAll(list, FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
        }
        return list;
    }
}
