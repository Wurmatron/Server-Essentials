package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.ModuleUtils;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.core.api.module.config.GeneralConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "SetSpawn")
public class SetSpawnCommand {

  @Command(inputArguments = {})
  public void setSpawn(ICommandSender sender, String arg) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        GeneralConfig config = (GeneralConfig) SERegistry
            .getStoredData(DataKey.MODULE_CONFIG, "General");
        config.spawn = new LocationWrapper(player.posX, player.posY, player.posZ,
            player.dimension);
        player.world
            .setSpawnPoint(new BlockPos(config.spawn.x, config.spawn.y, config.spawn.z));
        ModuleUtils
            .writeConfigFile(config.getClass().getAnnotation(ConfigModule.class), config);
        SERegistry.register(DataKey.MODULE_CONFIG, config);
        sender.sendMessage(
            TextComponentUtils.addPosition(new TextComponentString(COMMAND_COLOR +
            PlayerUtils.getUserLanguage(sender).GENERAL_SPAWN_SET), config.spawn));
    }
  }

}
