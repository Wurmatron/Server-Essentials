package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.api.storage.Global;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.Arrays;

public class MotdCommand extends EssentialsCommand {

    public MotdCommand(String perm) {
        super(perm);
    }

    private int LIST_SIZE = 7;

    @Override
    public String getCommandName() {
        return "motd";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "motd";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Global globalSettings = DataHelper.globalSettings;
        if (globalSettings.getMotd() != null && globalSettings.getMotd().length > 0) {
            if (globalSettings.getMotd().length > LIST_SIZE) {
                if (args.length == 0) {
                    String[] temp = Arrays.copyOfRange(globalSettings.getMotd(), 0, LIST_SIZE);
                    for (String rule : temp)
                        sender.addChatMessage(new TextComponentString(rule));
                } else {
                    int pageNo = Integer.valueOf(args[0]);
                    if (pageNo <= (globalSettings.getMotd().length / LIST_SIZE)) {
                        String[] temp = Arrays.copyOfRange(globalSettings.getMotd(), (pageNo * LIST_SIZE), (pageNo * LIST_SIZE) + LIST_SIZE);
                        for (String rule : temp)
                            if (rule != null && rule.length() > 0)
                                sender.addChatMessage(new TextComponentString(rule));
                    } else
                        sender.addChatMessage(new TextComponentString(Local.PAGE_NONE.replaceAll("#", args[0]).replaceAll("$", (globalSettings.getMotd().length / LIST_SIZE) + "")));
                }
            } else
                for (String rule : globalSettings.getMotd())
                    sender.addChatMessage(new TextComponentString(rule));
        } else
            sender.addChatMessage(new TextComponentString(Local.NO_MOTD));
    }
}
