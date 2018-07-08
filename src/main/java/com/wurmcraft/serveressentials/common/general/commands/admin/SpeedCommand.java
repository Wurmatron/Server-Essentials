package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class SpeedCommand extends SECommand {

  @Override
  public String getName() {
    return "speed";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length >= 1) {
      double speed = .1;
      try {
        speed = Double.parseDouble(args[0]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[0])));
        return;
      }
      NBTTagCompound tagCompound = new NBTTagCompound();
      player.capabilities.writeCapabilitiesToNBT(tagCompound);
      tagCompound
          .getCompoundTag("abilities")
          .setTag("flySpeed", new NBTTagFloat((float) speed / 20));
      tagCompound
          .getCompoundTag("abilities")
          .setTag("walkSpeed", new NBTTagFloat((float) speed / 10));
      player.capabilities.readCapabilitiesFromNBT(tagCompound);
      player.sendPlayerAbilities();
      player.sendMessage(
          new TextComponentString(
              LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                  .SPEED_CHANGED
                  .replaceAll("%SPEED%", "" + speed)));
    } else {
      NBTTagCompound tagCompound = new NBTTagCompound();
      player.capabilities.writeCapabilitiesToNBT(tagCompound);
      tagCompound.getCompoundTag("abilities").setTag("flySpeed", new NBTTagFloat(0.05F));
      tagCompound.getCompoundTag("abilities").setTag("walkSpeed", new NBTTagFloat(0.1f));
      player.capabilities.readCapabilitiesFromNBT(tagCompound);
      player.sendPlayerAbilities();
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
