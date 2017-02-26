package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import wurmcraft.serveressentials.common.commands.utils.PlayerInventory;
import wurmcraft.serveressentials.common.reference.Local;

import java.util.ArrayList;
import java.util.List;

public class EnderChestCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "echest";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/echest <name>";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("enderchest");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            if (args.length == 0)
                ((EntityPlayer) sender).addChatComponentMessage(new TextComponentString(getCommandUsage(sender)));
            if (args.length == 1) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                PlayerList players = server.getServer().getPlayerList();
                if (players.getPlayerList().size() > 0) {
                    boolean open = false;
                    for (EntityPlayerMP victim : players.getPlayerList())
                        if (victim.getGameProfile().getId().equals(server.getServer().getPlayerProfileCache().getGameProfileForUsername(args[0]).getId())) {
                            if (player.openContainer != player.inventoryContainer)
                                player.closeScreen();
                            player.displayGUIChest(new PlayerInventory(victim, player, true));
                            player.addChatComponentMessage(new TextComponentString(Local.PLAYER_INVENTORY_ENDER.replaceAll("#", victim.getDisplayName().getUnformattedText())));
                            open = true;
                        }
                    if (!open)
                        player.addChatComponentMessage(new TextComponentString(Local.PLAYER_NOT_FOUND.replaceAll("#", args[0])));
                }
            }
        }
    }
}
