package com.wurmcraft.serveressentials.common.general.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import java.util.Random;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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
    return "fire";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    BlockPos teleportPos = null;
    while (teleportPos == null) {
      BlockPos randPos = getRandomPos(player);
      if (validLocation(player.world, randPos) && safeTeleport(player,
          new LocationWrapper(randPos, player.dimension))) {
        teleportPos = randPos;
      }
    }
    TeleportUtils.teleportTo(
        (EntityPlayerMP) player, new LocationWrapper(teleportPos, player.dimension), true);
    player.addPotionEffect(new PotionEffect(Potion.getPotionById(22), 20, 4));
    ChatHelper.sendMessage(sender, getCurrentLanguage(sender).RTP.replaceAll("&", "\u00A7"));
  }

  private boolean safeTeleport(EntityPlayer player, LocationWrapper loc) {
    boolean quick = TeleportUtils.safeToTeleport(player, loc);
    if (!quick) {
      for (int x = 0; x < 4; x++) {
        for (int z = 0; z < 4; z++) {
          for (int f = 0; f < 2; f++) {
            if (!TeleportUtils.safeToTeleport(player,
                new LocationWrapper(flipChance(loc.getX() + x), loc.getY(),
                    flipChance(loc.getZ() + z), loc.getDim()))) {
              return false;
            }

          }
        }
      }
    }
    return quick;
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
    return player.world.getTopSolidOrLiquidBlock(new BlockPos(
        flipChance(rand.nextInt((int) ((border.getDiameter() / 2) + border.getCenterX()))), 255,
        flipChance(rand.nextInt((int) ((border.getDiameter() / 2) + border.getCenterZ())))))
        .add(0, 3, 0);
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

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
