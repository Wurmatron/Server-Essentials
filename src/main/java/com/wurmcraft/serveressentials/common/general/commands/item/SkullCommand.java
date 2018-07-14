package com.wurmcraft.serveressentials.common.general.commands.item;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
@Command(moduleName = "General")
public class SkullCommand extends SECommand {

  @Override
  public String getName() {
    return "skull";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player != null) {
        ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag("SkullOwner", new NBTTagString(args[0]));
        player.inventory.addItemStackToInventory(stack);
        player.sendMessage(
            new TextComponentString(getCurrentLanguage(sender).SKULL.replaceAll("#", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }
}
