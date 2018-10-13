package com.wurmcraft.serveressentials.common.claim2.events;

import com.wurmcraft.serveressentials.api.json.claim2.Claim;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimOwner;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.claim2.ClaimManager;
import com.wurmcraft.serveressentials.common.claim2.SEClaim;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Global;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@EventBusSubscriber(modid = Global.MODID)
public class DisplayCreateClaimEvent {

  private static NonBlockingHashMap<String, LocationWrapper[]> cache = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<String, Long> timeout = new NonBlockingHashMap<>();

  // TODO Config
  public static ItemStack clamingStack = new ItemStack(Items.GOLDEN_AXE);
  public static Block cornerBlocks = Blocks.OBSIDIAN;
  public static final int xMinLength = 12;
  public static final int yMinLength = 8;
  public static final int zMinLength = 12;

  @SubscribeEvent
  public void onInteractEvent(PlayerInteractEvent.RightClickBlock e) {
    if (!e.getEntityPlayer().world.isRemote
        && e.getEntityPlayer().getHeldItemMainhand().isItemEqualIgnoreDurability(clamingStack)) {
      if (cache.get(e.getEntityPlayer().getGameProfile().getId().toString()) != null) {
        // TODO Check for timeout
        LocationWrapper loc = cache.get(e.getEntityPlayer().getGameProfile().getId().toString())[0];
        if (loc != null) {
          cache.get(e.getEntityPlayer().getGameProfile().getId().toString())[1] =
              new LocationWrapper(e.getPos(), e.getEntityPlayer().dimension);
          ChatHelper.sendMessage(
              e.getEntityPlayer(),
              LanguageModule.getLangFromKey(e.getEntityPlayer().getGameProfile().getName())
                  .CLAIM_POS_SET);
          timeout.put(
              e.getEntityPlayer().getGameProfile().getId().toString(), System.currentTimeMillis());
          updateClaimDisplay(e.getEntityPlayer());
        }
      } else {
        cache.put(
            e.getEntityPlayer().getGameProfile().getId().toString(),
            new LocationWrapper[] {
              new LocationWrapper(e.getPos(), e.getEntityPlayer().dimension), null
            });
        timeout.put(
            e.getEntityPlayer().getGameProfile().getId().toString(), System.currentTimeMillis());
        ChatHelper.sendMessage(
            e.getEntityPlayer(),
            LanguageModule.getLangFromKey(e.getEntityPlayer().getGameProfile().getName())
                .CLAIM_POS_SET);
        updateClaimDisplay(e.getEntityPlayer());
      }
      if (finished(e.getEntityPlayer())) {
        createClaim(e.getEntityPlayer());
        ChatHelper.sendMessage(e.getEntityPlayer(), "Claim Created!");
      }
    }
  }

  // TODO Requires Testing
  public static void updateClaimDisplay(EntityPlayer player) {
    if (player.world.isRemote) {
      World world = player.world;
      LocationWrapper[] loc = cache.get(player.getGameProfile().getId().toString());
      world.notifyBlockUpdate(
          loc[0].getPos(), world.getBlockState(loc[0].getPos()), cornerBlocks.getDefaultState(), 3);
      world.notifyBlockUpdate(
          loc[1].getPos(), world.getBlockState(loc[1].getPos()), cornerBlocks.getDefaultState(), 3);
    }
  }

  private static boolean finished(EntityPlayer player) {
    LocationWrapper[] locations = cache.get(player.getGameProfile().getId().toString());
    return locations[0] != null && locations[1] != null;
  }

  private static void createClaim(EntityPlayer player) {
    LocationWrapper[] locations = cache.get(player.getGameProfile().getId().toString());
    if (validClaimSize(locations[0], locations[1])) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(player)[0];
      Claim claim =
          new SEClaim(
              new ClaimOwner(user.getTeam(), player.getGameProfile().getId()),
              locations[0],
              locations[1]);
      ClaimManager.getFromDimID(player.dimension).saveClaim(claim);
    }
  }

  private static boolean validClaimSize(LocationWrapper a, LocationWrapper b) {
    int xCheck = Math.abs(a.getX() - b.getX());
    int yCheck = Math.abs(a.getY() - b.getY());
    int zCheck = Math.abs(a.getZ() - b.getZ());
    return xCheck >= xMinLength && yCheck >= yMinLength && zCheck >= zMinLength;
  }
}
