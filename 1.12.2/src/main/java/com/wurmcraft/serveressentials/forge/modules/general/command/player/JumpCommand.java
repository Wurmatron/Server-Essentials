package com.wurmcraft.serveressentials.forge.modules.general.command.player;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Jump")
public class JumpCommand {

  public static final int TRACE_LENGTH = 60;

  @Command(inputArguments = {})
  public void jump(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.jump") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        Vec3d vec3d = player.getPositionEyes(0);
        Vec3d vec3d1 = player.getLookVec();
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * TRACE_LENGTH, vec3d1.y * TRACE_LENGTH,
            vec3d1.z * TRACE_LENGTH);
        RayTraceResult rayTrace = player.world
            .rayTraceBlocks(vec3d, vec3d2, false, false, true);
        if (rayTrace != null && rayTrace.typeOfHit == Type.BLOCK) {
          TeleportUtils.teleportTo(player,
              new LocationWrapper(rayTrace.hitVec.x, rayTrace.hitVec.y, rayTrace.hitVec.z,
                  player.dimension));
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_JUMP));
        } else {
          sender.sendMessage(new TextComponentString(ERROR_COLOR +
              PlayerUtils.getUserLanguage(sender).GENERAL_JUMP_INVALID));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
