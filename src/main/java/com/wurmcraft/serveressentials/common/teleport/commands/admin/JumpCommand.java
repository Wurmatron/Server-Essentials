package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

@Command(moduleName = "Teleportation")
public class JumpCommand extends SECommand {

  @Override
  public String getName() {
    return "jump";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    BlockPos pos = getRayTracedPos(player, 256);
    if (pos.getX() != player.posX && pos.getY() != player.posY && pos.getZ() != player.posZ) {
      TeleportUtils.teleportTo(
          (EntityPlayerMP) player, new LocationWrapper(pos, player.dimension), true);
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).JUMPED);
    } else {
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).TP_BACK_FAIL);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_JUMP;
  }

  public static BlockPos getRayTracedPos(EntityPlayer player, int distance) {
    Vec3d lookAt = player.getLook(1);
    Vec3d pos =
        new Vec3d(
            player.posX,
            player.posY + (player.getEyeHeight() - player.getDefaultEyeHeight()),
            player.posZ);
    Vec3d a = pos.addVector(0, player.getEyeHeight(), 0);
    Vec3d b = a.addVector(lookAt.x * distance, lookAt.y * distance, lookAt.z * distance);
    RayTraceResult result = player.world.rayTraceBlocks(a, b);
    if (player.world.rayTraceBlocks(a, b) != null && Type.BLOCK.equals(result.typeOfHit)) {
      return new BlockPos(result.hitVec.x, result.hitVec.y, result.hitVec.z);
    }
    return player.getPosition();
  }
}
