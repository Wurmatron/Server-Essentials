package com.wurmcraft.serveressentials.common.chat;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.global.Channel.Type;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.chat.events.PlayerChat;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Chat")
public class ChatModule implements IModule {

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new PlayerChat());
    createDefaultChannels();
  }

  private void createDefaultChannels() {
    Channel global = new Channel("global", "&b[G]", Type.PUBLIC, "");
    Channel local = new Channel("local", "&e[L]", Type.PUBLIC, "");
    Channel team = new Channel("team", "&2[T]", Type.TEAM, "");
    DataHelper.createIfNonExist(Keys.CHANNEL, global);
    DataHelper.createIfNonExist(Keys.CHANNEL, local);
    DataHelper.createIfNonExist(Keys.CHANNEL, team);
    DataHelper.load(Keys.CHANNEL, global);
    DataHelper.load(Keys.CHANNEL, local);
    DataHelper.load(Keys.CHANNEL, team);
  }
}
