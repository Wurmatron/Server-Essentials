package com.wurmcraft.serveressentials.common.chat;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.global.Channel.Type;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.chat.events.PlayerChat;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Chat")
public class ChatModule implements IModule {

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new PlayerChat());
    createDefaultChannels();
  }

  private void createDefaultChannels() {
    Channel channel = new Channel("global", "[G]", Type.PUBLIC, "");
    DataHelper.forceSave(Keys.CHANNEL, channel);
    DataHelper.load(Keys.CHANNEL, channel);
  }
}
