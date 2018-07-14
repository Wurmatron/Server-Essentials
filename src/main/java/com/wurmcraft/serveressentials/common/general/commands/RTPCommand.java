package com.wurmcraft.serveressentials.common.general.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import java.util.Random;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fluids.BlockFluidBase;

// TODO Rework Command
@Command(moduleName = "General")
public class RTPCommand extends SECommand {

  private static Random rand = new Random(System.currentTimeMillis());

  private static boolean validLocation(World world, BlockPos pos) {
    if (world.getBlockState(pos).getBlock() != Blocks.AIR
        || world.getBlockState(pos.up()).getBlock() != Blocks.AIR) {
      return false;
    }
    if (world.getBiome(pos.down()).topBlock.getBlock() instanceof BlockFluidBase) {
      return false;
    }
    return true;
  }

  @Override
  public String getName() {
    return "rtp";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    BlockPos teleportPos = null;
    while (teleportPos == null) {
      BlockPos randPos = getRandomPos(player);
      if (validLocation(player.world, randPos)) {
        teleportPos = randPos;
      }
    }
    TeleportUtils.teleportTo(
        (EntityPlayerMP) player, new LocationWrapper(teleportPos, player.dimension), true);
    player.sendMessage(
        new TextComponentString(getCurrentLanguage(sender).RTP.replaceAll("&", "\u00A7")));
  }

  private int flipChance(int no) {
    if (rand.nextInt(10) % 2 == 0) {
      return no;
    } else {
      return -no;
    }
  }

  private BlockPos getRandomPos(EntityPlayer player) {
    WorldBorder border = player.world.getWorldBorder();
    double maxLocationX = (border.getDiameter() / 2) + border.getCenterX();
    double maxLocationZ = (border.getDiameter() / 2) + border.getCenterZ();
    int x = rand.nextInt((int) maxLocationX);
    int z = rand.nextInt((int) maxLocationZ);
    return player.world.getTopSolidOrLiquidBlock(new BlockPos(flipChance(x), 500, flipChance(z)));
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A7/rtp";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_RTP.replaceAll("&", "\u00A7");
  }
}
