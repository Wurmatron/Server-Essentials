package wurmcraft.serveressentials.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import wurmcraft.serveressentials.common.api.storage.Warp;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class SetWarpCommand extends EssentialsCommand {

		public SetWarpCommand(String perm) {
				super(perm);
		}

		@Override
		public String getCommandName() {
				return "setwarp";
		}

		@Override
		public String getCommandUsage(ICommandSender sender) {
				return "/setwarp <name>";
		}

		@Override
		public List<String> getCommandAliases() {
				List<String> aliases = new ArrayList<>(); aliases.add("[sS]etWarp"); aliases.add("SWarp"); return aliases;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (sender.getEntityWorld().isRemote) return; if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
						if (args.length != 1) sender.addChatMessage(new TextComponentString(Local.WARP_NAME));
						else {
								EntityPlayerMP      player   = (EntityPlayerMP) sender.getCommandSenderEntity();
								Warp                warp     = new Warp(args[0], player.getPosition(), player.dimension, player.rotationYaw, player.rotationPitch);
								TextComponentString nameWarp = new TextComponentString(DataHelper.createWarp(warp));
								DataHelper.createWarp(warp); nameWarp.getStyle().setHoverEvent(hoverEvent(warp));
								sender.addChatMessage(nameWarp);
						}
				}
		}

		public HoverEvent hoverEvent(Warp home) {
				return new HoverEvent(HoverEvent.Action.SHOW_TEXT, DataHelper.displayLocation(home));
		}
}
