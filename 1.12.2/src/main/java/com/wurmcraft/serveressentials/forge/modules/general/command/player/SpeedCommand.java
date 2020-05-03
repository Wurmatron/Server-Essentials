package com.wurmcraft.serveressentials.forge.modules.general.command.player;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Speed")
public class SpeedCommand {

  @Command(inputArguments = {CommandArguments.DOUBLE}, inputNames = {"Speed"})
  public void setSelfSpeed(ICommandSender sender, double speed) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      String type = "";
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player.capabilities.isFlying) {
        type = "Fly";
      } else {
        type = "Walk";
      }
      setSelfSpeed(sender, speed, type);
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER}, inputNames = {"Speed"})
  public void setSelfSpeed(ICommandSender sender, int speed) {
    setSelfSpeed(sender, (double) speed);
  }

  @Command(inputArguments = {CommandArguments.DOUBLE,
      CommandArguments.STRING}, inputNames = {"Speed", "Fly, Walk"})
  public void setSelfSpeed(ICommandSender sender, double speed, String type) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.speed") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.getCompoundTag("abilities")
            .setTag(type.toLowerCase() + "Speed", new NBTTagFloat((float) speed / 10));
        player.capabilities.readCapabilitiesFromNBT(tagCompound);
        player.sendPlayerAbilities();
        sender.sendMessage(new TextComponentString(
            PlayerUtils.getUserLanguage(player).GENERAL_SPEED_SET
                .replaceAll("%TYPE%", type.toUpperCase())
                .replaceAll("%SPEED%", "" + speed)));
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER,
      CommandArguments.STRING}, inputNames = {"Speed", "Fly, Walk"})
  public void setSelfSpeed(ICommandSender sender, int speed, String type) {
    setSelfSpeed(sender, (double) speed, type);
  }

  @Command(inputArguments = {})
  public void setDefaultSpeed(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      setSelfSpeed(sender,1,"walk");
      setSelfSpeed(sender,1,"fly");
    }
  }
}
