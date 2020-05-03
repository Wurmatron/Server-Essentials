package com.wurmcraft.serveressentials.forge.modules.general.command.player;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Skull")
public class SkullCommand {

  @Command(inputArguments = {CommandArguments.PLAYER})
  public void createSkull(ICommandSender sender, EntityPlayer player) {
    createSkull(sender, player.getDisplayNameString());
  }

  @Command(inputArguments = {CommandArguments.STRING})
  public void createSkull(ICommandSender sender, String player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.skull") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer senderPlayer = (EntityPlayer) sender.getCommandSenderEntity();
        ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("SkullOwner", new NBTTagString(player));
        senderPlayer.inventory.addItemStackToInventory(stack);
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(senderPlayer).GENERAL_SKULL
                .replaceAll("%PLAYER%", COMMAND_INFO_COLOR + player)));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }


}
