package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

@ModuleCommand(moduleName = "Teleportation")
public class JumpCommand extends Command {

  @Override
  public String getName() {
    return "Jump";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/jump";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_JUMP;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      BlockPos pos = getRayTracedPos((EntityPlayer) sender.getCommandSenderEntity(), 256);
      if (pos.getX() != sender.getCommandSenderEntity().posX
          && pos.getY() != sender.getCommandSenderEntity().posY
          && pos.getZ() != sender.getCommandSenderEntity().posZ) {
        TeleportUtils.teleportTo(
            (EntityPlayerMP) sender.getCommandSenderEntity(),
            new LocationWrapper(pos, sender.getCommandSenderEntity().dimension),
            false,
            false);
        ChatHelper.sendMessage(sender, LanguageModule.getUserLanguage(sender).local.TELEPORT_JUMP);
      } else {
        // Only Possible via Stuck-In-Block
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
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

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
