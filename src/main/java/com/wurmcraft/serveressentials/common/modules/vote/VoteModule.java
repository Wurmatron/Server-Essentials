package com.wurmcraft.serveressentials.common.modules.vote;

import com.github.upcraftlp.votifier.api.VoteReceivedEvent;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module(name = "Vote")
public class VoteModule {

  public void setup() {

    if (Loader.isModLoaded("votifier")) {
      MinecraftForge.EVENT_BUS.register(new VoteModule());
    } else {
      ServerEssentialsServer.LOGGER.error(
          "Unable to startup Vote Module due to missing Voting Mod");
      ServerEssentialsServer.LOGGER.error(
          "Check out: https://www.curseforge.com/minecraft/mc-mods/voting");
    }
  }

  @SubscribeEvent
  public void voteEvent(VoteReceivedEvent e) {
    ServerEssentialsServer.LOGGER.info(e.getEntityPlayer().getGameProfile().getId().toString() + " has voted @ " + e.getServiceDescriptor());
    if (UserManager.addReward(e.getEntityPlayer(), 1)) {
      for (EntityPlayer player :
          FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
        ChatHelper.sendMessage(
            player,
            LanguageModule.getUserLanguage(player)
                .local
                .ECO_VOTE
                .replaceAll(Replacment.USER, player.getDisplayNameString())
                .replaceAll(Replacment.LINK, e.getServiceDescriptor()));
      }
    }
  }
}
