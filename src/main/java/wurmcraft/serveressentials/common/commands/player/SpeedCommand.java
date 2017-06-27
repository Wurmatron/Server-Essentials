package wurmcraft.serveressentials.common.commands.player;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;

public class SpeedCommand extends EssentialsCommand {

	public SpeedCommand (Perm perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "speed";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/speed <speed>";
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length >= 1) {
			double speed = Double.parseDouble (args[0]);
			NBTTagCompound tagCompound = new NBTTagCompound ();
			player.capabilities.writeCapabilitiesToNBT (tagCompound);
			tagCompound.getCompoundTag ("abilities").setTag ("flySpeed",new NBTTagFloat ((float) speed));
			tagCompound.getCompoundTag ("abilities").setTag ("walkSpeed",new NBTTagFloat ((float) speed));
			player.capabilities.readCapabilitiesFromNBT (tagCompound);
			player.sendPlayerAbilities ();
			ChatHelper.sendMessageTo (sender,Local.SPEED_CHANGED.replaceAll ("#","" + speed));
		} else  {
			NBTTagCompound tagCompound = new NBTTagCompound ();
			player.capabilities.writeCapabilitiesToNBT (tagCompound);
			tagCompound.getCompoundTag ("abilities").setTag ("flySpeed",new NBTTagFloat (0.05F));
			tagCompound.getCompoundTag ("abilities").setTag ("walkSpeed",new NBTTagFloat (0.1f));
			player.capabilities.readCapabilitiesFromNBT (tagCompound);
			player.sendPlayerAbilities ();
		}
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Changes the players walk and flying speed";
	}
}
